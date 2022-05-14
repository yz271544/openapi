package com.teradata.openapi.master.resolver

import akka.actor.Actor
import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.deploy.FindToExp
import com.teradata.openapi.framework.deploy._
import com.teradata.openapi.framework.model.SourceInfoRow
import com.teradata.openapi.framework.step._
import com.teradata.openapi.master.controller.Message.DAGExecutePlan
import com.teradata.openapi.master.deploy.Master
import com.teradata.openapi.master.resolver.dao.SourceInfoDao

import scala.collection.mutable

/**
  * Created by lzf on 2016/4/7.
  */
class ResolverActor(master: Master) extends Actor with OpenApiLogging {

  val controller = master.controller
  var sourceInfo:scala.collection.mutable.Map[Int, SourceInfoRow] = _

  log.debug("init resolver actor ok...")

  def reload(): Unit = {
    log.debug("load args")
    sourceInfo = (new SourceInfoDao).loadSourceInfoMap
  }

  override def preStart(): Unit = {
    reload()
  }

  override def receive: Receive = {

    case cache@CacheToExpAlone(cacheTableInfo) =>
      val dag = new DAG(cache)
      dag.createNode(CacheStep(sourceInfo(cacheTableInfo.sourceID), cacheTableInfo))
      controller ! DAGExecutePlan(dag)

    case clean@CacheToExpClean(cleanTables) =>
      val dag = new DAG(clean)
      dag.createNode(CleanStep(cleanTables))
      controller ! DAGExecutePlan(dag)

    case c@CleanDataFingerCache(cleanDataFingerList) =>
      val dag = new DAG(c)
      val finger = cleanDataFingerList.map(_.dataFinger)
      dag.createNode(CleanFileStep(finger))
      controller ! DAGExecutePlan(dag)

    case c@CleanFormatFingerCache(cleanFormatFingerList) =>
      val dag = new DAG(c)
      val finger = cleanFormatFingerList.map(_.formatFinger)
      dag.createNode(CleanFileStep(finger))
      controller ! DAGExecutePlan(dag)

    case m@FindToExp(reqID, apiId, api_version, apiName, format, encode, priority, reqArgs,
    repArgs, pushArgs, location, retFormatFinger, retDataFinger, isSyn, client, pageSize, pageNum, trigger_sorc) =>
      log.info("{}|接收到finder消息FindToExp.", reqID)
      //log.info("receive msg: {}", m)

      //isSyn 0:同步 1:异步 2:订阅
      val dag: DAG =

        location match {
          case Some(x) =>
            x match {

              case RetLocation(list) =>

                if (isSyn == 0) {
                  if (list.length == 1) {
                    syncPlan(m, list.head)
                  }
                  else
                    throw new Exception("sync request only support one source.")
                }
                else {
                  sourcePlan(m, list)
                }

              case RetDataFinger(finger, loc, _) => dataFingerPlan(m)
              case RetFormatFinger(finger, loc) => formatFingerPlan(m)

            }
          case None => throw new Exception("location is none.")
        }

      if(dag !=null) controller ! DAGExecutePlan(dag)

    case ReLoad => reload()

  }

  //同步
  private def syncPlan(find: FindToExp, tableInfo: TableInfo): DAG = {

    val dag = new DAG(find)
    val source = sourceInfo(tableInfo.sourceID)
    dag.createNode(SyncStep(find.reqID, source, tableInfo, find.reqArgs, find.repArgs,
      find.pushArgs, find.retFormatFinger, find.format.getOrElse(Format(formType = "JSON", formDetail = None)),
      find.encode, find.client, find.pageSize, find.pageNum))
    dag
  }

  //探查到不带格式指纹 格式化、推送(订阅)
  private def dataFingerPlan(find: FindToExp): DAG = {

    val dag = new DAG(find)
    val s1 = dag.createNode(FormatStep(find.format.getOrElse(Format(formType = "JSON", formDetail = None)), find.encode,
      find.repArgs, find.retDataFinger, find.retFormatFinger))

    if(find.isSyn==2) {
      val s2 = dag.createNode(PushStep(find.retFormatFinger,find.format, find.pushArgs))
      dag.addLink(s1, s2)
    }
    dag
  }

  //探查到带格式的数据指纹仅推送(订阅)
  private def formatFingerPlan(find: FindToExp): DAG = {

    if(find.isSyn==2) {
      val dag = new DAG(find)
      val push = PushStep(find.retFormatFinger,find.format, find.pushArgs)
      dag.createNode(push)
      dag
    }
    else
      null

  }

  //探查到数据源徐判断是否要缓存再看有几个数据源取数、格式、推送(订阅)
  private def sourcePlan(find: FindToExp, infos: List[TableInfo]): DAG = {

    val dag = new DAG(find)
    val nodes = mutable.Set[Node]()

    infos.foreach(info => {
      val node =
        if (info.isCach) {
          val cStep = CacheStep(sourceInfo(info.sourceID), info)

          val cache = dag.createNode(cStep)
          val fStep = FetchStep(sourceInfo(info.sourceID), info, find.reqArgs,
            find.repArgs, find.retDataFinger)

          val fetch = dag.createNode(fStep)
          dag.addLink(cache, fetch)
          fetch
        }
        else {
          val fStep = FetchStep(sourceInfo(info.sourceID), info, find.reqArgs,
            find.repArgs, find.retDataFinger)

          dag.createNode(fStep)
        }
      nodes += node

    })

    val format = dag.createNode(FormatStep(find.format.getOrElse(Format(formType = "JSON", formDetail = None)), find.encode, find.repArgs,
      find.retDataFinger, find.retFormatFinger))
    nodes.foreach(n => {
      dag.addLink(n, format)
    })

    if(find.isSyn==2) {
      val push = dag.createNode(PushStep(find.retFormatFinger,find.format, find.pushArgs))
      dag.addLink(format, push)
    }
    dag

  }

}
