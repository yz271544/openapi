package com.teradata.openapi.master.test


import _root_.akka.actor.{Props, Actor, ActorSystem,ActorRef}
import com.teradata.openapi.framework.{OpenApiEnv, OpenApiConf, OpenApiLogging}
import com.teradata.openapi.framework.scheduler.CoarseThreadBackend
import com.teradata.openapi.framework.util.AkkaUtils


/**
  * Created by Administrator on 2016/3/31.
  */
class MasterBackend(actorSystem: ActorSystem)
  extends CoarseThreadBackend with OpenApiLogging {

  private val timeout = AkkaUtils.askTimeout(new OpenApiConf())

  class MasterActor() extends Actor{

    override def preStart(): Unit ={
      logInfo("准备启动Master Actor .......")
    }

    override def receive: Receive = {
      case message:String =>
        sender ! "收到后再发给客户端"
    }
  }

  var masterActor:ActorRef = null


  override def start(): Unit = {
    masterActor = actorSystem.actorOf(Props(new MasterActor),name = "master")
  }

  override def stop(): Unit = {

  }


}

object MasterBackend{

  def main(args: Array[String]) {

    val config = new OpenApiConf()
    config.set("openapi.master.hostname","127.0.0.1")
    config.set("openapi.master.hostname","")

    val env = OpenApiEnv.create(config,"127.0.0.1",4567,false)
    OpenApiEnv.set(env)
    val master = new MasterBackend(env.actorSystem).start()
  }
}
