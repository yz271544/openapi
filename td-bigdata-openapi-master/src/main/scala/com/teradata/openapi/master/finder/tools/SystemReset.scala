package com.teradata.openapi.master.finder.tools

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.deploy.{ReLoad, ReLoadType, ReqArg}
import com.teradata.openapi.framework.message.response.FindToReqAsynYIK
import com.teradata.openapi.master.deploy.Master
/**
  * Created by John on 2016/10/19.
  */
class SystemReset extends ToolApis with OpenApiLogging{
  override def execute(reqID: String, master: Master, reqArgs: List[ReqArg]): FindToReqAsynYIK = {
    val ret = new FindToReqAsynYIK()
    ret.setReqID(reqID)
    var reSetType: String = ""
    try {
      reSetType = (for (elem <- reqArgs if elem.fieldName.toUpperCase equals "RESETTYPE") yield elem.fieldValue).flatten.head.toString
    } catch {
      case e:Exception => log.error("get retSetType Exception:" + e.getStackTrace)
    }
    try {
      reSetType.toUpperCase match {
        case "SYSARG" => master.self ! ReLoad
        case "FORMATFINGER" => master.finderActor ! ReLoadType(reSetType)
        case "DATAFINGER" => master.finderActor ! ReLoadType(reSetType)
        case "SOURCEIDS" => master.finderActor ! ReLoadType(reSetType)
        case "APITABINFOS" => master.finderActor ! ReLoadType(reSetType)
        case "SNAPSHOTDIC" => master.finderActor ! ReLoadType(reSetType)
        case "TOOLAPIINFOS" => master.finderActor ! ReLoadType(reSetType)
        case "CACHE" => master.cacheActor ! ReLoad
        case "SCHEDULE" => master.scheduleActor ! ReLoad
        case "CONTROLLER" => master.controller ! ReLoad
        case "RESOLVER" => master.resolverActor ! ReLoad
        case "ALL" => {
          master.self ! ReLoad
          master.finderActor ! ReLoadType(reSetType)
          master.cacheActor ! ReLoad
          master.scheduleActor ! ReLoad
          master.controller ! ReLoad
          master.resolverActor ! ReLoad
        }
        case _ => {
          ret.setRetCode("-1")
          ret.setRetMsg("SystemReset:" + reSetType.toUpperCase)
          ret.setReqStat("Unknown parameters that cannot be resolved.")
        }
      }
      ret.setRetCode("0")
      ret.setRetMsg("SystemReset:" + reSetType.toUpperCase)
      ret.setReqStat("reset successed!...")
    } catch {
      case e: Exception => {
        ret.setRetCode("-1")
        ret.setRetMsg("SystemReset:" + reSetType.toUpperCase)
        ret.setReqStat("reset failed!...")
      }
    }
    ret
  }
}
