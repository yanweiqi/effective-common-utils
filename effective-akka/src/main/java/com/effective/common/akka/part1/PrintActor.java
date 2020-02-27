package com.effective.common.akka.part1;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * @Dessciption TODO
 * @Author yanweiqi
 * @Date 2019-11-07 5:15 PM
 */
public class PrintActor extends AbstractActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder().matchEquals("print",
                p -> {
                    ActorRef secondRef = getContext().actorOf(Props.empty(), "second-actor");
                    System.out.println("Second Actor:" + secondRef);
                }
        ).build();
    }
}


class PrintApp {

    public static void main(String[] args) {

        ActorSystem system = ActorSystem.create("testSystem");

        ActorRef firstActor = system.actorOf(Props.create(PrintActor.class), "firstActor");

        System.out.println("First Actor:" + firstActor);

        System.out.println(">>> Press Enter to Exist <<<");

        try {
            System.in.read();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            system.terminate();
        }

    }
}