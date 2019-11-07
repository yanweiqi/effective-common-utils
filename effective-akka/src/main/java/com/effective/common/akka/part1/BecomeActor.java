package com.effective.common.akka.part1.msg;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Procedure;

/**
 * @Dessciption TODO
 * @Author yanweiqi
 * @Date 2019-11-1 5:07 PM
 */
public class BecomeActor {

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("testSystem");

        ActorRef ref = system.actorOf(Props.create(BecomeActorDemo.class),"becomeActor");

        ref.tell("hello",ActorRef.noSender());
        ref.tell("hi",ActorRef.noSender());
        ref.tell("hi",ActorRef.noSender());
        ref.tell("hi",ActorRef.noSender());
        ref.tell("hi",ActorRef.noSender());
    }

}


class UnBecomeActorDemo extends UntypedActor {

    Procedure<Object> Level1 = (message)->{
      if(message instanceof String){
          if(message.equals("end")){
              getContext().unbecome();
          } else {
              
          }
      }
    };

    @Override
    public void onReceive(Object message) throws Throwable {

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