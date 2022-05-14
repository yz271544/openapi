package com.teradata.openapi.master.finder.tools

import com.codahale.jerkson.{Json, ParsingException}
import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.deploy.{DownLoadAttrArgs, Format, ReqArg}
import com.teradata.openapi.framework.message.response.FindToReqAsynYIK
import com.teradata.openapi.framework.util.DicMapFunc
import com.teradata.openapi.master.deploy.Master
import com.teradata.openapi.master.finder.dao.ReqInfoVwDao

/**
  * Created by John on 2016/8/23.
  */
class GetAsynTaskData extends ToolApis with DicMapFunc with OpenApiLogging{
  override def execute(reqID: String, master: Master, reqArgs: List[ReqArg]): FindToReqAsynYIK = {
    val ret = new FindToReqAsynYIK()
    ret.setReqID(reqID)
    var chkReqID: String = ""
    var downLoadFlag:String ="N"
    try {
      chkReqID = (for (elem <- reqArgs if elem.fieldName.toUpperCase equals "REQID") yield elem.fieldValue).flatten.head.toString
      downLoadFlag = (for (elem <- reqArgs if elem.fieldName.toUpperCase equals "DOWNLOADFLAG") yield elem.fieldValue).flatten.head.toString
    } catch {
      case e:Exception => log.error("get chkReqID Exception:" + e.getStackTrace)
    }
    log.debug("chkReqID:" + chkReqID)
    val reqInfos = ReqInfoVwDao()

    var reqStat = -1
    var formCode = ""
    var retn_form_finger = ""

    try {
      //(reqStat, formCode, retn_form_finger)
      val crs: (Int, String, String) = reqInfos.chkReqStat(chkReqID).head
      reqStat = crs._1
      formCode = Json.parse[Format](crs._2).formType
      retn_form_finger = crs._3
    } catch {
      case e:UnsupportedOperationException => log.error("get chkReqStat Exception:" + e.getStackTrace)
      case e:ParsingException => log.error("parse formCode Exception:" + e.getStackTrace)
      case e:Exception => log.error("fatal error! " + e.getStackTrace)
    }
    val downloadAttrArgs: DownLoadAttrArgs = DownLoadAttrArgs(formCode,retn_form_finger)
    ret.setReqStat(Json.generate(downloadAttrArgs))

    val retCode = if (downLoadFlag equals "Y") {
      reqStat match {
        case 0 => "DownLoad"
        case -1 => "Doing"
        case _ => "Exception"
      }
    } else {
      reqStat match {
        case 0 => "Down"
        case -1 => "Doing"
        case _ => "Exception"
      }
    }

    ret.setRetCode(retCode)
    val retMsg = reqStat match {
      case 0 => "完成"
      case -1 => "运行中"
      case _ => "运行异常，请重新发起取数请求"
    }
    ret.setRetMsg(retMsg)
    log.debug("{}|接收到GetAsynTaskData请求:getRetMsg:{},getRetCode:{},getReqStat:{}",reqID,ret.getRetMsg,ret.getRetCode,ret.getReqStat)
    ret
  }
}
