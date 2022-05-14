package com.teradata.openapi.master.controller

import akka.actor.Actor
import akka.util.Timeout
import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.deploy._
import com.teradata.openapi.framework.model.PluginInfoRow
import com.teradata.openapi.framework.plugin.{AsyncTaskPlugin, SyncTaskPlugin, TaskPlugin}
import com.teradata.openapi.framework.step.{SyncStep, _}
import com.teradata.openapi.master.controller.Message.{SubmitTask, TaskSubmitException}
import com.teradata.openapi.master.controller.dao.PluginInfoDao
import com.teradata.openapi.master.deploy.WorkerState

import scala.concurrent.duration._
import scala.util.Random

/**
  * Created by lzf on 2016/4/12.
  */
class SubmitActor(controller:Controller) extends Actor with OpenApiLogging {

  implicit val execution = context.system.dispatcher
  implicit val timeout = Timeout(60 seconds)

  var pluginInfo: Seq[PluginInfoRow] = _

  def reload(): Unit = {
    log.debug("load args")
    pluginInfo = (new PluginInfoDao).loadPluginInfo
  }

  override def preStart(): Unit ={
    reload()
  }

  override def receive: Receive = {

    case SubmitTask(task: Task) =>

      task.status = TaskState.RUN
      task.getDagNodes.foreach(it => {
        if(it._1.step == task.step) {
          val node = it._1
          val dag = it._2

          dag.source match {
            case find:FindToExp => log.info("{}|提交任务消息 {}", find.reqID, node.id)
            case _ =>
          }
        }
      })

      log.debug("Submit task: {} step: {}", task.id , task.step  )

      try{

        val pluginInfoRow = findPlugin(task.step)
        val clazzName = pluginInfoRow.class_name
        val taskPlugin: TaskPlugin[Step] = Class.forName(clazzName).
          newInstance().asInstanceOf[TaskPlugin[Step]]

        log.debug("init plugin....{}", clazzName)
        val env = Map[String,Any](task.env.toSeq: _*)

        taskPlugin.init(task.step, pluginInfoRow.template, env)
        submit(task, taskPlugin)

      }catch {
        case e:Exception =>
          controller.self ! TaskSubmitException(task.id, e)
      }

      /*
    case m@ExecutePluginResp(taskId, result, out) =>
      log.debug("receive execute resp: {}", m)
      controller.self ! m
      */

    case ExecutePluginAck(taskId) =>
      log.debug("receive task {} execute ack.", taskId)
      val taskOpt = controller.task(taskId)
      if(taskOpt.nonEmpty) {
        taskOpt.get.acked = true
        val task = taskOpt.get

        task.getDagNodes.foreach(it=> {
          if(it._1.step == task.step) {
            val node = it._1
            val dag = it._2

            dag.source match {
              case find:FindToExp => log.info("{}|接收到Worker 执行确认消息, 步骤序号 {}", find.reqID, node.id)
              case _ =>
            }
          }
        })

      }

    case ReLoad => reload()

  }

  //获取数据源类型 01 TD 02 Aster 03 HIVE
  private def getSourceType(sourceId:Int): String ={
    val opt = controller.sourceInfo.get(sourceId)
    opt match {
      case Some(x) => x.source_type_code
      case None => ""
    }
  }

  private def findPlugin(step: Step): PluginInfoRow = {

    var sourceType = ""
    var visitMethd = 1

    val pluginType =
    step match {
      case syn: SyncStep =>
        sourceType = syn.source.source_type_code
        visitMethd = 0
        PluginType.FETCH

      case cache: CacheStep =>
        sourceType = getSourceType(cache.tableInfo.sourceID)
        PluginType.CACHE
      case fetch: FetchStep =>
        sourceType = getSourceType(fetch.tableInfo.sourceID)
        PluginType.FETCH

      case format: FormatStep => PluginType.FORMAT
      case push: PushStep => PluginType.PUSH
      case clean: CleanStep => PluginType.CLEAN
      case cleanF: CleanFileStep => PluginType.CLEAN_FILE
      case _ => throw new Exception("unknown step")

    }

    log.debug("plugin type:{} source {}", pluginType.id, sourceType)
    val pluginOpt = pluginInfo.find(p=> {
      (p.source_type_code ==sourceType || sourceType=="") &&
        p.api_visit_methd ==visitMethd  && p.plugin_type == pluginType.id
    })

    if(pluginOpt.isEmpty) throw new RuntimeException("Can't match plugin.")
    pluginOpt.get

  }

  private def submit(task: Task, plugin: TaskPlugin[Step]): Unit = {

    //log.debug("send task to worker....")

    log.debug("all registed workers : {}", controller.master.workers)
    val workers = controller.master.workers.filter(_.state==WorkerState.ALIVE).toIndexedSeq
    if(workers.isEmpty)
      throw new RuntimeException("no worker")

    val worker = workers(new Random().nextInt(workers.size))
    log.debug("selected worker info: {}", worker)

    val executeMsg =
    plugin match {
      case syn:SyncTaskPlugin[Step] =>
        val step = task.step.asInstanceOf[SyncStep]
        ExecuteSyncPlugin(task.id, syn, step.reqID, step.formatFinger, step.clientPath)
      case asyn:AsyncTaskPlugin[Step] =>
        ExecuteAsyncPlugin(task.id, asyn)

    }

    worker.actor ! executeMsg
    task.submitTime = System.currentTimeMillis()

    /*
    log.debug("start send sync exec msg, task {}", task.id)
    try{
      val future = workActor ask executeMsg
      Await.result(future, 1 minute)
      log.debug("rcv send sync exec msg ack, task {}", task.id)
    }catch{
      case e:Exception => e.printStackTrace()

    }
    */

  }

}
