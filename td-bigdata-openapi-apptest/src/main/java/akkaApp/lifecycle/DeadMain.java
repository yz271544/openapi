package akkaApp.lifecycle;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

/**
 * Created by Administrator on 2016/3/25.
 */
public class DeadMain {

    public static void main(String[] args){
        //创建ActorSystem全局实例
        ActorSystem system = ActorSystem.create("deadwatch", ConfigFactory.load("samplehello,conf"));
        //创建MyWorker和Watch Actor
        ActorRef worker = system.actorOf(Props.create(MyWorker.class),"worker");
        //第一个参数是：要创建的Actor类型
        //第二个参数是：为这个Actor的构造函数的参数
        system.actorOf(Props.create(WatchActor.class,worker),"watcher");

        worker.tell(MyWorker.Msg.WORKING,ActorRef.noSender());
        worker.tell(MyWorker.Msg.DONE,ActorRef.noSender());
        worker.tell(PoisonPill.getInstance(), ActorRef.noSender());
    }
}
