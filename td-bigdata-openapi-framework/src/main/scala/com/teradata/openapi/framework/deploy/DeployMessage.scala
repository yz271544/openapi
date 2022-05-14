package com.teradata.openapi.framework.deploy

import akka.actor.ActorRef
import com.teradata.openapi.framework.plugin.{AsyncTaskPlugin, SyncTaskPlugin}
import com.teradata.openapi.framework.step.Step
import com.teradata.openapi.framework.util.Utils
import com.teradata.openapi.framework.{ApplicationDescription, OpenApiLogging}

import scala.collection.immutable.TreeMap
import scala.collection.mutable

/**
  * Created by Administrator on 2016/4/1.
  */
sealed trait DeployMessage extends Serializable with OpenApiLogging


object DeployMessage {}

//系统消息
case class UnrecognizedMessage(mess: Any) extends DeployMessage {
  log.error("Unrecognized Message:" + mess.toString)
}

//刷新订阅定时触发Seq
case class refreshTimeTriggerRss()

//时间戳消息
case class ClockTimeStamp(timeMills: Long)

// AppClient to Master
case class RegisterApplication(appDescription: ApplicationDescription) extends DeployMessage

// Master to AppClient
case class RegisteredApplication(apiName: String, masterUrl: String) extends DeployMessage

//Worker to Master
case class RegisterWorker(id: String, host: String, port: Int, publicAddress: String) extends DeployMessage {
  Utils.checkHost(host, "必须要有hostname")
  assert(port > 0)
}

case class ExecutePluginAck(taskId: Int) extends DeployMessage

case class ExecutePluginResp(taskId: Int, result: Int, out: mutable.Map[String, Any]) extends DeployMessage


//Master to Worker

case class RegisteredWorker(masterUrl: String) extends DeployMessage

case class RegisterWorkerFailed(message: String) extends DeployMessage

case class ExecuteAsyncPlugin(taskId: Int, plugin: AsyncTaskPlugin[Step]) extends DeployMessage

case class ExecuteSyncPlugin(taskId: Int, plugin: SyncTaskPlugin[Step], reqID: String, formatFinger: String, client: String) extends DeployMessage

case class ExecuteToolsPlugin(plugin: SyncTaskPlugin[Step],reqID: String) extends DeployMessage

case class TriggerInfo(trigger_methd: Int = 0, trigger_sorc: String = "", rss_id: Int = 0) extends DeployMessage

case class MasterToFinder(sender: ActorRef, reqtofind: DeployMessage, triggerInfo: TriggerInfo) extends DeployMessage

case class UpdCycleRcdToFinder(reqID:String, sender: ActorRef, cachetofind: UpdCycleRcd) extends DeployMessage

case class RefreshCache() extends DeployMessage

case class CleanCacheByScheduler() extends DeployMessage

case class CleanFingerCache() extends DeployMessage

//Finder to Explainer
case class FindToExp(reqID: String,
                     apiId: Int, //finger
                     api_version: Int, //finger
                     apiName: String,
                     format: Option[Format], //Format finger
                     encode: Encode.Value, //Format finger
                     priority: Int,
                     reqArgs: List[ReqArg], //finger
                     repArgs: List[RepArg], //finger   fields:deal_date,region_code,user_id
                     pushArgs: Option[PushArgs],
                     location: Option[LocationInfo],
                     retFormatFinger: String,
                     retDataFinger: String,
                     isSyn: Int,
                     client: String,
                     pageSize: Option[Int],
                     pageNum: Option[Int],
                     triggerInfo: Option[String]
                    ) extends DeployMessage

//Request to Finder
case class ReqToFind(var reqID: String,
                     var apiClsCode: Int,
                     var apiId: Int,
                     var api_version: Int,
                     var apiName: String,
                     var appKey: String,
                     var apiSort: Int,
                     var timeStamp: Long,
                     var format: Option[Format],
                     var encode: String,
                     var priority: Int,
                     var reqArgs: List[ReqArg],
                     var repArgs: List[RepArg],
                     var pushArgs: Option[PushArgs],
                     var retFormatFinger: String,
                     var retDataFinger: String,
                     var isSyn: Int,
                     var pageSize: Option[Int],
                     var pageNum: Option[Int]
                    ) extends DeployMessage

//Schedule to Request
case class ScheduleToReq(rss_id: Int,
                         apiId: Int,
                         api_version: Int,
                         apiName: String,
                         timeStamp: Long,
                         format: String,
                         encode: String,
                         priority: Int,
                         reqArgs: List[ReqArg],
                         repArgs: List[RepArg],
                         retFormatFinger: String,
                         retDataFinger: String,
                         isSyn: Int) extends DeployMessage

//Request to Finder
case class UpdCycleRcd(var sourceid: Int,
                       var schemaName: String,
                       var tableName: String,
                       var finderElements: String,
                       var updCycleType: String
                      ) extends DeployMessage

