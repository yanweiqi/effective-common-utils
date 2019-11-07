package com.effective.common.akka.part1;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Function;
import scala.Option;
import scala.concurrent.duration.Duration;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @Dessciption TODO
 * @Author yanweiqi
 * @Date 2019-11-03 1:30 PM
 */
public class SupervisorActor extends UntypedActor {

    private LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);
    private static ActorSystem system = ActorSystem.create("testSystem");


    @Override
    public void preStart() throws Exception {
        //创建子Actor
        ActorRef child = getContext().actorOf(Props.create(WorkerStrategyActor.class), "child");
        //监控生命周期
        getContext().watch(child);
    }

    /**
     * 定义监督策略
     */
    @Override
    public SupervisorStrategy supervisorStrategy() {
        return new OneForOneStrategy(3, Duration.create("1 minute"), new Function<Throwable, SupervisorStrategy.Directive>() {
            @Override
            public SupervisorStrategy.Directive apply(Throwable param) throws Exception {

                if (param instanceof IOException) {
                    log.error("IoException");
                    return SupervisorStrategy.resume();
                } else if (param instanceof IndexOutOfBoundsException) {
                    log.error("IndexOutOfBoundsException");
                    return SupervisorStrategy.restart();
                } else {
                    return SupervisorStrategy.escalate();
                }
            }
        }

        );
    }

    @Override
    public void onReceive(Object message) throws Throwable {

        if (message instanceof Terminated) {
            Terminated ter = (Terminated) message;
            log.info("{}已经终止", ter.getActor());
        } else {
            log.info(" status {}", message);
        }
    }


    public static void main(String[] args) {

        ActorRef workerActor = system.actorOf(Props.create(SupervisorActor.class), "SupervisorActor");

        workerActor.tell(new IOException(), workerActor);

        workerActor.tell(new SQLException("sql exception"), workerActor);

        workerActor.tell(new IndexOutOfBoundsException(), workerActor);

        workerActor.tell("getValue", workerActor);
    }
}


class WorkerStrategyActor extends UntypedActor {

    private LoggingAdapter log = getContext().system().log();

    private int count = 1;

    @Override
    public void preStart() throws Exception {
        super.receive();
        log.info("workerActor,执行了preStart");
    }

    @Override
    public void postStop() throws Exception {
        super.postStop();
        log.info("workerActor,执行了postStop");
    }


    @Override
    public void preRestart(Throwable reason, Option<Object> message) throws Exception {
        log.info("workerActor preRestart begin {}", this.count);
        super.preRestart(reason, message);
        log.info("workerActor reRestart end {}", this.count);
    }

    @Override
    public void postRestart(Throwable reason) throws Exception {
        log.info("workerActor postRestart {}", this.count);
        super.postRestart(reason);
        log.info("workerActor postRestart begin {}", this.count);
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        this.count++;
        if (message instanceof Exception) {
            throw (Exception) message;
        } else if ("getValue".equals(message)) {
            getSender().tell(count, getSelf());
        } else {
            unhandled(message);
        }
    }
}