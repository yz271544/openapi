package com.teradata.openapi.master.deploy



import akka.actor.{Actor, ActorSystem, Address, Props}
import com.codahale.jerkson.Json._
import com.teradata.openapi.framework.deploy._
import com.teradata.openapi.framework.message.request.ReqToFindBody
import com.teradata.openapi.framework.{OpenApiConf, OpenApiLogging}
import com.teradata.openapi.framework.util.{ActorLogReceive, AkkaUtils, Utils}
import com.teradata.openapi.master.cacher.CacheActor
import com.teradata.openapi.master.checker.CheckActor
import com.teradata.openapi.master.controller.Controller
import com.teradata.openapi.master.deploy.service.SysArgs
import com.teradata.openapi.master.finder.FinderActor
import com.teradata.openapi.master.resolver.ResolverActor
import com.teradata.openapi.master.schedule.ScheduleActor

import scala.collection.mutable

/**
  * Created by Evan on 2016/4/1.
  */
private[openapi] class Master(
                               host: String,
                               port: Int,
                               val conf: OpenApiConf)
  extends Actor with ActorLogReceive with OpenApiLogging {
  val workers = new mutable.HashSet[WorkerInfo]
  val idToWorker = new mutable.HashMap[String, WorkerInfo]
  val addressToWorker = new mutable.HashMap[Address, WorkerInfo]

  var finderActorIns: FinderActor = _
  val finderActor = context.actorOf(Props(classOf[FinderActor], this), "FinderActor")
  var cacheActorIns: CacheActor = _
  val cacheActor = context.actorOf(Props(classOf[CacheActor], this), "CacheActor")
  var scheduleActorIns: ScheduleActor = _
  val scheduleActor = context.actorOf(Props(classOf[ScheduleActor], this), "ScheduleActor")

  val controller = context.actorOf(Props(classOf[Controller], this), "Controller")
  val resolverActor = context.actorOf(Props(classOf[ResolverActor], this), "Resolver")
  val checkActor = context.actorOf(Props(classOf[CheckActor]), "CheckActor")
  var sysArgs: Map[String, String] = SysArgs().sysArgs
  val masterUrl = "openapi://" + host + ":" + port
  //var state = RecoveryState.STANDBY
  var state = RecoveryState.ALIVE
  var persistenceEngine: PersistenceEngine = _



  override def preStart(): Unit = {
    //必要的日志记录
    logInfo("开始启动的master在 " + masterUrl)
    logInfo(s"当前运行的OPEN API version ${com.teradata.openapi.OPENAPI_VERSION}")
  }

  /**
    * ReqToFind  从Client发送到Master的取数Api
    *
    * @return
    */
  override def receiveWithLogging: Receive = {
    case x: ReqToFindBody => {
      log.info("接收到client的请求:" + x.getClientTreadId)
      //log.debug("BAOLING client~~~!")
      //println("println BAOLING client~~~!")
      log.debug("JSON context:" + x.getMessageBody)
      val mess: DeployMessage with Product = try {
        parse[ReqToFind](x.getMessageBody)
      } catch {
        case e: Exception => UnrecognizedMessage("Fatal Error:" + x.getMessageBody)
      }
      val masterTreadId = Thread.currentThread().getId
      val triggerInfo: TriggerInfo = TriggerInfo(0, "", 0)
      /*val aa: List[String] = for (elem: RepArg <- mess.asInstanceOf[ReqToFind].repArgs) yield elem.fieldName
      log.debug("Master mess RepArgs:" + aa )*/
      /*val bb: List[(String, List[Any])] = for (elem: ReqArg <- mess.asInstanceOf[ReqToFind].reqArgs) yield (elem.fieldName,elem.fieldValue)
      log.debug("Master mess ReqArgs:" + bb )*/
      finderActor ! MasterToFinder(sender, mess.asInstanceOf[ReqToFind], triggerInfo)
    }
    //刷新采集表的元数据信息转发Checker
    case WebSearchMetaTableInfo(reqId: String, sourceId: Int, schemaName: String, tabName: String) => {
      checkActor ! WebSearchMetaTableInfoToChecker(sender, reqId, sourceId, schemaName, tabName)
    }
    //
    case RefreshApiMetaInfo(reqId: String, apiId: Int, apiVersion: Int) => {
      finderActor ! RefreshApiMetaInfoToFinder(sender, reqId, apiId, apiVersion)
    }
    //Client 注册 Master
    case RegisterApplication(description) => {
      if (state == RecoveryState.STANDBY) {
        // ignore, don't send response
      } else {
        //logInfo("注册 openapi 应用 "+description.apiName)
        //拿到apiname后完成一系列的业务方法调用.....
        sender ! RegisteredApplication(description.apiName, masterUrl)
      }
    }
    //Worker 注册 Master
    case RegisterWorker(id, workerHost, workerPort, publicAddress) => {
      if (state == RecoveryState.STANDBY) {
        // ignore, don't send response
      } else if (idToWorker.contains(id)) {
        sender ! RegisterWorkerFailed("重复的worker ID")
      } else {
        val worker = new WorkerInfo(id, workerHost, workerPort, sender, publicAddress)
        if (registerWorker(worker)) {
          //持久化worker信息，未出错后的恢复使用
          //persistenceEngine.addWorker(worker)
          log.debug("regist worker: {} ok.", worker.id)
          sender ! RegisteredWorker(masterUrl)
        } else {
          val workerAddress = worker.actor.path.address
          logWarning("Worker registration failed. Attempted to re-register worker at same " +
            "address: " + workerAddress)
          sender ! RegisterWorkerFailed("Attempted to re-register worker at same address: "
            + workerAddress)
        }
      }
    }
    case m@ExecutePluginResp(taskId, result, out) =>
      log.debug("receive execute resp: {}", m)
      controller ! m

    case m@RefreshCache() => cacheActor ! m

    case ReLoad => reload()

    case _ => log.debug("receive unknown message")

  }

  override def postStop(): Unit = {

  }


  def registerWorker(worker: WorkerInfo): Boolean = {

    workers.filter { w =>
      (w.host == worker.host && w.port == worker.port) && (w.state == WorkerState.DEAD)
    }.foreach {
      w => workers -= w
    }

    val workerAddress = worker.actor.path.address
    if (addressToWorker.contains(workerAddress)) {
      val oldWorker = addressToWorker(workerAddress)
      if (oldWorker.state == WorkerState.UNKNOWN) {
        //删除old dead worker，保证新的worker可以注册进来
      } else {
        logInfo("试图重复注册worker以相同的地址：" + workerAddress)
        return false
      }
    }

    workers += worker
    idToWorker(worker.id) = worker
    addressToWorker(workerAddress) = worker
    true

  }

  //
  def removeWorker(worker: WorkerInfo): Unit = {

  }

  def reload() = {
    this.sysArgs = SysArgs().sysArgs
  }

}

