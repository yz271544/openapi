package typedActorExample.calculator

import akka.actor.Actor
import akka.actor.Actor.Receive
import akka.event.Logging

/**
  * Created by Administrator on 2016/3/24.
  */
class CalculatorRef extends Actor{

  val log = Logging(context.system,Actor.getClass)


  override def receive: Receive = {
    case "hello" => log.info ("hello")
  }

  override def preStart() ={
    log.info("Actor Started")
  }

  override def postStop():Unit={
    log.info("Actor Stopped")
  }
}
