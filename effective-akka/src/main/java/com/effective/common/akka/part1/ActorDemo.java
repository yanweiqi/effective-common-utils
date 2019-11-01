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
 * @Date 2019-10-31 6:42 PM
 */
public class ActorDemo extends UntypedActor {

    private LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Throwable {

        if (message instanceof String) {
            log.info("接收到:{},发送者:{}", message.toString(), getSender());
        } else {
            unhandled(message);
        }
    }

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("testSystem");
        ActorRef actorRef = system.actorOf(Props.create(ActorDemo.class),"actorFirst");
        actorRef.tell("测试消息",actorRef);
    }
}
