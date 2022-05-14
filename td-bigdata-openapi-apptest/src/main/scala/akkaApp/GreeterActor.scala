package akkaApp

import akka.actor.{ActorRef, Actor}
import akka.actor.Actor.Receive

/**
  * Created by Administrator on 2016/3/25.
  */
class GreeterActor extends Actor{

  override def receive: Receive = {

    case HelloWorld2(mes:String) =>{
      println(mes)
      sender ! GreeterMessage("GREET")
    }
    case _ =>
      unhandled("nothing```")
  }
}
