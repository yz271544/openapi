package openapi.deploy.master

import java.text.SimpleDateFormat

import java.util.Date

import akka.actor._
import openapi.util.AkkaUtils
import openapi.{OpenApiException, Logging}
import openapi.deploy.{RegisteredApplication, RegisterApplication, ApplicationDescription}

import scala.collection.mutable.{ArrayBuffer, HashMap, HashSet}

/**
  * Created by Administrator on 2016/3/15.
  */
private[openapi] class Master(ip:String,port:Int) extends Actor with Logging{

  val DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss") // for application IDs
  var nextAppNumber = 0

  val apps = new HashSet[ApplicationInfo]
  val idToApp = new HashMap[String,ApplicationInfo]
  val actorToApp = new HashMap[ActorRef,ApplicationInfo]
  val addressToApp = new HashMap[Address,ApplicationInfo]

  val waitingApps = new ArrayBuffer[ApplicationInfo]
  val completedApps = new ArrayBuffer[ApplicationInfo]

  var firstApp : Option[ApplicationInfo] = None

  override def preStart(){
    logInfo("Starting openapi master at openapi://" + ip + ":" + port)
    //监听远程的客户端的断开连接的事件
  }


  override def receive = {
    case RegisterApplication(description) => {
      logInfo("Registering app " + description.name)
      val app = addApplication(description, sender)
      logInfo("Registered app " + description.name + " with ID " + app.id)
      waitingApps += app
      context.watch(sender)  // 远程调用前的，测试连通
      sender ! RegisteredApplication(app.id)
      //schedule()
    }
  }

  def addApplication(desc:ApplicationDescription,driver:ActorRef): ApplicationInfo = {
     val now = System.currentTimeMillis()
     val date = new Date(now)
     val app = new ApplicationInfo(now,newApplicationId(date),desc,date,driver)
     apps += app
     idToApp(app.id) = app
     actorToApp(driver) = app
     addressToApp(driver.path.address) = app
     if(firstApp == None){
       firstApp = Some(app)
     }

     app
  }

  /** 根据提交的日期生成app ID */
  def newApplicationId(submitDate:Date): String = {
    val appId = "app-%s-%04d".format(DATE_FORMAT.format(submitDate),nextAppNumber)
    nextAppNumber += 1
    appId
  }

}

private[openapi] object Master{

  private val systemName = "openapi"
  private val actorName = "Master"
  private val openapiUrlRegex = "openapi://([^:]+):([0-9]+)".r

  def main(argStrings: Array[String]) {
     val args = new MasterArguments(argStrings)
     val (actorSystem,_) = startSystemAndActor(args.ip, args.port)
     actorSystem.awaitTermination()
  }

  def toAkkaUrl(openapiUrl: String): String = {
    openapiUrl match {
      case openapiUrlRegex(host,port) =>
        "akka://%s@%s:%s/user/%s".format(systemName, host, port, actorName)
      case _ =>
         throw new OpenApiException("Invalid master URL: " + openapiUrl)
    }
  }

  def startSystemAndActor(host: String, port: Int): (ActorSystem, Int) = {
    val (actorSystem, boundPort) = AkkaUtils.createActorSystem(systemName, host, port)
    val actor = actorSystem.actorOf(Props(new Master(host, boundPort)), name = actorName)
    (actorSystem, boundPort)
  }


}
























