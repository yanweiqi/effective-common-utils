package com.effective.common.akka.part1;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * @Dessciption TODO
 * @Author yanweiqi
 * @Date 2019-11-07 4:28 PM
 */
public class MyActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(
                String.class,
                s -> {
                    log.info("Receive String message:{}", s);
                    getSender().tell(s, getSelf());
                }).
                matchAny(o -> {
                    log.info("Received message Unknown message");
                    unhandled(o);
                })
                .build();
    }
}
