package com.teradata.openapi.master.schedule


import akka.actor.{Actor, ActorRef}
import com.codahale.jerkson.Json
import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.deploy._
import com.teradata.openapi.framework.deploy.{RepArg, ReqArg}
import com.teradata.openapi.master.schedule.dao.{ApiRssInfoDao, ApiRssInfoVwDao}

import scala.collection.immutable.TreeMap
import com.codahale.jerkson.Json._
import com.teradata.openapi.framework.message.response.FindToReqAsynYIK
import com.teradata.openapi.framework.model.{ApiRssInfoRow, ApiRssInfoVwRow, ReqInfoRow, ReqInfoVwRow}
import com.teradata.openapi.framework.util.{DicMapFunc, TimeFunc, Utils}
import com.teradata.openapi.master.deploy.Master
import com.teradata.openapi.master.finder.dao.ReqInfoVwDao
import com.xiaoleilu.hutool.util.SecureUtil
import it.sauronsoftware.cron4j.InvalidPatternException

import scala.concurrent.duration._

/**
  * Created by John on 2016/5/12.
  */
class ScheduleActor(val master: Master) extends Actor with TimeFunc with DicMapFunc with OpenApiLogging {
  master.scheduleActorIns = this
  var apirssinfovw = ApiRssInfoVwDao()
  log.info("ScheduleActor [apirssinfovw] Ref:" + apirssinfovw)
  var reqinfovw = ReqInfoVwDao()
  //log.info("ScheduleActor [reqinfovw] Ref:" + reqinfovw)
  var timeTriggerRssInfos: Seq[ApiRssInfoVwRow] = apirssinfovw.loadByTimeTrigger
  log.info("ScheduleActor [timeTriggerRssInfos]:" + timeTriggerRssInfos)
  //时间触发
  implicit val execution = context.system.dispatcher
  var lastMills: Long = System.currentTimeMillis()
  context.system.scheduler.schedule(60 seconds, 10 seconds, new Runnable {
    override def run() = {
      val currentMills = System.currentTimeMillis()
      if (lastMills / 1000 / 60 != currentMills / 1000 / 60) {
        master.scheduleActor ! ClockTimeStamp(currentMills)
        lastMills = currentMills
      }
    }
  })


  //接收消息
  override def receive: Receive = {
    case ClockTimeStamp(currentMills: Long)
    => {
      log.info("Schedule接收到时间触发")
      timeScheduler(currentMills)
      log.info("Schedule接收到时间触发完成")
    }
    case FindCycRcdToSchedule(apiIdsVersions: List[(Int, Int)], cycleRcdInfo: UpdCycleRcd)
    => {
      val eventEntity: String = cycleRcdInfo.sourceid + "_" + cycleRcdInfo.schemaName + "_" + cycleRcdInfo.tableName
      val triggerApi: String = apiIdsVersions.head._1 + "_" + apiIdsVersions.head._2
      log.info("Schedule接收到事件:" + eventEntity + " 触发API:" + triggerApi)
      event(apiIdsVersions, cycleRcdInfo)
    }
    //Schedule触发了一个新的异步取数任务给Finder，Finder会马上回复一条消息，供调用方来判断是否有数据
    case x: FindToReqAsynYIK
    => {
      if ("HIVEDATA" equals x.getReqStat) {
        log.info("{}|FinderToSchedule have data and has been notified to the ResolverActor...Report:[getReqStat:{} getRetMsg:{} getRetCode:{}]", x.getReqID, x.getReqStat, x.getRetMsg, x.getRetCode) // 有数据，正在执行
      } else {
        log.info("{}|FinderToSchedule find but no data yet.getReqStat:{} getRetMsg:{} getRetCode:{}", x.getReqID, x.getReqStat, x.getRetMsg, x.getRetCode) // rcdNextEvent() 有待下一次触发
      }
    }
    case refreshTimeTriggerRss()
    => {
      log.info("更新订阅时间触发列表")
      refreshTimeTriggerRssInfos(apirssinfovw)
    }
    case ReLoad => reload()
  }

