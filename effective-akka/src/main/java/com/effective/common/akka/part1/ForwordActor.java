package com.effective.common.akka.part1;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * @Dessciption TODO
 * @Author yanweiqi
 * @Date 2019-10-31 9:25 PM
 */
public class ForwordActor extends UntypedActor {

    private LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);


    @Override
    public void onReceive(Object message) throws Throwable {
        ActorRef target = getContext().actorOf(Props.create(TargetActor.class), "targetActor");
        log.info("不做任何处理直接转发");
        target.forward(message, getContext());
    }


    static class TargetActor extends UntypedActor {
        private LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);
        @Override
        public void onReceive(Object message) throws Throwable {
            log.info("接收到:{}, 发送者:{}", message, getSender());
        }
    }


    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("testSystem");
        ActorRef forwardActor = system.actorOf(Props.create(ForwordActor.class),"forWordActor");
        forwardActor.tell("转发消息内容",forwardActor);
    }
}