case class ToolApiMsg(apiName: String, paramMap: Map[String, Any]) extends DeployMessage
//Finder to Explainer
case class FtpTestMsg(pushPluginType: String, host: String, port: Int, username: String, password: String, ftpmode: String, directory: String) extends DeployMessage
//Finder to Request
//case class FindToReqAsynYIK(reqID: String, reqStat: ReqStat.Value) extends DeployMessage

/**
  * Finder CycRcd to Schedule
  * schemaName: String, tableName: String, jsonCondtionMap: TreeMap[String, List[Any]]
  */
case class FindCycRcdToSchedule(apiIdsVersions: List[(Int, Int)], cycleRcdInfo: UpdCycleRcd)

//Finder CycRcd to Cache
case class FindCycRcdToCache(schemaName: String, tableName: String, jsonCondtionMap: TreeMap[String, List[Any]], sourceid: Int)

//Cache to Resolver
/**
  * 判断是否需要将该表该周期快照进行缓存
  *
  * @param cacheTableInfo
  */
case class CacheToExpAlone(cacheTableInfo: TableInfo) extends DeployMessage

/**
  * 根据策略搜索哪些表的快照需要清出缓存
  *
  * @param cleanTables
  */
case class CacheToExpClean(cleanTables: List[CacheTable]) extends DeployMessage

case class CleanDataFingerCache(cleanDataFingerList: List[RetDataFinger]) extends DeployMessage

case class CleanFormatFingerCache(cleanFormatFingerList: List[RetFormatFinger]) extends DeployMessage

//Resolver to Cache
/**
  * 接收Explainer 返回缓存计算结果
  * 1.更新缓存Map和数据库 ADD加入缓存：0 -> 1 ， DROP清出缓存：2 -> 3
  * 2.通知Finder更新缓存进入Dic
  *
  * @param cacheTable
  */
case class ExpCacheActionFinish(cacheTable: CacheTable) extends DeployMessage

//Controller to Finder update the data finger -> eff_flag 1:生效 0:失效
case class UpdDataFinger(treadId: Long, data_finger: String, file_loc: String, encode: String, eff_flag: Int) extends DeployMessage

//Controller to Finder update the format finger -> eff_flag 1:生效 0:失效
case class UpdFormatFinger(treadId: Long, data_finger: String, form_finger: String, file_loc: String, eff_flag: Int) extends DeployMessage

case class FindToExpNew(reqID: String,
                        apiId: Int, //finger
                        api_version: Int, //finger
                        apiName: String,
                        priority: Int,
                        retFormatFinger: String,
                        retDataFinger: String,
                        isSyn: Int
                       ) extends DeployMessage

/**
  * 更新请求状态：opi.req_stat_code
  * -1	探查有数，请求执行中
  * -2	探查无数据
  * -3	重复提交 等待
  * 0	运行结束
  * >0	运行失败
  *
  * @param reqID
  * @param reqStat
  */
case class ReqStatUpdExpToFind(reqID: String, reqStat: Int)

/**
  * 刷新采集表的元数据信息
  *
  * @param sourceId
  * @param schemaName
  * @param tabName
  */
case class WebSearchMetaTableInfo(reqId: String, sourceId: Int, schemaName: String, tabName: String)

/**
  * Master转发刷新采集表的元数据信息
  *
  * @param sender Client Actor引用地址
  * @param sourceId
  * @param schemaName
  * @param tabName
  */
case class WebSearchMetaTableInfoToChecker(sender: ActorRef, reqId: String, sourceId: Int, schemaName: String, tabName: String)

/**
  * 返回采集元数据的结果
  *
  * @param reqId
  * @param result : 0:成功，1:失败
  */
case class MetaTableInfoCollectResult(reqId: String, result: Int)

/**
  * 刷新API元数据信息
  */
case class RefreshApiMetaInfo(reqId: String, apiId: Int, apiVersion: Int)

/**
  * Master转发刷新API元数据信息
  */
case class RefreshApiMetaInfoToFinder(sender: ActorRef, reqId: String, apiId: Int, apiVersion: Int)

/**
  * 返回API后台元数据刷新结果
  *
  * @param reqId
  * @param result : 0:成功，1:失败
  */
case class RefreshApiMetaInfoResult(reqId: String, result: Int)


//Worker to Client
case class SyncExecuteResult(reqID: String, result: String, formatFinger: String) extends DeployMessage

case class SyncExecuteException(reqID: String, e: Exception) extends DeployMessage

case class SyncToolsResult(reqID: String, result: String) extends DeployMessage
case class SyncToolsException(reqID: String, e: String) extends DeployMessage
case object ReLoad extends DeployMessage
case class ReLoadType(reSetType :String) extends DeployMessage