package openapi.deploy.client

import akka.actor._
import openapi.Logging
import openapi.deploy.{RegisteredApplication, RegisterApplication, ApplicationDescription}
import openapi.deploy.master.Master

/**
  * Created by Administrator on 2016/3/15.
  */
private[openapi] class Client(
      actorSystem:ActorSystem,
      masterUrl:String,
      appDescription:ApplicationDescription,
      listener: ClientListener)
     extends Logging{


  var actor: ActorRef = null
  var appId: String = null

  class ClientActor extends Actor with Logging{

    var master: ActorRef = null
    var masterAddress: Address = null
    var alreadyDisconnected = false //

    var masterSelection: ActorSelection = null

    override def preStart(){
       logInfo("Connecting to master " + masterUrl)
       try{
          //masterSelection =  context.actorSelection(Master.toAkkaUrl(masterUrl))

          //master = context.actorOf(Props(Master.getClass),"Master")
         logInfo(">>>>>>>>>>>>>>"+Master.toAkkaUrl(masterUrl))
          master = context.actorFor(Master.toAkkaUrl(masterUrl))
          //masterAddress = master.path.address
          master ! RegisterApplication(appDescription)
          //context.watch(master)
          logInfo(">>>>>>>>>>>>>>")
          //masterSelection ! RegisterApplication(appDescription)
       }catch{
         case e: Exception =>
           logError("Failed to connect to master", e)
           markDisconnected()
           context.stop(self)
       }
    }

    override def receive = {
      case RegisteredApplication(appId_) =>
        appId = appId_
        listener.connected(appId)

    }

    def markDisconnected() {
      if (!alreadyDisconnected) {
        listener.disconnected()
        alreadyDisconnected = true
      }
    }
  }

  def start() {
    // Just launch an actor; it will call back into the listener.
    logInfo(">>>>>>>>创建了 ClientRef>>>>>>")
    actor = actorSystem.actorOf(Props(new ClientActor))
  }

}















