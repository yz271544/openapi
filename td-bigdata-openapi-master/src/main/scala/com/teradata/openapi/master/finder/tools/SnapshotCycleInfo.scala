package com.teradata.openapi.master.finder.tools

import com.teradata.openapi.framework.deploy.ReqArg
import com.teradata.openapi.framework.message.response.FindToReqAsynYIK
import com.teradata.openapi.framework.util.DicMapFunc
import com.teradata.openapi.master.deploy.Master

/**
  * Created by John on 2016/7/12.
  */
class SnapshotCycleInfo extends ToolApis with DicMapFunc{
  override def execute(reqID:String, master: Master, reqArgs: List[ReqArg]): FindToReqAsynYIK = {
    val ret = new FindToReqAsynYIK()
    ret.setReqID(reqID)


    val apiId: Int = (for (elem <- reqArgs if elem.fieldName.toUpperCase equals "API_ID") yield elem.fieldValue).flatten.head.toString.toInt
    val api_version: Int = (for (elem <- reqArgs if elem.fieldName.toUpperCase equals "API_VERSION") yield elem.fieldValue).flatten.head.toString.toInt
    val columnName: String = (for (elem <- reqArgs if elem.fieldName.toUpperCase equals "COLUMNNAME") yield elem.fieldValue).flatten.head.toString
    val getType: String = (for (elem <- reqArgs if elem.fieldName.toUpperCase equals "GETTYPE") yield elem.fieldValue).flatten.head.toString
    val listSnapshotInfos: List[Any] = master.finderActorIns.finderDataSnapshotDicInfo.listSnapshot(apiId,api_version,columnName,getType)

    ret.setRetCode("0")
    ret.setRetMsg(listSnapshotInfos.toString)
    ret.setReqStat("get snapshot info successed!...")

    ret
  }
}
