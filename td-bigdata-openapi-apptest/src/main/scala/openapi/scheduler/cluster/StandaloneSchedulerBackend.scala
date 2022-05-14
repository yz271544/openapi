package openapi.scheduler.cluster

import scala.collection.mutable.{ArrayBuffer, HashMap, HashSet}

import akka.actor.{Props, Actor, ActorRef, ActorSystem}
import openapi.Logging

/**
  * Created by Administrator on 2016/3/15.
  */
private[openapi] class StandaloneSchedulerBackend(scheduler: ClusterScheduler,actorSystem: ActorSystem)
   extends SchedulerBackend with Logging{


  class DriverActor(openapiProperties:Seq[(String,String)]) extends Actor{

    override def preStart(): Unit ={
      logInfo("driver_actor is starting..........")
    }

    override def receive = {
      case RegisteredExecutor(sparkProperties) =>
        logInfo("Successfully registered with driver")
    }
  }


  var driverActor: ActorRef = null
  val taskIdsOnSlave = new HashMap[String,HashSet[String]]


  override def start(){

    val properties = new ArrayBuffer[(String, String)]
    val iterator = System.getProperties.entrySet.iterator
    while (iterator.hasNext) {
      val entry = iterator.next
      val (key, value) = (entry.getKey.toString, entry.getValue.toString)
      if (key.startsWith("openapi.")) {
        properties += ((key, value))
      }
    }

    driverActor = actorSystem.actorOf(Props(new DriverActor(properties)),StandaloneSchedulerBackend.ACTOR_NAME)

    logInfo("driver_actor is started..........")

  }

  override def stop(): Unit ={

  }

  override def reviveOffers(): Unit ={

  }

  override def defaultParallelism: Unit ={

  }

}

private[openapi] object StandaloneSchedulerBackend{
  val ACTOR_NAME = "StandaloneScheduler"
}
