package com.effective.common.akka.part1;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Function;
import scala.Option;
import scala.concurrent.duration.Duration;

import java.io.IOException;

/**
 * @Dessciption TODO
 * @Author yanweiqi
 * @Date 2019-11-03 1:30 PM
 */
public class SupervisorActor extends UntypedActor {

    private static LoggingAdapter log;

    {
        log = Logging.getLogger(this.getContext().system(), this);
    }


    private static ActorSystem system = ActorSystem.create("testSystem");

    static ActorRef child;


    @Override
    public void preStart() throws Exception {
        //创建子Actor
        child = getContext().actorOf(Props.create(WorkerStrategyActor.class), "child");
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
                if (param instanceof NullPointerException) {
                    log.error("收到NullPointerException异常，执行resume");
                    return SupervisorStrategy.resume();
                } else if (param instanceof IOException) {
                    log.error("收到IOException异常，执行restart");
                    return SupervisorStrategy.restart();
                } else {
                    log.error("严重异常，关闭服务");
                    return SupervisorStrategy.escalate();
                }
            }
        });
    }

    @Override
    public void onReceive(Object message) {
        child.tell(message, ActorRef.noSender());
    }


    public static void main(String[] args) {

        ActorRef supervisorActor = system.actorOf(Props.create(SupervisorActor.class), "SupervisorActor");
        supervisorActor.tell("文本消息", supervisorActor);
        try {
            //执行暂停
            supervisorActor.tell(new NullPointerException(), supervisorActor);
            //执行重启
            supervisorActor.tell(new IOException(), supervisorActor);
            //执行关闭
            supervisorActor.tell(new Throwable(), supervisorActor);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}


class WorkerStrategyActor extends UntypedActor {

    private LoggingAdapter log = getContext().system().log();

    private int preRestart = 1;
    private int postRestart = 1;

    @Override
    public void preStart() {
        super.receive();
        log.info("workerActor,执行了preStart");
    }

    @Override
    public void preRestart(Throwable reason, Option<Object> message) throws Exception {
        preRestart++;
        log.info("workerActor 执行了preRestart {}", this.preRestart);
        super.preRestart(reason, message);
    }

    @Override
    public void postStop() throws Exception {
        super.postStop();
        log.info("workerActor,执行了postStop");
    }

    @Override
    public void postRestart(Throwable reason) throws Exception {
        log.info("workerActor 执行了postRestart {}", this.postRestart);
        super.postRestart(reason);
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof Exception) {
            throw (Exception) message;
        } else if (message instanceof Throwable) {
            throw (Throwable) message;
        } else if (message instanceof String) {
            log.info("接收到文本消息 {}", message);
        } else {
            unhandled(message);
        }
    }
}