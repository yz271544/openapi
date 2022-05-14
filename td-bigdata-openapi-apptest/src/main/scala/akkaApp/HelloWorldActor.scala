package akkaApp

import akka.actor.{Props, ActorRef, Actor}
import akka.actor.Actor.Receive


/**
  * Created by Administrator on 2016/3/25.
  */
class HelloWorldActor extends Actor{

  override def preStart ={

    val greeter:ActorRef = context.actorOf(Props[GreeterActor])
    println("Greeter Actor Path:" + greeter.path)
    greeter ! HelloWorld2("hello akka!")

  }

  override def receive: Receive = {

    case GreeterMessage(msg:String) =>
       sender ! HelloWorld2("hello akka!"+msg)
       context.stop(self)
    case _ =>
      unhandled("nothing!")

  }
}
