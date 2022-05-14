package com.teradata.openapi.master.finder.tools

import akka.pattern.ask
import akka.util.Timeout
import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.deploy._
import com.teradata.openapi.framework.message.response.FindToReqAsynYIK
import com.teradata.openapi.framework.plugin.{FtpTestPlugin, SyncTaskPlugin}
import com.teradata.openapi.framework.step.{FtpTest, Step}
import com.teradata.openapi.framework.util.DicMapFunc
import com.teradata.openapi.master.deploy.{Master, WorkerInfo, WorkerState}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

/**
  * Created by John on 2016/7/12.
  */
class FtpConnTest extends ToolApis with DicMapFunc with OpenApiLogging {

  //var fileTransferService: FileTransferService = _

  override def execute(reqID: String, master: Master, reqArgs: List[ReqArg]): FindToReqAsynYIK = {
    val ret = new FindToReqAsynYIK()

    log.debug("reqArgs:" + reqArgs)
    println("reqArgs:" + reqArgs)

    for (elem: ReqArg <- reqArgs) {
      log.debug("argName:" + elem.fieldName + " argValue:" + elem.fieldValue)
      println("argName:" + elem.fieldName + " argValue:" + elem.fieldValue)
    }

    val pushPluginType: String = (for (elem <- reqArgs if elem.fieldName.toUpperCase equals "PUSHPLUGINTYPE") yield elem.fieldValue).flatten.head.toString
    val host: String = (for (elem <- reqArgs if elem.fieldName.toUpperCase equals "HOST") yield elem.fieldValue).flatten.head.toString
    val port: Int = (for (elem <- reqArgs if elem.fieldName.toUpperCase equals "PORT") yield elem.fieldValue).flatten.head.toString.toInt
    val username: String = (for (elem <- reqArgs if elem.fieldName.toUpperCase equals "USERNAME") yield elem.fieldValue).flatten.head.toString
    val password: String = (for (elem <- reqArgs if elem.fieldName.toUpperCase equals "PASSWORD") yield elem.fieldValue).flatten.head.toString
    val ftpmode: String = (for (elem <- reqArgs if elem.fieldName.toUpperCase equals "FTPMODE") yield elem.fieldValue).flatten.head.toString
    val directory: String = (for (elem <- reqArgs if elem.fieldName.toUpperCase equals "DIRECTORY") yield elem.fieldValue).flatten.head.toString


    val ftpParams: FtpTest = FtpTest(pushPluginType, host, port, username, password, ftpmode, directory)


    val ftpTestPlugin: FtpTestPlugin = new FtpTestPlugin
    ftpTestPlugin.init(ftpParams, "", Map())
    val stp: SyncTaskPlugin[Step] = ftpTestPlugin.asInstanceOf[SyncTaskPlugin[Step]]

    val sendmsg: ExecuteToolsPlugin = ExecuteToolsPlugin(stp, reqID)
    //(1) this is one way to "ask" another actor for information
    /*implicit val timeout = Timeout(5 seconds)
    val future1 = master.resolverActor ? sendmsg
    val result = Await.result(future1, timeout.duration).asInstanceOf[String]*/

    //(2) a slightly different way to ask another actor for information

    var sending: Int = 0
    //val ec: ExecutionContextExecutor = master.context.system.dispatcher
    val workers = master.workers.filter(_.state == WorkerState.ALIVE).toIndexedSeq
    for (workerInfo: WorkerInfo <- workers if sending == 0) {
      implicit val timeout = Timeout(10 seconds)
      log.info("worker actor info:" + workerInfo.actor)
      log.info("test worker:" + workerInfo.host + " port:" + workerInfo.port + " id:" + workerInfo.id + " address:" + workerInfo.publicAddress)
      val future: Future[DeployMessage] = workerInfo.actor.ask(sendmsg).mapTo[DeployMessage]
      try {
        val result: DeployMessage = Await.result(future, 15 second)
        result match {
          case su: SyncToolsResult =>
            retMessage(ret, reqID, pushPluginType, host, port, "OK", 0, "All the workers")
          case ex: SyncToolsException =>
            sending = 1
            val workerAddr: String = workerInfo.host
            retMessage(ret, reqID, pushPluginType, host, port, "failed", 121, workerAddr)
            return ret
          case _ =>
            sending = 1
            retMessage(ret, reqID, pushPluginType, host, port, "failed", 122, "Unknown workers")
            return ret
        }
      } catch {
        case e: Exception =>
          sending = 1
          retMessage(ret, reqID, pushPluginType, host, port, "exception", 123, "Unknown workers")
          e.printStackTrace()
          return ret
      }
    }
    retMessage(ret, reqID, pushPluginType, host, port, "OK", 0, "All the workers")
    ret
  }

  def retMessage(ret: FindToReqAsynYIK, reqID: String, pushPluginType: String, host: String, port: Int, status: String, retCode: Int, localHost: String) = {
    ret.setReqID(reqID)
    ret.setRetMsg(s"$localHost $pushPluginType to $host:$port is $status.")
    ret.setRetCode(retCode.toString)
    ret.setReqStat(s"Connect Test $status.")
    ret
  }
}