  /*
case class ScheduleToReq(          case class SorcType(	         case class RepArg(fieldAlias: String,  //finger	   case class ReqArg(fieldName: String, //finger
     rss_id: Int,                   sorc_id :Int,	                                 fieldName: String,	                                 fieldTargtType: String,
     apiId :Int,                    sorc_field_type:String,	                       fieldTargtType: String,	                       var field_sorc_type: List[SorcType],
     api_version: Int,              sorc_format:String,	                           fieldTitle: String,	                               calcPrincId: Int,  //finger
     apiName: String,               sorc_max_len:Int,	                             schemaName: String,	                               fieldValue: List[Any],  //finger
     timeStamp: Long,               sorc_total_digit :Int,	                       tabName: String,	                                   mustType: String,
     format: String,                sorc_prec_digit:Int)extends Serializable	     tabAlias: String	                                   schemaName: String,
     encode: String,	                                                           ) extends Serializable	                               tabName: String,
     priority: Int,		                                                                                                                 tabAlias: String
     reqArgs: List[ReqArg],		                                                                                                       ) extends Serializable
     repArgs: List[RepArg],
     retFormatFinger: String,
     retDataFinger: String,
     isSyn: Boolean,
     isTool: Boolean) extends DeployMessage
   */
  /**
    * 事件触发器
    * 触发异步转订阅任务，需接收运行完之后消息，注销任务
    *
    * @param apiIdsVersions 事件相关APIs Versions
    * @param cycleRcdInfo   UpdCycleRcd(sourceid,schemaName,tableName,finderElements,updCycleType)
    *                       reqStat: -1探查有数，执行状态，往下走，执行取数To Explainer
    *                       -2无数据
    *                       -3重复提交 等待
    *                       0运行结束
    *                       >0失败
    */
  def event(apiIdsVersions: List[(Int, Int)], cycleRcdInfo: UpdCycleRcd) = {

    val schemaName = cycleRcdInfo.schemaName
    val tableName = cycleRcdInfo.tableName

    //触发订阅任务
    val apiRssInfos: Seq[ApiRssInfoVwRow] = apirssinfovw.loadByApiIds(apiIdsVersions)
    for (elem <- apiRssInfos) {
      val jsonCondtionMap: TreeMap[String, List[Any]] = mapToTreeMapVallst(jsonToMap(cycleRcdInfo.finderElements))
      //触发事件
      val flatCondtionMap: Map[Any, String] = jsonCondtionMap.flatMap(m => m._2.map(v => (v,m._1)))
      for (flatCondElem <- flatCondtionMap) {
        val elemJsonCondtionMap: TreeMap[String, Any] = TreeMap(flatCondElem._2 -> flatCondElem._1)
        val triggerSorc: String = generate(elemJsonCondtionMap)

        log.debug("elemJsonCondtionMap:" + elemJsonCondtionMap)
        log.debug("triggerSorc:" + triggerSorc)

        //依次处理订阅任务 elem.rss_id
        //处理请求参数 elem.req_arg
        var reqArgList: List[ReqArg] = null
        try {
          reqArgList = parse[List[ReqArg]](elem.req_arg)
        } catch {
          case e:Exception => {
            log.error("RSS task ReqArgs Error:" + e.getMessage + e)
          }
        }

        log.debug("reqArgList:" + reqArgList)

        //将订阅中的DEAL_DATE=any改成为由CycleRcd(保存在rss的trigger_sorc)提供的周期值

        val correctReqArgList: List[ReqArg] = for (reqArgElem <- reqArgList) yield reqArgBuilder(elemJsonCondtionMap, reqArgElem)
        log.debug("Rss correctReqArgList:" + correctReqArgList)

        //处理响应参数 elem.respn_arg
        var repArgList: List[RepArg] = null
        try {
          repArgList = parse[List[RepArg]](elem.respn_arg)
        } catch {
          case e:Exception => {
            log.error("RSS task RepArgs Error:" + e.getMessage + e)
          }
        }

        //获取推送参数
        var pushArgs: Option[PushArgs] = None
        try {
          pushArgs = Some(parse[PushArgs](elem.push_arg))
        } catch {
          case e: Exception => {
            log.info("RSS task PushArgs Error:" + e.getMessage + e)
            pushArgs = None
          }
        }

        //订阅请求，为其任务生成一个uuid
        val uuid: String = java.util.UUID.randomUUID.toString.toUpperCase
        log.info("为订阅请求[" + elem.rss_id + "]生成请求[" + uuid + "]")

        val format: Format =
          try {
            Json.parse[Format](elem.form_code)
          } catch {
            case e:Exception => {
              log.info("格式转换失败:" + e.getMessage + e)
              Format("Json",None)
            }
          }
        //生成消息主体内容ReqToFind
        val reqtofind: ReqToFind = ReqToFind(uuid, elem.api_cls_code, elem.api_id, elem.api_version, elem.api_name, elem.app_key, elem.api_sort, System.currentTimeMillis(), Some(format), elem.encode, elem.priority, correctReqArgList, repArgList, pushArgs, elem.retn_form_finger, elem.retn_data_finger, isSyn = 2, None, None)
        log.debug("Rss schedule reqtofind:" + reqtofind)
        val retn_form_finger_apd = Utils.finger(reqtofind,isFormat = true)
        val retn_data_finger_apd = Utils.finger(reqtofind,isFormat = false)
        val reqtofindApd: ReqToFind = ReqToFind(uuid, elem.api_cls_code, elem.api_id, elem.api_version, elem.api_name, elem.app_key, elem.api_sort, System.currentTimeMillis(), Some(format), elem.encode, elem.priority, correctReqArgList, repArgList, pushArgs, retn_form_finger_apd, retn_data_finger_apd, isSyn = 2, None, None)
        log.debug("Rss schedule reqtofindApd:" + reqtofindApd)

        //如果请求实例表中有正在等待或执行未完成的记录，则不做处理。如果没有这样的记录，则发探测消息给FinderActor
        if (!reqinfovw.existsReq(api_visit_methd = 2, elem.api_id, elem.app_key, triggerSorc, reqStats = Set(-1))) {
          val triggerInfo: TriggerInfo = TriggerInfo(1, triggerSorc, elem.rss_id)
          master.finderActor ! MasterToFinder(self: ActorRef, reqtofindApd, triggerInfo)
        }
      }
    }

    log.debug("触发异步转订阅任务，需接收运行完之后消息，注销任务")
    //触发异步转订阅任务，需接收运行完之后消息，注销任务
    val apiRssReq: Seq[ReqInfoVwRow] = reqinfovw.loadByTriggerSorc(apiIdsVersions, api_visit_methd = Set(1), reqStats = Set(-2))
    for (elem <- apiRssReq) {
      val a: String = cycleRcdInfo.finderElements
      val jsonCondtionMap: TreeMap[String, List[Any]] = mapToTreeMapVallst(jsonToMap(cycleRcdInfo.finderElements))
      //触发事件
      val flatCondtionMap: Map[Any, String] = jsonCondtionMap.flatMap(m => m._2.map(v => (v,m._1)))
      for (flatCondElem <- flatCondtionMap) {
        val elemJsonCondtionMap: TreeMap[String, Any] = TreeMap(flatCondElem._2 -> flatCondElem._1)
        val triggerSorc = generate(elemJsonCondtionMap)
        //处理请求参数 elem.req_arg
        var reqArgList: List[ReqArg] = null
        try {
          reqArgList = parse[List[ReqArg]](elem.req_arg)
        } catch {
          case e:Exception => {
            log.error("AsynToRss task ReqArgs Error:" + e.getMessage + e)
          }
        }
        //将订阅中的DEAL_DATE=any改成为由CycleRcd(保存在rss的trigger_sorc)提供的周期值
        val correctReqArgList: List[ReqArg] = for (reqArgElem <- reqArgList) yield reqArgBuilder(elemJsonCondtionMap, reqArgElem)
        log.debug("AsynToRss correctReqArgList:" + correctReqArgList)
        //处理响应参数 elem.respn_arg
        var repArgList: List[RepArg] = null
        try {
          repArgList = parse[List[RepArg]](elem.respn_arg)
        } catch {
          case e:Exception => {
            log.error("AsynToRss task RepArgs Error:" + e.getMessage + e)
          }
        }
        //获取推送参数
        var pushArgs: Option[PushArgs] = None
        try {
          pushArgs = Some(parse[PushArgs](elem.push_arg))
        } catch {
          case e: Exception => {
            log.info("AsynToRss task PushArgs Error:" + e.getMessage + e)
            pushArgs = None
          }
        }
        //发起异步转订阅的请求
        log.info("AsynToRss CycRcd更新schemaName[" + schemaName + "]tableName[" + tableName + "]jsonCondtionMap[" + jsonCondtionMap + "]，触发异步转订阅请求[" + elem.req_id + "]")
        //生成消息主体内容ReqToFind
        val format =
          try {
            Json.parse[Format](elem.form_code)
          } catch {
            case e:Exception => {
              log.info("格式转换失败:" + e.getMessage + e)
              Format("Json",None)
            }
          }
        val reqtofind = ReqToFind(elem.req_id, elem.api_cls_code, elem.api_id, elem.api_version, elem.api_name, elem.appkey, elem.api_sort, System.currentTimeMillis(), Some(format), elem.encode, elem.priority, correctReqArgList, repArgList, pushArgs, elem.retn_form_finger, elem.retn_form_finger, isSyn = 2, None, None)
        log.debug("AsynToRss schedule reqtofind:" + reqtofind)
        val retn_form_finger_apd = Utils.finger(reqtofind,isFormat = true)
        val retn_data_finger_apd = Utils.finger(reqtofind,isFormat = false)
        val reqtofindApd: ReqToFind = ReqToFind(elem.req_id, elem.api_cls_code, elem.api_id, elem.api_version, elem.api_name, elem.appkey, elem.api_sort, System.currentTimeMillis(), Some(format), elem.encode, elem.priority, correctReqArgList, repArgList, pushArgs, retn_form_finger_apd, retn_data_finger_apd, isSyn = 2, None, None)
        log.debug("Rss schedule reqtofindApd:" + reqtofindApd)

        //如果频繁接收到对某张表的固定周期变化，会发生此类情况，效率不高
        if (!reqinfovw.existsReq(api_visit_methd = 2, elem.api_id, elem.appkey, triggerSorc, reqStats = Set(-1))) {
          val triggerInfo: TriggerInfo = TriggerInfo(3, triggerSorc, 0)
          master.finderActor ! MasterToFinder(self: ActorRef, reqtofindApd, triggerInfo)
        }
      }
    }
  }

