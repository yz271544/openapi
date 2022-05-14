package com.teradata.openapi.master.cacher

import akka.actor.Actor
import com.codahale.jerkson.Json
import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.deploy._
import com.teradata.openapi.framework.util.DicMapFunc
import com.teradata.openapi.master.cacher.service.DataSnpstCacheInfo
import com.teradata.openapi.master.deploy.Master

import scala.collection.immutable.TreeMap
import scala.concurrent.duration._

/**
  * Created by John on 2016/7/4.
  */
class CacheActor(val master: Master) extends Actor with DicMapFunc with OpenApiLogging {
  master.cacheActorIns = this
  var cacheDataSnpstInfo: DataSnpstCacheInfo = DataSnpstCacheInfo()
  log.info("CacheActor [cacheDataSnpstInfo] Ref:" + cacheDataSnpstInfo)
  val cacheTreadId = Thread.currentThread().getId
  implicit val execution = context.system.dispatcher
  context.system.scheduler.schedule(65 seconds, 23 hours, master.cacheActor, CleanCacheByScheduler())
  //context.system.scheduler.schedule(6 seconds, 23 hours, master.cacheActor, CleanFingerCache())
  //context.system.scheduler.schedule(1 seconds,1 seconds,master.cacheActor,ClockTimeStamp(System.currentTimeMillis()))



  override def receive: Receive = {
    case ClockTimeStamp(timeMills: Long) => {
      log.info("当前时间是:" + timeMills.toString)
    }
    /*
    接收Logger的缓存列表刷新消息
     */
    case RefreshCache() => {
      log.info("接收到RefreshCache()刷新缓存表清单消息")
      cacheDataSnpstInfo.refreshTabCacheList()
      //找出哪些表需要清出缓存
      //val outCacheList: List[String] = cacheDataSnpstInfo.findOutCacheByList()
    }
    /*
    根据策略搜索哪些表的快照需要清出缓存
     */
    case CleanCacheByScheduler() => {
      log.info("接收到CleanCacheByScheduler()根据策略搜索哪些表的快照需要清出缓存")
      val outCacheList: List[CacheTable] = cacheDataSnpstInfo.findOutCacheByStrategy()
      master.resolverActor ! CacheToExpClean(outCacheList)
    }

    /** 接收到Finder发过来的CycRcd更新消息，判断是否需要将该表该周期快照进行缓存
     * 1.判断表是否应该加入缓存
     * 2.发消息给resolver进行数据缓存
     */
    case FindCycRcdToCache(schemaName: String, tableName: String, jsonCondtionMap: TreeMap[String, List[Any]], sourceid: Int) => {
      //如果需要进入缓存，则写入cacheDataSnpstInfo这个Map中，并设置状态为0
      log.info("接收到FindCycRcdToCache，将表[" + schemaName + "." + tableName + "]的快照:" + jsonCondtionMap + "加入缓存.")
      if (cacheDataSnpstInfo.findInCache(schemaName + "." + tableName, jsonCondtionMap)) {
        master.resolverActor ! CacheToExpAlone(TableInfo(schemaName + "." + tableName, jsonCondtionMap, sourceid, isCach = true))
      }
    }
    /**接收Explainer 返回缓存计算结果
     * 1.更新缓存Map和数据库 ADD加入缓存：0 -> 1 ， DROP清出缓存：2 -> 3
     * 2.通知Finder更新缓存进入Dic
     */
    case ExpCacheActionFinish(cacheTableInfo: CacheTable) => {
      log.info("接收到ExpCacheActionFinish，缓存表:[" + cacheTableInfo.schemaName + "." + cacheTableInfo.tableName + "]已经" + cacheTableInfo.operateType + ",正在更新DIC")
      val propMap: TreeMap[String, List[Any]] = mapToTreeMapVallst(cacheTableInfo.columnMap)
      if (cacheTableInfo.operateType.toUpperCase equals "ADD")
        cacheDataSnpstInfo.updateCacheToHold(cacheTableInfo.schemaName, cacheTableInfo.tableName, propMap)
      else if (cacheTableInfo.operateType.toUpperCase equals "DROP")
        cacheDataSnpstInfo.updateCacheHaveOuted(cacheTableInfo.schemaName, cacheTableInfo.tableName, propMap)
      else log.info("接收到错误的ExpCacheActionFinish消息体:" + cacheTableInfo.operateType.toString)
      val finderElement: String = Json.generate(propMap)
      val uuid = java.util.UUID.randomUUID.toString.toUpperCase
      master.finderActor ! UpdCycleRcdToFinder(uuid, sender, UpdCycleRcd(0, cacheTableInfo.schemaName, cacheTableInfo.tableName, finderElement, cacheTableInfo.operateType.toUpperCase))
    }
    case ReLoad => reload()
  }

  def reload() = {
    this.cacheDataSnpstInfo = DataSnpstCacheInfo()
  }
}
