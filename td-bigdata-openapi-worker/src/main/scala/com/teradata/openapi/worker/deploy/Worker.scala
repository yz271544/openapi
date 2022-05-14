package com.teradata.openapi.worker.deploy

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

import akka.actor._
import com.teradata.openapi.framework.deploy._
import com.teradata.openapi.framework.plugin.SyncTaskPlugin
import com.teradata.openapi.framework.step.Step
import com.teradata.openapi.framework.util.{ActorLogReceive, AkkaUtils, Utils}
import com.teradata.openapi.framework.{OpenApiConf, OpenApiLogging}
import com.teradata.openapi.master.deploy.Master
import com.teradata.openapi.worker.ExecutorActor

/**
  * Created by Evan on 2016/4/18.
  */
private[openapi] class Worker(host:String,
                              port:Int,
                              masterAkkaUrls:Array[String],
                              actorSystemName:String,
                              actorName:String,
                              workDirPath:String = null,
                              val conf:OpenApiConf)
  extends Actor with ActorLogReceive with OpenApiLogging{

  Utils.checkHost(host,"Expected hostname")
  assert(port > 0)

  def createDateFormat = new SimpleDateFormat("yyyyMMddHHmmss")

  //每(heartbeat timeout) / 4 milliseconds 发送心跳
  val HEARTBEAT_MILLIS = conf.getLong("openapi.worker.timeout",60) * 1000 / 4

  var master:ActorSelection = null
  var masterAddress:Address = null
  var activeMasterUrl: String = ""
  val akkaUrl = AkkaUtils.address(AkkaUtils.protocol(context.system),
    actorSystemName,host,port,actorName)

  val publicAddress = {
    host
  }

  @volatile var registered = false
  @volatile var connected = false

  val workDir: File = null
  val workerId = generateWorkerId()

  var registrationRetryTimer: Option[Cancellable] = None

  val executorActor = context.actorOf(Props(classOf[ExecutorActor], this))

  override def preStart(): Unit ={

    logInfo(s" 运行的open api 版本 ${com.teradata.openapi.OPENAPI_VERSION}")
    registerWithMaster()

  }

  override def receiveWithLogging: Receive = {
    case RegisteredWorker(masterUrl) =>
      logInfo("成功注册的master地址是：" + masterUrl)
      registered = true
      this.activeMasterUrl = masterUrl

    case RegisterWorkerFailed(message) =>
      if (!registered) {
        logError("Worker registration failed: " + message)
        System.exit(1)
      }
    case m@ExecuteAsyncPlugin(taskId, plugin) =>
      log.info("Receive execute plugin {}, task {}", plugin, taskId)
      executorActor ! m
      sender() ! ExecutePluginAck(taskId)

    case m@ExecuteSyncPlugin(taskId, plugin, reqID, formatFinger, client) =>
      log.info("Receive execute plugin {}, task {}", plugin, taskId)
      executorActor ! m
      sender() ! ExecutePluginAck(taskId)
    case ExecuteToolsPlugin(plugin, reqID) =>
      executeTools(plugin, reqID, sender())
    case _ =>
      log.error("Don't know message!")
  }

  //整个Worker 向 Master 注册过程的封装
  def registerWithMaster(){

    registrationRetryTimer match{
      case None =>
        registered = false
        //向Master注册
        tryRegisterAllMasters()

      case Some(_) =>
        logInfo("已经注册了，不能重复注册！")
    }

  }

  private def tryRegisterAllMasters(){
    for(masterAkkaUrl <- masterAkkaUrls){
      logInfo("连接到master " + masterAkkaUrl + "...")
      val actor = context.actorSelection(masterAkkaUrl)
      actor ! RegisterWorker(workerId,host,port,publicAddress)
    }
  }

  def generateWorkerId():String = {
    "worker-%s-%s-%d".format(createDateFormat.format(new Date), host, port)
  }

  def activeMasterActor  = {
    context.actorSelection(Master.toAkkaUrl(this.activeMasterUrl, "akka.tcp"))
  }


  def executeTools(plugin: SyncTaskPlugin[Step], reqID: String, sender: ActorRef): Unit = {
    log.debug("start execute tools from {}", plugin)
    try {
      val ret = plugin.execute()
      log.debug("sync exec tools complete, ret {}", ret)
      sender ! SyncToolsResult(reqID, ret)
    } catch {
      case e: Exception =>
        log.error("sync exec tools error:", e)
        e.fillInStackTrace()
        sender ! SyncToolsException(reqID,  e.getMessage)
    }

  }
}

private[openapi] object Worker extends OpenApiLogging {

  def main(argStrings: Array[String]) {

    //创建Openapi配置对象
    val conf = new OpenApiConf
    //创建worker启动所需要的参数对象,WorkerArguments暂时没有用到conf的数据信息，为以后扩展做准备
    //var argStrings = Array("openapi://"+Utils.localHostName()+":8888")
    //argStrings(0) = "openapi://Evan-PC:8888"
    for(arg <- argStrings){
      println("接收到的执行参数："+arg)
    }
    val args = new WorkerArguments(argStrings,conf)
    //创建worker的actorSystem
    //创建worker actor
    val (actorSystem,_) = startSystemAndActor(args.host,args.port,args.masters,args.workDir)
    actorSystem.awaitTermination()

  }

  def startSystemAndActor(host:String,
                          port:Int,
                          masterUrls:Array[String],
                          workDir:String,
                          workerNumber:Option[Int] = None,
                          conf:OpenApiConf = new OpenApiConf):(ActorSystem,Int) ={

     val systemName = "openapiWorker" + workerNumber.map(_.toString).getOrElse("")
     val actorName = "Worker"
     val (actorSystem,boundPort) = AkkaUtils.createActorSystem(systemName,host,port,conf)
     val masterAkkaUrls = masterUrls.map(Master.toAkkaUrl(_,AkkaUtils.protocol(actorSystem)))
     actorSystem.actorOf(Props(classOf[Worker],host,boundPort,masterAkkaUrls,systemName,actorName,workDir,conf),name = actorName)
     (actorSystem, boundPort)
  }
}