package com.effective.common.akka.part1;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * @Dessciption TODO
 * @Author yanweiqi
 * @Date 2019-10-31 10:07 PM
 */
public class LookupActor extends UntypedActor {

    private LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);

    private ActorRef target;
    {
        target = getContext().actorOf(Props.create(ForwordActor.TargetActor.class), "targetActor");
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

    public static void main(String[] args){
        ActorSystem system = ActorSystem.create("testSystem");
        ActorRef actorRef = system.actorOf(Props.create(LookupActor.class),"lookupActor");
        actorRef.tell("find",actorRef);
    }
}
