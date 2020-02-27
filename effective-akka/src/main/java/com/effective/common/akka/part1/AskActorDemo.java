package com.effective.common.akka.part1;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.dispatch.OnSuccess;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * @Dessciption TODO
 * @Author yanweiqi
 * @Date 2019-10-31 7:31 PM
 */
public class AskActorDemo {


    public static void main(String[] args) {

        ActorSystem system = ActorSystem.create("testSystem");
        LoggingAdapter log = system.log();
        
        ActorRef actorRef = system.actorOf(Props.create(AskActor.class), "askActor");
        log.info("引用Actor的路径:{}", actorRef.path());
        Timeout timeout = new Timeout(Duration.create(2, TimeUnit.SECONDS));
        Future<Object> f = Patterns.ask(actorRef, "akka ask", timeout);
        log.info("...ask...");
        f.onSuccess(new OnSuccess<Object>() {
            @Override
            public void onSuccess(Object result) throws Throwable {
                log.info("收到消息：{}", result);
            }
        }, system.dispatcher());

        log.info("continue......");
    }
}


class AskActor extends UntypedActor {

    private LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);
    @Override
    public void onReceive(Object message) {
        log.info("发送者是：{}" + getSender());
        getSender().tell("hello " + message, getSelf());
    }
}