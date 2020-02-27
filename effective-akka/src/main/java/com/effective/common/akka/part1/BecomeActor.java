package com.effective.common.akka.part1;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Procedure;
import com.effective.common.akka.part1.msg.Employee;

/**
 * @Dessciption TODO
 * @Author yanweiqi
 * @Date 2019-11-1 5:07 PM
 */
public class BecomeActor {

    private static ActorSystem system = ActorSystem.create("testSystem");

    public static void main(String[] args) {
        //testCase1();

        testCase2();
    }


    private static void testCase1() {
        ActorRef ref = system.actorOf(Props.create(BecomeActorDemo.class), "becomeActor");
        ref.tell("hello", ActorRef.noSender());
        ref.tell("hi", ActorRef.noSender());
        ref.tell("hi", ActorRef.noSender());
        ref.tell("hi", ActorRef.noSender());
        ref.tell("hi", ActorRef.noSender());
    }

    private static void testCase2() {
        ActorRef ref = system.actorOf(Props.create(UnBecomeActorDemo.class), "unBecomeActorDemo");
        ref.tell(1, ActorRef.noSender());
        ref.tell(new Employee("张三", 10000), ActorRef.noSender());
        ref.tell(new Employee("李四", 20000), ActorRef.noSender());


        ref.tell("end", ActorRef.noSender());

        ref.tell(2, ActorRef.noSender());
        ref.tell(new Employee("王五", 10000), ActorRef.noSender());
        ref.tell(new Employee("赵六", 20000), ActorRef.noSender());

    }

}

enum BonusEnum {
    LEVEL1(1.8f),
    LEVEL2(1.5f),
    ;

    private float ratio;

    BonusEnum(float ratio) {
        this.ratio = ratio;
    }

    public float getRatio() {
        return ratio;
    }
}

/**
 * 这个技术做业务交换挺合适，或者业务降级
 */
class UnBecomeActorDemo extends UntypedActor {
    private LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);

    Procedure<Object> level1 = (message) -> {
        if (message instanceof String) {
            if (message.equals("end")) {
                getContext().unbecome();
            }
        } else if (message instanceof Employee) {
            Employee emp = (Employee) message;
            double salary = emp.getSalary() * BonusEnum.LEVEL1.getRatio();
            log.info("员工{}:{}", emp.getName(), salary);

        } else {
            unhandled(message);
        }
    };


    Procedure<Object> level2 = (message) -> {
        if (message instanceof String) {
            if (message.equals("end")) {
                getContext().unbecome();
            }
        } else if (message instanceof Employee) {
            Employee emp = (Employee) message;
            double salary = emp.getSalary() * BonusEnum.LEVEL2.getRatio();
            log.info("员工{}:{}", emp.getName(), salary);

        } else {
            unhandled(message);
        }
    };

    @Override
    public void onReceive(Object message) throws Throwable {

        if (message instanceof Integer) {
            Integer v = (Integer) message;
            switch (v) {
                case 1:
                    getContext().become(level1);
                    break;
                case 2:
                    getContext().become(level2);
                    break;
                default:
                    break;
            }
        } else if (message instanceof String) {
            getContext().become(level1);
        } else {
            unhandled(message);
        }
    }
}


/**
 * 一旦执行了become后，之后的输出全是走become,这个特性比较魔性
 */
class BecomeActorDemo extends UntypedActor {

    private LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);

    Procedure<Object> proc = (message) -> {
        log.info("become:{}", message);
    };

    @Override
    public void onReceive(Object message) {
        log.info("接收到的信息:{}", message);
        getContext().become(proc);
        log.info("----------------------");
    }
}