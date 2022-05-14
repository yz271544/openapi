package com.teradata.openapi.worker

import java.util.concurrent.Executors

import akka.actor.{Actor, ActorRef}
import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.deploy._
import com.teradata.openapi.framework.plugin.{AsyncTaskPlugin, SyncTaskPlugin}
import com.teradata.openapi.framework.step.Step
import com.teradata.openapi.worker.deploy.Worker


/**
  * Created by lzf on 2016/4/14.
  */
class ExecutorActor(val worker: Worker) extends Actor with OpenApiLogging {

  private val asyncExec = Executors.newFixedThreadPool(100)
  private val syncExec = Executors.newFixedThreadPool(200)

  override def receive: Receive = {
    case ExecuteSyncPlugin(id, plugin, reqID, formatFinger, client) =>
      executeSync(id, plugin, reqID, formatFinger, client)

    case ExecuteAsyncPlugin(id, plugin) =>
      executeAsync(id, plugin)
  }

  override def postStop: Unit = {
    this.asyncExec.shutdown()
    this.syncExec.shutdown()
  }

  def executeAsync(id: Int, plugin: AsyncTaskPlugin[Step]): Unit = {

    log.debug("start execute task : {}", plugin)

    asyncExec.submit(new Runnable {
      override def run(): Unit = {

        try {
          val ret = plugin.execute()
          log.debug("async exec complete, ret {}", ret)
          //ref ! ExecutePluginResp(id, ret, plugin.out)
          worker.activeMasterActor ! ExecutePluginResp(id, ret, plugin.out)
        } catch {
          case e: Exception => e.printStackTrace()
            log.debug("async exec error:", e)
            //ref ! ExecutePluginResp(id, -1, plugin.out)
            worker.activeMasterActor ! ExecutePluginResp(id, -1, plugin.out)
        }

      }
    })

  }

  def executeSync(id: Int, plugin: SyncTaskPlugin[Step], reqID: String, formatFinger: String, client: String): Unit = {

    log.debug("start execute task from {}", client)

    syncExec.submit(new Runnable {
      override def run(): Unit = {

        try {
          val ret = plugin.execute()
          log.debug("sync exec complete, ret length {}", ret.length)
          context.system.actorSelection(client) ! SyncExecuteResult(reqID, ret, formatFinger)
        } catch {
          case e: Exception => e.printStackTrace()
            log.debug("sync exec error:", e)
            context.system.actorSelection(client) ! SyncExecuteException(reqID, e)
        } finally {
          worker.activeMasterActor ! ExecutePluginResp(id, 0, plugin.out)
        }

      }
    })

  }



}
