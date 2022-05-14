package com.teradata.openapi.master.finder

import akka.actor.{Actor, ActorRef}
import com.codahale.jerkson.Json
import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.deploy._
import com.teradata.openapi.master.deploy.Master
import com.teradata.openapi.master.finder.dao.{ReqInfoDao, ReqInfoVwDao}
import com.teradata.openapi.master.finder.service._
import com.teradata.openapi.framework.message.response.FindToReqAsynYIK
import com.teradata.openapi.framework.model.ApiInfoRow
import com.teradata.openapi.framework.util.DicMapFunc
import com.teradata.openapi.master.finder.tools.ToolApis

import scala.collection.immutable.TreeMap
import scala.collection.mutable
import scala.reflect.ClassTag
import scala.concurrent.duration._

/**
  * Created by John on 2016/4/22.
  */
class FinderActor(val master: Master) extends Actor with DicMapFunc with OpenApiLogging {
  master.finderActorIns = this
  val formatFinger: FormatFinger = FormatFinger()
  log.info("FinderActor [formatFinger] Ref:" + formatFinger)
  val dataFinger: DataFinger = DataFinger()
  log.info("FinderActor [dataFinger] Ref:" + dataFinger)
  val finderDataSnapshotDicInfo = DataSnapshotDicInfo()
  log.info("FinderActor [finderDataSnapshotDic] Ref:" + finderDataSnapshotDicInfo)
  val reqinfovw = ReqInfoVwDao()
  log.info("FinderActor [reqinfovw] Ref:" + reqinfovw)
  val reqinfo = ReqInfoDao()
  log.info("FinderActor [reqinfo] Ref:" + reqinfo)
  //val toolApiMsg: Seq[ToolApiInfoRow] = ToolApiOps().getToolApiinfos
  var toolApiInfo: Seq[ApiInfoRow] = ApiInfo().getToolApiinfos
  log.info("FinderActor [toolApiInfo]:" + toolApiInfo)
  implicit val execution = context.system.dispatcher
  context.system.scheduler.schedule(66 seconds, 23 hours, master.finderActor, CleanFingerCache())

  val toolsApiMap = mutable.Map[String, ToolApis]()


  override def receive: Receive = {

    case m@MasterToFinder(sender: ActorRef, reqtofind: ReqToFind, triggerInfo: TriggerInfo)
    => {
      log.info("{}|接收到Master探查请求:{}触发源:{}", reqtofind.reqID, reqtofind.toString, triggerInfo.toString)
      reqFindLocation(sender, reqtofind, triggerInfo)
    }
    case m@UpdCycleRcdToFinder(reqID, sender: ActorRef, cycleRcdInfo: UpdCycleRcd)
    => {
      log.info("{}|接收到周期更新请求", reqID)
      updCycleRcdInfo(sender, cycleRcdInfo)
    }
    case UpdDataFinger(treadId: Long, data_finger: String, file_loc: String, encode: String, eff_flag: Int)
    => {
      log.info("接收到无格式指纹更新请求:{}", treadId)
      procUpdDataFinger(data_finger, file_loc, encode, eff_flag)
    }
    case UpdFormatFinger(treadId: Long, data_finger: String, form_finger: String, file_loc: String, eff_flag: Int)
    => {
      log.info("接收到带格式指纹更新请求:{}", treadId)
      procUpdFormFinger(data_finger, form_finger, file_loc, eff_flag)
    }
    case CleanFingerCache() => {
      log.info("接收到CleanFingerCache()根据系统参数，清除需要清除的指纹缓存")
      val cleanDataFingerList: List[RetDataFinger] = dataFinger.getCleanDataFingerList(master.sysArgs.getOrElse("cleandatafingercache_hittimes_min", 5).toString.toInt, master.sysArgs.getOrElse("cleandatafingercache_visittime_max", 5).toString.toInt, Option(master.sysArgs.getOrElse("cleandatafingercache_visittime_unit", "DAY")))
      dataFinger.cleanDataFingerCache(cleanDataFingerList)
      master.resolverActor ! CleanDataFingerCache(cleanDataFingerList)
      val cleanFormatFingerList: List[RetFormatFinger] = formatFinger.getCleanFormatFingerList(master.sysArgs.getOrElse("cleanformatfingercache_hittimes_min", 5).toString.toInt, master.sysArgs.getOrElse("cleanformatfingercache_visittime_max", 5).toString.toInt, Option(master.sysArgs.getOrElse("cleanformatfingercache_visittime_unit", "DAY")))
      formatFinger.cleanFormatFingerCache(cleanFormatFingerList)
      master.resolverActor ! CleanFormatFingerCache(cleanFormatFingerList)
    }
    case ReqStatUpdExpToFind(reqID: String, reqStat: Int) => {
      log.info("{}|接收到请求执行状态:{}", reqID, reqStat)
      reqinfo.updateReqStat(reqID, reqStat)
    }
    case RefreshApiMetaInfoToFinder(sender: ActorRef, reqId: String, apiId: Int, apiVersion: Int) => {
      log.info("{}|API发布刷新", reqId)
      val refreshResult: Int = refreshApiTableinfo(apiId: Int, apiVersion: Int)
      sender ! RefreshApiMetaInfoResult(reqId, refreshResult)
    }
    case ReLoadType(reSetType: String) => reload(reSetType)
  }

