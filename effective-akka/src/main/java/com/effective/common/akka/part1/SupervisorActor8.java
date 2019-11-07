package com.effective.common.akka.part1;

import akka.actor.*;
import akka.event.LoggingAdapter;
import akka.japi.Function;
import akka.japi.pf.DeciderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Option;
import scala.concurrent.Await;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static akka.pattern.Patterns.ask;

/**
 * @Dessciption TODO
 * @Author yanweiqi
 * @Date 2019-11-03 1:30 PM
 */
public class SupervisorActor8 {


    static class Supervisor extends AbstractActor {

        private static LoggingAdapter log;

        {
            log = getContext().getSystem().log();
        }

        private static SupervisorStrategy strategy = new OneForOneStrategy(
                10,
                Duration.ofMinutes(1), new Function<Throwable, SupervisorStrategy.Directive>() {
            @Override
            public SupervisorStrategy.Directive apply(Throwable param) throws Exception {

                if (param instanceof NullPointerException) {
                    log.error("收到NullPointerException,执行resume");
                    return SupervisorStrategy.resume();
                } else if (param instanceof IOException) {
                    log.error("收到IOException，执行restart");
                    return SupervisorStrategy.restart();
                } else {
                    return SupervisorStrategy.escalate();
                }
            }
        });

                /*
                DeciderBuilder.match(ArithmeticException.class, new App)
                        .match(NullPointerException.class, e -> SupervisorStrategy.restart())
                        .match(IllegalArgumentException.class, e -> SupervisorStrategy.stop())
                        .matchAny(o -> SupervisorStrategy.escalate()).build());*/

        @Override
        public SupervisorStrategy supervisorStrategy() {
            return strategy;
        }

        @Override
        public Receive createReceive() {
            return receiveBuilder().match(Props.class,
                    props -> {
                        //以下代码是必须要执行的，不执行就会报错
                        getSender().tell(getContext().actorOf(props), getSelf());
                    }).build();
        }

        @Override
        public void preStart() throws Exception {
            super.preStart();
            log.info("watcher 执行preStart");
        }
    }

    static class Child extends AbstractActor {

        private static LoggingAdapter log;

        {
            log = getContext().getSystem().log();
        }

        int state = 0;

        @Override
        public void preStart() throws Exception {
            super.preStart();
            log.info("child 执行preStart");
        }

        @Override
        public Receive createReceive() {
            return receiveBuilder()
                    .match(Integer.class, i -> {
                        state = i;
                        log.info("接收到数字为:{}", state);
                    })
                    .match(String.class, s -> {
                        log.info("接收到字符串:{}", s);
                    })
                    .matchAny(o -> {
                        throw (Exception) o;
                    })
                    .build();
        }
    }
}


class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    private static ActorSystem system = ActorSystem.create("testSystem");

    private static scala.concurrent.duration.Duration timeout = scala.concurrent.duration.Duration.create(2, TimeUnit.SECONDS);

    public static void main(String[] args) throws Exception {

        Props superpros = Props.create(SupervisorActor8.Supervisor.class);
        ActorRef supervisor = system.actorOf(superpros, "supervisor");
        ActorRef child = (ActorRef) Await.result(ask(supervisor, Props.create(SupervisorActor8.Child.class), 5000), timeout);
        child.tell(10, ActorRef.noSender());

        child.tell("文本消息体", ActorRef.noSender());

        //child.tell(new NullPointerException(), ActorRef.noSender());

        child.tell(new IOException(), ActorRef.noSender());

        child.tell("文本消息体", ActorRef.noSender());

    }

}



