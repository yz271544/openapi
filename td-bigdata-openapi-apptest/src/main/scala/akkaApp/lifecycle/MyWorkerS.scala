package akkaApp.lifecycle

import akka.actor.Actor
import akka.actor.Actor.Receive
import akka.event.{Logging, LoggingAdapter}

/**
  * Created by Administrator on 2016/3/25.
  */
class MyWorkerS extends Actor{

  val log:LoggingAdapter = Logging.getLogger(context.system,this)

  override def preStart(): Unit ={
    println("MyWorker is starting!")
  }

  override def postStop(): Unit ={
    println("MyWorker is stopping!")
  }

  override def receive: Receive = {
    case "WORKING" =>
      println("I am working!")
    case "DONE" =>{
      println("I will shutdown!")
      sender() ! "CLOSE"
      context.stop(self)
    }
    case _ =>
      unhandled("nothing!")
  }
}