  def refreshApiTableinfo(apiId: Int, apiVersion: Int): Int = {
    val refreshResult: Int = finderDataSnapshotDicInfo.reFreshApiTabInfos(apiId, apiVersion)
    refreshResult
  }

  def procUpdDataFinger(data_finger: String, file_loc: String, encode: String, eff_flag: Int): Boolean = {
    val updStat =
      if (eff_flag equals 1) dataFinger.add(data_finger, file_loc, encode, eff_flag)
      else dataFinger.drop(data_finger, eff_flag)
    updStat
  }

  def procUpdFormFinger(data_finger: String, form_finger: String, file_loc: String, eff_flag: Int): Boolean = {
    val updStat =
      if (eff_flag equals 1) formatFinger.add(form_finger, file_loc, data_finger, eff_flag)
      else formatFinger.drop(form_finger, eff_flag)
    updStat
  }

  def reqFindLocation(sender: ActorRef, reqtofind: ReqToFind, triggerInfo: TriggerInfo) = {
    val reqID: String = reqtofind.reqID
    val apiClsCode: Int = reqtofind.apiClsCode
    val apiId: Int = reqtofind.apiId
    val api_version: Int = reqtofind.api_version
    val apiName: String = reqtofind.apiName
    val appKey: String = reqtofind.appKey
    val apiSort: Int = reqtofind.apiSort
    val timeStamp: Long = reqtofind.timeStamp
    val format: Option[Format] = reqtofind.format
    val encode: String = reqtofind.encode
    val priority: Int = reqtofind.priority
    val reqArgs: List[ReqArg] = reqtofind.reqArgs
    val repArgs: List[RepArg] = reqtofind.repArgs
    val pushArgs: Option[PushArgs] = reqtofind.pushArgs
    val retFormatFinger: String = reqtofind.retFormatFinger
    val retDataFinger: String = reqtofind.retDataFinger
    val isSyn: Int = reqtofind.isSyn
    val pageSize: Option[Int] = reqtofind.pageSize
    val pageNum: Option[Int] = reqtofind.pageNum
    val findtoReqAsynYik = new FindToReqAsynYIK()

    def metaDataCollection[T: ClassTag](elems: T*) = mutable.Set[T](elems: _*) //将元素放入Set

    def findDataSource(getFinderARG: mutable.Map[String, Any], schemaName: String, tabName: String): Option[RetLocation] = {
      var local: Option[RetLocation] = null
      val getFinderARGS: List[mutable.Map[String, Any]] = metaDataCollection(getFinderARG).toList
      log.debug("apiId:" + apiId + " api_version:" + api_version + " getFinderARGS:" + getFinderARGS)
      val isStartHiveCacheFlag: Boolean = try {
        master.sysArgs.getOrElse("isStartHiveCache", "false").toBoolean
      } catch {
        case e: Exception => false
      }
      local = finderDataSnapshotDicInfo.find(isSyn, apiId, api_version, getFinderARGS, isStartHiveCacheFlag)
      if (local.nonEmpty) {
        //帮齐伟删除掉错误的source_id记录
        val localList: List[TableInfo] = local.get match {
          case RetLocation(x) => x
          case _ => List()
        }
        //log.debug("findDataSource reqArgs 1:" + reqArgs)
        for (elem_local <- localList; elem_reqArg <- reqArgs if (schemaName + "." + tabName).toUpperCase equals elem_local.tableFullName.toUpperCase) {
          //elem_reqArg.field_sorc_type = for (elem <- elem_reqArg.field_sorc_type if elem.sorc_id equals elem_local.sourceID) yield elem
          if ((elem_reqArg.fieldName.toUpperCase equals "_EXPRESSION_") && elem_reqArg.expressionAtomSorcTypeMap.get.nonEmpty){
            val filteredExpressionAtomSorcTypeMap = mutable.Map[String, List[SorcType]]()
            for (atomSorcTypeMap <- elem_reqArg.expressionAtomSorcTypeMap.get) {
              val filterSorcTypeList: List[SorcType] = for (sorcType: SorcType <- atomSorcTypeMap._2 if sorcType.sorc_id equals elem_local.sourceID) yield sorcType
              filteredExpressionAtomSorcTypeMap += (atomSorcTypeMap._1 -> filterSorcTypeList)
            }
            elem_reqArg.expressionAtomSorcTypeMap = Some(filteredExpressionAtomSorcTypeMap.toMap)
          } else {
            elem_reqArg.field_sorc_type = for (elem <- elem_reqArg.field_sorc_type if elem.sorc_id equals elem_local.sourceID) yield elem
          }
        }
        //log.debug("findDataSource reqArgs 2:" + reqArgs)
      }
      local
    }

    def outLocal(isSyn: Int, elemSorcType: SorcType, getFinderARG: mutable.Map[String, Any]): (Boolean, Option[LocationInfo]) = {
      var sendResolverFlag: Boolean = true
      var local: Option[LocationInfo] = null
      if (isSyn == 0) {
        //同步访问，只探查数据源，不再探查指纹文件
        local = findDataSource(getFinderARG, elemSorcType.schemaName, elemSorcType.tabName)
      }
      else {
        //订阅+异步
        //探查数据：先进行带格式缓存探查，再进行无格式缓存探查，最后进行表周期快照探查
        local = formatFinger.find(retFormatFinger)
        log.debug("Found formatFinger:" + retFormatFinger)
        if (local.isEmpty) {
          local = dataFinger.find(retDataFinger)
          log.debug("Found dataFinger:" + retDataFinger)
          if (local.isEmpty) {
            log.debug("findDataSource getFinderARG:" + getFinderARG)
            local = findDataSource(getFinderARG, elemSorcType.schemaName, elemSorcType.tabName)
            log.debug("findDataSource out asyn reqArgs 2:" + reqArgs)
          }
        } else {
          //如果带格式指纹探查有数据，则不再发消息给Resolver
          if (isSyn == 1) sendResolverFlag = false //如果异步调用发现有数据，则不再发消息给Resolver，而是直接记录表信息即可；
        }
        log.info("【订阅+异步】探查的数据位置LOCAL:" + local)
      }
      (sendResolverFlag, local)
    }

    def listSorcTypeLoop(elemCycleReqArgs: Option[ReqArg], listSorcType: List[SorcType], local: Option[LocationInfo]): (Int, Boolean, Option[LocationInfo]) = {
      var sendResolverFlag: Boolean = true
      var getFinderARG = mutable.Map[String, Any]()
      var ckFieldValue: Int = 0
      var localThis: Option[LocationInfo] = null
      for (elemSorcType <- listSorcType if (local == null || local.isEmpty) && ckFieldValue >= 0) {
        log.debug("elemSorcType:" + elemSorcType + " local:" + local + " ckFieldValue:" + ckFieldValue)
        val getFinderFullTable = elemSorcType.schemaName + "." + elemSorcType.tabName
        if (elemCycleReqArgs.isEmpty) {
          getFinderARG += (getFinderFullTable -> Map())
        } else {
          try {
            var elemFieldValue: List[Any] = for (e <- elemCycleReqArgs.get.fieldValue) yield e.toString
            log.debug("elemFieldValue:" + elemFieldValue)
            if ("any" equals elemFieldValue.head.toString) {
              try {
                val elemVals: Map[String, List[Any]] = Json.parse[Map[String, List[Any]]](triggerInfo.trigger_sorc)
                elemFieldValue = elemVals.getOrElse(elemCycleReqArgs.get.fieldName, List("any"))
              } catch {
                case e: Exception => {
                  log.error("triggerInfo.trigger_sorc Error:" + triggerInfo.trigger_sorc)
                  elemFieldValue = List("any")
                }
              }
            }
            getFinderARG += (getFinderFullTable -> TreeMap(elemCycleReqArgs.get.fieldName -> elemFieldValue))
          }
          catch {
            case e: Exception => {
              log.error("处理reqArgs错误，可能有未识别数据类型:" + e.getMessage)
              ckFieldValue = -1
            }
          }
        }
        //检查参数类型是否合法，不合法通知client，合法往下走
        /*log.debug("dataFinger content:" + dataFinger.toString)
          log.debug("formatFinger content:" + formatFinger.toString)*/

        if (ckFieldValue == -1) {
          findtoReqAsynYik.setRetCode("-1")
          findtoReqAsynYik.setRetMsg("Error, the field value type no match.")
          sender ! findtoReqAsynYik //参数类型不匹配，消息通知client
        }
        else {
          val retOutLocal = outLocal(isSyn, elemSorcType, getFinderARG)
          sendResolverFlag = retOutLocal._1
          localThis = retOutLocal._2
        }
        log.debug("必选参数字段类型异常标识：ckFieldValue:" + ckFieldValue + " local:" + local)
      }
      (ckFieldValue, sendResolverFlag, localThis)
    }


    //取RDBMS数据类API
    if (apiClsCode == 1) {
      var sendResolverFlag: Boolean = true
      //var getFinderARG = mutable.Map[String, Any]()
      var ckFieldValue: Int = 0
      var local: Option[LocationInfo] = null

      log.debug("REQARGS:" + reqArgs.toString)
      val cycleColumnList: List[ReqArg] = for (elemReqArgs: ReqArg <- reqArgs if elemReqArgs.mustType equals 3) yield elemReqArgs
      if (cycleColumnList.isEmpty) {
        log.debug("Not have cycleColumn finder.")
        val listSorcType: List[SorcType] = finderDataSnapshotDicInfo.listTables(isSyn, apiId, api_version)
        log.debug("Api contain tables:" + listSorcType)
        val retListSorcTypeLoop = listSorcTypeLoop(None, listSorcType, local)
        ckFieldValue = retListSorcTypeLoop._1
        sendResolverFlag = retListSorcTypeLoop._2
        local = retListSorcTypeLoop._3
        //(ckFieldValue,sendResolverFlag,local) = listSorcTypeLoop(None, listSorcType, local)
      } else {
        for (elemCycleReqArgs: ReqArg <- cycleColumnList if local == null && ckFieldValue >= 0) {
          log.debug("elemCycleReqArgs:" + elemCycleReqArgs)
          val listSorcType: List[SorcType] = elemCycleReqArgs.field_sorc_type
          log.debug("sorcType:" + listSorcType + " local:" + local + " ckFieldValue:" + ckFieldValue)
          //(ckFieldValue, sendResolverFlag, local) = listSorcTypeLoop(Some(elemCycleReqArgs), listSorcType, local)
          val retListSorcTypeLoop = listSorcTypeLoop(Some(elemCycleReqArgs), listSorcType, local)
          ckFieldValue = retListSorcTypeLoop._1
          sendResolverFlag = retListSorcTypeLoop._2
          local = retListSorcTypeLoop._3

        }
      }

      log.info("探查的数据位置LOCAL:" + local)
      //send FindToExp to Explainer
      //reqStat:reqStat:req_stat -1探查有数，执行状态，往下走，执行取数To Explainer
      //                        -2无数据
      //                        -3重复提交 等待
      //                         0 运行结束
      //                        >0 失败
      if (local == null || local.isEmpty) {
        //探查无数据，异步新提交自动转为订阅请求，
        var trigger_methd: String = ""
        if ((ckFieldValue > 0) && (isSyn equals 1)) {
          //异步新提交自动转为订阅请求；
          findtoReqAsynYik.setRetMsg("Received, Waiting for data")
          trigger_methd = sender.toString
          //异步取数当前为无数据状态，已提交走等待状态,当数据更新生成之后自动回复请求方
        }
        else if ((ckFieldValue > 0) && (isSyn equals 0)) {
          //同步请求
          findtoReqAsynYik.setRetMsg("Syn request not have data")
          trigger_methd = sender.toString
        }
        else if ((ckFieldValue > 0) && (isSyn equals 2)) {
          //订阅请求
          findtoReqAsynYik.setRetMsg("Rss request not have data still")

        }
        findtoReqAsynYik.setReqID(reqID)
        findtoReqAsynYik.setReqStat(ReqStat.NODATA.toString)
        findtoReqAsynYik.setRetCode("1")
        log.info("isSyn[" + isSyn + "]reqID[" + reqID + "]探查无数据")

        val trigger_sorc = if (triggerInfo.trigger_sorc equals "") sender.toString else triggerInfo.trigger_sorc
        reqinfo.upsert(reqID, req_stat = -2, appKey, apiId, api_version, Json.generate(format), encode, Json.generate(reqArgs), Json.generate(reqArgs), Json.generate(pushArgs), retFormatFinger, retDataFinger, api_visit_methd = isSyn, triggerInfo.trigger_methd, trigger_sorc, Json.generate(local), triggerInfo.rss_id, priority)
        sender ! findtoReqAsynYik //探查无数据，同步、异步都通知Client；订阅通知Schedule，sender是Schedule Actor
      }
      else {
        //探查有数据
        //如果没有未执行完的任务
        if (!reqinfovw.existsReq(api_visit_methd = isSyn, apiId, appKey, Json.generate(local), reqStats = Set(-1))) {
          findtoReqAsynYik.setRetCode("0")
          findtoReqAsynYik.setRetMsg("Received,Fetching data")
          val trigger_sorc: String = if (triggerInfo.trigger_sorc equals "") sender.toString else triggerInfo.trigger_sorc
          if (sendResolverFlag) {
            reqinfo.upsert(reqID, req_stat = -1, appKey, apiId, api_version, Json.generate(format), encode, Json.generate(reqArgs), Json.generate(reqArgs), Json.generate(pushArgs), retFormatFinger, retDataFinger, api_visit_methd = isSyn, triggerInfo.trigger_methd, trigger_sorc, Json.generate(local), triggerInfo.rss_id, priority)
            val encode_real = if (encode equals "UTF-8") "UTF8" else encode
            log.info("探查有数据,发送FindToExp消息给resolverActor:" + reqArgs)
            master.resolverActor ! FindToExp(reqID, apiId, api_version, apiName, format, Encode.withName(encode_real.toUpperCase), priority, reqArgs, repArgs, pushArgs, local, retFormatFinger, retDataFinger, isSyn, sender.path.toString, pageSize, pageNum, Some(Json.generate(triggerInfo)))
          } else {
            reqinfo.upsert(reqID, req_stat = 0, appKey, apiId, api_version, Json.generate(format), encode, Json.generate(reqArgs), Json.generate(reqArgs), Json.generate(pushArgs), retFormatFinger, retDataFinger, api_visit_methd = isSyn, triggerInfo.trigger_methd, trigger_sorc, Json.generate(local), triggerInfo.rss_id, priority)
            log.info("探查有带带格式指纹数据,不再发送FindToExp消息给resolverActor!")
          }
        }
        else {
          //有相同未执行完的任务，重复提交，不再插入reqinfo
          findtoReqAsynYik.setRetCode("-3")
          findtoReqAsynYik.setRetMsg("Request duplication")
          findtoReqAsynYik.setReqStat("PLEASE WAIT")
        }
        //异步调度，则发消息通知Client[Request]；订阅调度，发消息通知Scheduler；同步不发
        if ((isSyn equals 1) || (isSyn equals 2)) {
          findtoReqAsynYik.setReqID(reqID)
          findtoReqAsynYik.setReqStat(ReqStat.HIVEDATA.toString)
          sender ! findtoReqAsynYik
        }
      }
    }
    //工具类API
    else if (apiClsCode == 2) {
      //log.info("{}|接收到工具类API请求:{}", reqID, apiName.toString)
      val apiMsgClassName: String = (for (elem <- toolApiInfo if elem.api_name equals apiName) yield elem.api_class_name).head
      val clazzOpt = toolsApiMap.get(apiMsgClassName)
      val clazz: ToolApis = if (clazzOpt.isEmpty) {
        //val toolApiInstance = ToolApiLoader().findClass(apiMsgClassName).newInstance().asInstanceOf[ToolApis]
        val toolApiInstance = Class.forName(apiMsgClassName).newInstance().asInstanceOf[ToolApis]
        toolsApiMap.+=(apiMsgClassName -> toolApiInstance)
        toolApiInstance
      } else {
        clazzOpt.get
      }
      //val clazz: ToolApis = Class.forName(apiMsgClassName).newInstance().asInstanceOf[ToolApis]
      val ret: FindToReqAsynYIK = clazz.execute(reqID, master, reqArgs)
      sender ! ret
    }

  }


