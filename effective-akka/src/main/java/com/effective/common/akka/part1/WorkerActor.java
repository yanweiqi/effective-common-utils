package com.effective.common.akka.part1;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * @Dessciption TODO
 * @Author yanweiqi
 * @Date 2019-11-02 11:11 AM
 */
public class WorkerActor {

    private static ActorSystem system = ActorSystem.create("testSystem");

    public static void main(String[] args) {

        /**
         * 当停止Actor时候吧
         * 1.正在处理的消息会在完全停止之前处理完毕，后续的信息不再进行处理，邮箱用来保存Actor的消息将被挂起
         * 2.给所有子Actor发送终止指令，当子级都停掉以后，在停掉自己，停止完毕后会调用prosStop方法，这里可以清理和释放资源
         * 3.向生命周期监控者【Death Watch】，发送Terminated消息，以便监控者
         */

        //testCase1();
        //testCase2();
        //testCase3();

        testCase4();

    }


    static void testCase1(){
        ActorRef ref = system.actorOf(Props.create(Worker.class), "worker");
        //通过System的stop方法
        system.stop(ref);
        ref.tell("测试消息", ActorRef.noSender());
    }

    static void testCase2(){
        ActorRef ref = system.actorOf(Props.create(Worker.class), "worker");
        //通过tell发送毒丸
        ref.tell(PoisonPill.getInstance(), ActorRef.noSender());

        ref.tell("测试消息", ActorRef.noSender());
    }

    static void testCase3(){
        ActorRef ref = system.actorOf(Props.create(Worker.class), "worker");
        //通过kill
        ref.tell(Kill.getInstance(), ActorRef.noSender());
        ref.tell("测试消息", ActorRef.noSender());
    }


    static void testCase4(){
        ActorRef ref = system.actorOf(Props.create(WatchActor.class), "watchActor");
        //通过kill
        ref.tell(Kill.getInstance(), ActorRef.noSender());
        ref.tell("测试消息", ActorRef.noSender());
    }
}


class Worker extends UntypedActor {
    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    @Override
    public void onReceive(Object message) throws Throwable {
        log.info("收到消息：{}", message);
    }
    @Override
    public void postStop() throws Exception {
        log.info("Worker 停止运行.....");
    }
}

/**
 * 通过WatchActor观察ChildAtor的关闭情况
 */
class WatchActor extends UntypedActor {

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    ActorRef child;

    @Override
    public void preStart() throws Exception {
        super.preStart();
        child = getContext().actorOf(Props.create(WorkerChildA.class), "workerA");
        getContext().watch(child);
    }

    @Override
    public void postStop() throws Exception {
        super.postStop();
        log.info("WatchActor停止运行......");
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof String) {
            if (message.equals("stopChild")) {
                getContext().stop(child);
            }
        } else if (message instanceof Terminated) {
            Terminated t = (Terminated) message;
            log.info("监控到 {} 停止了", t.getActor());
        } else {
            unhandled(message);
        }
    }
}

class WorkerChildA extends UntypedActor {
    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    @Override
    public void onReceive(Object message) throws Throwable {
        log.info("收到消息：{}", message);
    }

    @Override
    public void postStop() throws Exception {
        log.info("Worker child 停止运行.....");
    }

}