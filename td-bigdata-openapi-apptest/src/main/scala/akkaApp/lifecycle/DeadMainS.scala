package akkaApp.lifecycle

import akka.actor.{PoisonPill, Props, ActorRef, ActorSystem}

/**
  * Created by Administrator on 2016/3/25.
  */
object DeadMainS {

  def main(args: Array[String]) {

    val _system = ActorSystem.create("deadwatch")
    val worker: ActorRef = _system.actorOf(Props[MyWorkerS],"worker")
    val watcher = _system.actorOf(Props.create(classOf[WatchActorS],worker),"watcher")
    worker ! "WORKING"
    worker ! "DONE"
    worker ! PoisonPill.getInstance
  }

}
