package com.effective.common.akka.part1;

import akka.actor.*;
import akka.dispatch.OnFailure;
import akka.dispatch.OnSuccess;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.util.Timeout;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * @Dessciption TODO
 * @Author yanweiqi
 * @Date 2019-10-31 10:07 PM
 */
public class LookupActor {

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("testSystem");
        ActorRef actorRef = system.actorOf(Props.create(LookupName.class), "lookupName");
        actorRef.tell("find", actorRef);

        System.out.println("======================");

        ActorRef actorPath = system.actorOf(Props.create(LookupPath.class),"lookupPath");
        actorPath.tell("/user/lookupName/targetActor",actorPath);
    }
}


class LookupPath extends UntypedActor {

    private LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Throwable {

        if (message instanceof String) {
            String path = message.toString();
            log.info("查找的路径{}下的Actor", path);
            ActorSelection as = getContext().actorSelection(path);

            Timeout timeout = new Timeout(Duration.create(2, TimeUnit.SECONDS));

            Future<ActorRef> future = as.resolveOne(timeout);

            future.onSuccess(new OnSuccess<ActorRef>() {
                @Override
                public void onSuccess(ActorRef result) throws Throwable {
                  log.info("查看到的Actor:{}",result);
                }
            },getContext().system().dispatcher());

            future.onFailure(new OnFailure() {
                @Override
                public void onFailure(Throwable failure) throws Throwable {
                    if(failure instanceof ActorNotFound){
                        log.info("查不到对应的actor");
                    }
                }
            },getContext().system().dispatcher());
        }
    }
}


/**
 * 根据名称查找
 */
class LookupName extends UntypedActor {

    private LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);

    {
        getContext().actorOf(Props.create(ForwordActor.TargetActor.class), "targetActor");
    }


    @Override
    public void onReceive(Object message) {

        if (message instanceof String) {
            if ("find".equals(message)) {
                log.info("接收到查找的命令");
                ActorSelection as = getContext().actorSelection("targetActor");
                as.tell(new Identify("A001"), getSelf());
            }
        } else if (message instanceof ActorIdentity) {
            ActorIdentity ai = (ActorIdentity) message;
            ActorRef ref = ai.getRef();
            if (ref != null) {
                log.info("ActorIdentity {} {}", ai.correlationId(), ref);

                ref.tell("hello", getSelf());
            }
        } else {
            unhandled(message);
        }
    }
}