  /**
    * 将订阅任务的触发参数值替换到请求参数值中，替换规则按照参数名称进行匹配
    *
    * @param jsonCondtionMap 触发参数
    * @param reqArgElem      初始请求参数
    * @return 替换后的请求参数
    */
  def reqArgBuilder(jsonCondtionMap: TreeMap[String, Any], reqArgElem: ReqArg): ReqArg = {
    val fieldValue: List[Any] = if (reqArgElem.mustType equals 3) List(jsonCondtionMap.getOrElse(reqArgElem.fieldName, "any")) else reqArgElem.fieldValue
    val reqargelem: ReqArg = ReqArg(reqArgElem.fieldName, reqArgElem.fieldTargtType, reqArgElem.field_sorc_type, reqArgElem.calcPrincId, fieldValue, reqArgElem.mustType, reqArgElem.expressionAtomSorcTypeMap)
    reqargelem
  }

  /**
    * 时间触发器
    *
    * @param currentMills 当前时间戳
    */
  def timeScheduler(currentMills: Long) = {
    for (elem <- timeTriggerRssInfos) {
      try {
        val cronExpression: String = elem.trigger_sorc
        if (cronTriggerMatch(cronExpression, currentMills)) {
          val uuid = java.util.UUID.randomUUID.toString.toUpperCase
          val reqArgList: List[ReqArg] = parse[List[ReqArg]](elem.req_arg)
          val repArgList: List[RepArg] = parse[List[RepArg]](elem.respn_arg)
          //获取推送参数
          var pushArgs: Option[PushArgs] = None
          try {
            pushArgs = Some(parse[PushArgs](elem.push_arg))
          } catch {
            case e: Exception => {
              log.info(e.getMessage + e)
              pushArgs = None
            }
          }

          val format =
            try {
              Json.parse[Format](elem.form_code)
            } catch {
              case e:Exception => {
                log.info("格式转换失败:" + e.getMessage + e)
                Format("Json",None)
              }
            }
          //md5指纹重置
          val reqtofind = ReqToFind(uuid, elem.api_cls_code, elem.api_id, elem.api_version, elem.api_name, elem.app_key, elem.api_sort, System.currentTimeMillis(), Some(format), elem.encode, elem.priority, reqArgList, repArgList, pushArgs, elem.retn_form_finger, elem.retn_form_finger, isSyn = 2, None, None)
          val retn_form_finger_apd = Utils.finger(reqtofind,isFormat = true)
          val retn_data_finger_apd = Utils.finger(reqtofind,isFormat = false)
          val reqtofindApd: ReqToFind = ReqToFind(uuid, elem.api_cls_code, elem.api_id, elem.api_version, elem.api_name, elem.app_key, elem.api_sort, System.currentTimeMillis(), Some(format), elem.encode, elem.priority, reqArgList, repArgList, pushArgs, retn_form_finger_apd, retn_data_finger_apd, isSyn = 2, None, None)
          log.debug("Rss schedule reqtofindApd:" + reqtofindApd)

          val triggerInfo: TriggerInfo = TriggerInfo(2, cronExpression, elem.rss_id)
          master.finderActor ! MasterToFinder(self: ActorRef, reqtofindApd: ReqToFind, triggerInfo)
        }
      } catch {
        case e: InvalidPatternException => log.info("The cron Expression [" + elem.trigger_sorc + "] is wrong.")
        case e: Exception => log.info("timeScheduler internal error:" + e.printStackTrace())
      }
    }
  }