private[openapi] object Master extends OpenApiLogging {

  val systemName = "openapiMaster"
  private val actorName = "Master"

  def main(argStrings: Array[String]) {

    val conf = new OpenApiConf
    val args = new MasterArguments(argStrings, conf)
    val (actorSystem, _) = startSystemActor(args.host, args.port, conf = conf)
    actorSystem.awaitTermination()

    /*println("----------------------")
    println("本机ip地址:"+InetAddress.getLocalHost)
    println("----------------------")*/
  }


  /**
    * 针对masterUrl的 `openapi://host:port` 返回 `akka.tcp://...` URL
    *
    * @param openApiUrl
    * @param protocol
    * @return
    */
  def toAkkaUrl(openApiUrl: String, protocol: String): String = {
    val (host, port) = Utils.extractHostPortFromOpenApiUrl(openApiUrl)
    AkkaUtils.address(protocol, systemName, host, port, actorName)
  }

  def startSystemActor(
                        host: String,
                        port: Int,
                        conf: OpenApiConf): (ActorSystem, Int) = {
    //创建Akka的 ActorSystem
    log.info("xxxxxxxxxxxxxxxx Master url :" + host + " port:" + port + " conf:" + conf)
    val (actorSystem: ActorSystem, boundPort: Int) = AkkaUtils.createActorSystem(systemName, host, port, conf = conf)
    log.info("xxxxxxxxxxxxxxxx ActorSystem:" + actorSystem + " boundPort:" + boundPort)
    //通过ActorSystem创建 master actor
    val actor = actorSystem.actorOf(Props(classOf[Master], host, boundPort, conf), actorName)
    (actorSystem, boundPort)
  }

}
