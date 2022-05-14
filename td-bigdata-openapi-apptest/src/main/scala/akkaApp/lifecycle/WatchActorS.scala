package akkaApp.lifecycle

import akka.actor.{Terminated, ActorRef, Actor}
import akka.actor.Actor.Receive

/**
  * Created by Administrator on 2016/3/25.
  */
class WatchActorS(ref:ActorRef) extends Actor{

  val watchers = context.watch(ref)

  override def receive: Receive = {
    case s:Terminated =>{
      println(String.format("%s has terminated,shutting down system",s.getActor().path))
      context.system.shutdown
    }
    case _ =>
      unhandled("nothing!")

  }
}
