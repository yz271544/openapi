package akkaApp

import akka.actor.{ActorRef, Props, ActorSystem}


/**
  * Created by Administrator on 2016/3/25.
  */
object HelloMainSimpleForScala {

  def main(args: Array[String]) {
    val _system = ActorSystem("HelloForScala")
    val helloWorldActorRef:ActorRef = _system.actorOf(Props[HelloWorldActor])
    println("HelloWorld Actor path:"+helloWorldActorRef.path)

  }

}