  /**
    * 更新DataSnpstDd信息，
    * 增加ADD，判断是否需要搬迁数据到缓存中，调度相关订阅任务；
    * 删除DROP，删除DataSnpstDd信息
    *
    * @param sender
    * @param cycleRcdInfo UpdCycleRcd(sourceid,schemaName,tableName,finderElements,updCycleType)
    */
  def updCycleRcdInfo(sender: ActorRef, cycleRcdInfo: UpdCycleRcd) = {
    val finderCacheFinger = FinderCacheFinger()
    val sourceid: Int = cycleRcdInfo.sourceid
    val schemaName: String = cycleRcdInfo.schemaName
    val tableName: String = cycleRcdInfo.tableName
    val finderElements: String = cycleRcdInfo.finderElements
    val updCycleType: String = cycleRcdInfo.updCycleType
    val isStartHiveCacheFlag: Boolean = try {
      master.sysArgs.getOrElse("isStartHiveCache", "false").toBoolean
    } catch {
      case e: Exception => false
    }
    log.debug("UpdCycleRcdToFinder:" + finderElements)
    val jsonCondtionMap: TreeMap[String, List[Any]] = mapToTreeMapVallst(jsonToMap(finderElements))
    log.debug("jsonCondtionMap:" + jsonCondtionMap.toString)
    if (updCycleType.toUpperCase equals "ADD") {
      //val updApiIdList: List[Int] = finderDataSnapshotDic.add(sourceid, schemaName + "." + tableName, jsonCondtionMap)
      val updApiIdList: List[(Int, Int)] = finderDataSnapshotDicInfo.add(sourceid, schemaName, tableName, jsonCondtionMap)
      //log.info()
      //Msg CycRcd to Cache
      log.debug("updApiIdList :" + updApiIdList)
      //********************当有表快照更新，通知Cache判断是否要进入Hive缓存*******************************************************************
      if (isStartHiveCacheFlag && sourceid != 0) master.cacheActor ! FindCycRcdToCache(schemaName, tableName, jsonCondtionMap, sourceid)
      //Msg CycRcd to Schedule 表周期快照有更新，考虑将订阅的任务调度起来
      if (updApiIdList.nonEmpty) {
        master.scheduleActor ! FindCycRcdToSchedule(updApiIdList, cycleRcdInfo)
        //Cycle工具触发扫描是否应该清楚过期指纹信息，并将过期的信息迁入req_info_his中
        finderCacheFinger.cleanRedis(finderCacheFinger.findFingerMap(schemaName, tableName, jsonCondtionMap))
      }
    }
    else if (updCycleType.toUpperCase equals "DROP") {
      //finderDataSnapshotDic.drop(sourceid, schemaName + "." + tableName, jsonCondtionMap)
      log.debug("finderDataSnapshotDicInfo DROP:" + updCycleType.toUpperCase)
      finderDataSnapshotDicInfo.drop(sourceid, schemaName, tableName, jsonCondtionMap)
    }
    else {

    }
  }

  def reload(reSetType: String) = {
    reSetType.toUpperCase match {
      case "FORMATFINGER" => this.formatFinger.reload()
      case "DATAFINGER" => this.dataFinger.reload()
      case "SOURCEIDS" => this.finderDataSnapshotDicInfo.reloadSourceIds()
      case "APITABINFOS" => this.finderDataSnapshotDicInfo.reloadApiTabinfos()
      case "SNAPSHOTDIC" => this.finderDataSnapshotDicInfo.reloadSourceSnapshotDic()
      case "TOOLAPIINFOS" => this.toolApiInfo = ApiInfo().reloadToolApiinfos
      case "ALL" => {
        this.formatFinger.reload()
        this.dataFinger.reload()
        this.finderDataSnapshotDicInfo.reloadSourceIds()
        this.finderDataSnapshotDicInfo.reloadApiTabinfos()
        this.finderDataSnapshotDicInfo.reloadSourceSnapshotDic()
        this.toolApiInfo = ApiInfo().reloadToolApiinfos
      }
      case _ => throw new RuntimeException("Unknown parameters that cannot be resolved.")
    }
  }
}
