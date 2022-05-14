package com.teradata.openapi.master.controller

import java.util.concurrent.atomic.AtomicInteger

import akka.actor.{Actor, ActorRef, Props}
import com.teradata.openapi.master.resolver.{DAG, Node}
import com.teradata.openapi.master.controller.Message._
import com.teradata.openapi.framework.step._
import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.deploy._
import com.teradata.openapi.framework.model.{SourceInfoRow, StrategyCodeRow}
import com.teradata.openapi.master.controller.dao.StrategyCodeDao
import com.teradata.openapi.master.deploy.Master
import com.teradata.openapi.master.resolver.dao.SourceInfoDao

import scala.collection.mutable
import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Created by lzf on 2016/4/7.
  */
class Controller(val master: Master) extends Actor with OpenApiLogging{

  private val nextTaskId = new AtomicInteger(0)
  private val idToTask = mutable.HashMap[Int,Task]()
  private val stepToTask = mutable.HashMap[Step,Task]()
  //private val reqToDAG = new java.util.concurrent.ConcurrentHashMap[String,DAG]

  private val submitter: ActorRef = context.actorOf(Props(classOf[SubmitActor], this),name = "Submitter")

  private val formatQueue = new CountStrategy
  private val pushQueue = new CountStrategy
  private val cleanQueue = new CountStrategy

  private val syncPendActors = mutable.Map[Int, (ActorRef, Strategy)]()
  private val asyncPendActors = mutable.Map[Int, (ActorRef, Strategy)]()

  private val formatPendActor = context.actorOf(Props(classOf[PendActor], this, formatQueue, submitter),name = "FormatPendActor")
  private val pushPendActor = context.actorOf(Props(classOf[PendActor], this, pushQueue, submitter),name = "PushPendActor")
  private val cleanPendActor = context.actorOf(Props(classOf[PendActor], this, cleanQueue, submitter),name = "CleanPendActor")

  var sourceInfo :scala.collection.mutable.Map[Int, SourceInfoRow]= _
  var strategyMap:scala.collection.mutable.Map[Int, StrategyCodeRow] = _

  implicit val execution = context.system.dispatcher
  //log.debug("init controller actor ok, plugin info: {}", pluginInfo)

  def task(taskId:Int): Option[Task] = {
    idToTask.get(taskId)
  }

  def getActor(step:Step): ActorRef = {

    step match {
      case syn: SyncStep =>
        fetchActor(syncPendActors.get(syn.tableInfo.sourceID), syn)
      case cache: CacheStep =>
        fetchActor(asyncPendActors.get(cache.tableInfo.sourceID), cache)
      case fetch: FetchStep =>
        fetchActor(asyncPendActors.get(fetch.tableInfo.sourceID), fetch)
      case format: FormatStep =>
        formatPendActor
      case push: PushStep =>
        pushPendActor
      case clean: CleanStep =>
        cleanPendActor
      case cleanFile:CleanFileStep =>
        cleanPendActor
      case _ => throw new RuntimeException(s"can't get pend actor, step: $step  type unknown")
    }

  }

  def fetchActor(option: Option[(ActorRef, Strategy)], step:Step): ActorRef = {
    if(option.isEmpty) throw new RuntimeException(s"can't get pend actor step: $step")
    option.get._1
  }

  def reload(): Unit ={

    log.debug("load args")
    sourceInfo = (new SourceInfoDao).loadSourceInfoMap
    strategyMap = (new StrategyCodeDao).loadStrategyMap

    formatQueue.loadProperty("""[{"moment":"* * * * *", "maxCount":10}]""")
    pushQueue.loadProperty("""[{"moment":"* * * * *", "maxCount":10}]""")
    cleanQueue.loadProperty("""[{"moment":"* * * * *", "maxCount":10}]""")

    sourceInfo.foreach(source=> {
      loadQueue(source._2, true)
      loadQueue(source._2, false)
    })

  }

  def loadQueue(sourceInfo: SourceInfoRow, isSync:Boolean): Unit ={

    val id = sourceInfo.source_id
    val strategyId = if(isSync) sourceInfo.sync_strategy_id else sourceInfo.asyn_strategy_id
    val opt = strategyMap.get(strategyId)
    val queueMap = if(isSync) this.syncPendActors else this.asyncPendActors
    val actorName = (if(isSync) "SyncPendActor-" else "AsyncPendActor-") + id

    if(opt.nonEmpty) {
      val strategyArg = opt.get.strategy_arg
      if(queueMap.contains(id)) {
        queueMap(id)._2.loadProperty(strategyArg)
      }
      else {
        val strategy = new CountStrategy
        strategy.loadProperty(strategyArg)
        val actor = context.actorOf(Props(classOf[PendActor], this, strategy, submitter),name = actorName)
        queueMap += id-> (actor, strategy)
      }
    }
    else {
      log.error("unknown strategy {} source {}",strategyId ,id)
    }

  }

  override def preStart(): Unit ={

    reload()
  }

  override def postStop(): Unit ={

  }

  override def receive: Receive = {

    case DAGExecutePlan(dag) =>
      dag.source match {
        case find:FindToExp =>
          log.info("{}|接收到 Resolver的DAG.", find.reqID)
        case _ =>
      }

      dagExecutePlan(dag)

    case ExecutePluginResp(id, exitCode, out) =>
      execResp(id, exitCode, out)

    case TaskSubmitException(id, e) =>
      submitException(id, e)

    case ReLoad =>
      submitter ! ReLoad
      reload()

  }

