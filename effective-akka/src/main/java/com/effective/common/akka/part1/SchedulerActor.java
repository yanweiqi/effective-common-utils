package com.effective.common.akka.part1;

import akka.actor.*;
import org.junit.Test;

import java.time.Duration;

/**
 * @Dessciption TODO
 * @Author yanweiqi
 * @Date 2019-11-07 4:36 PM
 */
public class SchedulerActor {


    private static ActorSystem system = ActorSystem.create("testSystem");

    private static ActorRef testActor = system.actorOf(Props.create(MyActor.class));

    private static void testCase1() {
        system.scheduler().scheduleOnce(
                Duration.ofMillis(50),
                testActor,
                "foo",
                system.dispatcher(),
                ActorRef.noSender());
    }

    private static void testCase2() {
        system.scheduler().scheduleOnce(
                Duration.ofMillis(50),
                () -> {
                    testActor.tell(System.currentTimeMillis(), ActorRef.noSender());
                },
                system.dispatcher());
    }


    public void testCase3() {

        class Ticker extends AbstractActor {

            @Override
            public Receive createReceive() {
                return receiveBuilder()
                        .matchEquals("tick", s -> {

                        })
                        .build();
            }
        }
    }


    public static void main(String[] args) {

        testCase1();

        testCase2();
    }
}