  /**
    * 刷新时间触发订阅信息
    *
    * @param apirssbaseinfo 时间触发DAO
    */
  def refreshTimeTriggerRssInfos(apirssbaseinfo: ApiRssInfoVwDao) = {
    this.timeTriggerRssInfos = apirssbaseinfo.loadByTimeTrigger
  }

  def reload() = {
    this.apirssinfovw = ApiRssInfoVwDao()
    this.reqinfovw = ReqInfoVwDao()
  }


}

object TestSche {
  def main(args: Array[String]) {
    var apirssinfovw = ApiRssInfoVwDao()
    val apiIdsVersions: List[(Int, Int)] = List((1001,1))
    val apiRssInfos: Seq[ApiRssInfoVwRow] = apirssinfovw.loadByApiIds(apiIdsVersions)
    for (elem <- apiRssInfos) {
      val format: Format =
        try {
          println(elem.form_code)
          Json.parse[Format](elem.form_code)
        } catch {
          case e:Exception => {
            Format("Json",None)
          }
        }
      println("format:" + format)
    }
    println("loadByTimeTrigger:")
    val ltt = apirssinfovw.loadByTimeTrigger
    for (elem <- ltt) {
      println("ltt:" + elem)
    }
    println("loadByTimeTrigger.")

  }



}
