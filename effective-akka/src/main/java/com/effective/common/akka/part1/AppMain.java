package com.effective.common.akka.part1;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.dispatch.OnSuccess;
import akka.event.LoggingAdapter;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * @Dessciption TODO
 * @Author yanweiqi
 * @Date 2019-10-30 9:16 PM
 */
public class AppMain {

    public final static ActorSystem system = ActorSystem.create("testSystem");

    private final static LoggingAdapter log = system.log();

    public static void main(String[] args) {

        /*
        log.info("----------------split 0------------------");
        testCaseActor();

        log.info("----------------split 1------------------");
        testCaseAskActor();

        log.info("----------------split 2------------------");
        testCaseForwardActor();

        log.info("----------------split 3------------------");
        */
        testCaseLookupActor();
    }

    public static void testCaseActor(){

    }

    /**
     * test case AskActor
     */
    public static void testCaseAskActor() {

    }

    /**
     * test case ForwardActor
     */
    public static void testCaseForwardActor(){

    }

    /**
     * test case lookup actor
     */
    public static void testCaseLookupActor(){


    }
}