  private def dagExecutePlan(dag: DAG): Unit = {

    log.debug("Invoke dag:{}", dag.explain())
    dag.init()  //before get remain nodes, must init dag once
    dag.getRemainNodes.foreach(node=> {
      submit(dag, node, mutable.Map[String,Any]())
    })
    //reqToDAG.put(dag.find.reqID, dag)

  }

  //same step ,only create one task. put it to map which other step to use
  private def submit(dag:DAG, node:Node, env: mutable.Map[String,Any]): Unit = {

    val step = node.step
    val priority = dag.source match {
      case x:FindToExp =>
        if(x.triggerInfo.nonEmpty)
          env += "TriggerInfo"->x.triggerInfo.get
        x.priority
      case _ => 0
    }

    val task = stepToTask.getOrElse(step,
      new Task(this.nextTaskId.incrementAndGet(), step, priority))

    task.addDagNode(dag, node)
    if(!stepToTask.contains(step)) {
      if(env !=null) task.env ++= env
      stepToTask += step -> task
      this.idToTask += task.id -> task
      getActor(step) ! DispatchTask(task)
      log.debug("dag {} create task {} ",dag, task.id )
    }
    else {
      log.debug("dag {} use task {} ",dag, task.id )
      if(task.status == TaskState.COMPLETE) {
        dag.removeRemainNode(node)
        if(dag.isOver) complete(dag)
        else {
          dag.getRemainNodes.foreach(n => {
            submit(dag, n, task.out)
          })
        }
      }
    }

  }

  //request complete, some to clean
  private def complete(dag:DAG): Unit = {

    log.info("dag {} complete", dag)
    //reqToDAG.remove(dag.find.reqID)

    cleanTask(dag)
    dag.source match  {
      case find: FindToExp =>

        log.info("{}|接收到请求处理结束", find.reqID)
        master.finderActor ! ReqStatUpdExpToFind(find.reqID, 0)
      case _ =>
    }

  }

  private def cleanTask(dag:DAG): Unit ={

    //task all owned dag complete, remove task
    dag.getNodes.foreach(node=> {
      val opt = stepToTask.get(node.step)
      if(opt.nonEmpty) {
        val task = opt.get
        task.removeDagNode(node, dag)
        log.debug("remove task dag node {}", node.id)

        if(!task.getDagNodes.exists(!_._2.isOver)) {
          log.debug("remove task {}", task.id)
          stepToTask.remove(node.step)
          idToTask.remove(task.id)
        }
      }
    })
  }

  private def submitException(id:Int, e:Exception): Unit = {

    log.debug("submit task: {} exception" ,id, e)
    execResp(id, -1, null)
  }

  //process task response
  private def execResp(id: Int, exitCode: Int, out:mutable.Map[String,Any]): Unit = {

    log.debug("task : {} resp exit code: {}" ,id, exitCode)
    if(!idToTask.contains(id)) {
      return
    }
    val task = idToTask(id)
    if(out!=null) task.out ++= out
    getActor(task.step) ! DispatchClear(task)

    if(exitCode==0) {
      task.status = TaskState.COMPLETE

      task.step match {
        case CacheStep(_,tab) =>
          //update cache
          val namePart = tab.tableFullName.split('.')
          master.cacheActor ! ExpCacheActionFinish(CacheTable(namePart(0), namePart(1), tab.columnMap, tab.sourceID, "ADD"))

        case f:FetchStep =>
          //update data finger
          val fileRawCode = out.getOrElse("fileRawCode","")
          val fileRawLoc = out.getOrElse("fileRawLoc","")
          val treadId = Thread.currentThread().getId
          master.finderActor ! UpdDataFinger(treadId, f.retDataFinger, fileRawLoc.toString, fileRawCode.toString, 1)

        case f:FormatStep =>
          //update format finger
          val filePackLoc = out.getOrElse("filePackLoc","")
          val treadId = Thread.currentThread().getId
          master.finderActor ! UpdFormatFinger(treadId, f.retDataFinger, f.retFormatFinger, filePackLoc.toString, 1)

        case p:PushStep =>

        case CleanStep(tabs) =>
          tabs.foreach(tab => {
            master.finderActor ! ExpCacheActionFinish(CacheTable(tab.schemaName, tab.tableName, tab.columnMap, tab.sourceID, "DROP"))
          })

        case CleanFileStep(finger) =>
        case _ =>

      }

      task.getDagNodes.foreach(it => {
        val (node, dag) = it
        dag.removeRemainNode(node)
        if(dag.isOver) complete(dag)
        else
        dag.getRemainNodes.foreach(n => {
          submit(dag, n, out)
        })
      })

    }
    //when task error ,if try count not reached max, try it again
    else if(task.tryCount < 3) {

      log.info("task {} error, try again ", task.id)
      context.system.scheduler.scheduleOnce(30 seconds, new Runnable {
        override def run(): Unit = {
          getActor(task.step) ! DispatchTask(task)
          task.tryCount += 1
        }
      })
    }
    else { //task error process

      log.error("task {} error", task.id)
      stepToTask.remove(task.step)
      idToTask.remove(task.id)

      task.getDagNodes.foreach(it => {
        val (node, dag) = it
        log.error("dag {} error, step {}", dag, node.id)

        cleanTask(dag)
        dag.source match  {
          case find: FindToExp =>

            log.info("{}|接收到请求处理失败", find.reqID)

            master.finderActor ! ReqStatUpdExpToFind(find.reqID, 1)
          case _ =>
        }

      })

      //this.stepToTask.remove(task.step)
      //this.idToTask.remove(id)
    }

  }

}
