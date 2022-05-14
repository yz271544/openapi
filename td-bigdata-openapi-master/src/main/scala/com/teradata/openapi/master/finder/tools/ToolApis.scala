package com.teradata.openapi.master.finder.tools

import com.teradata.openapi.framework.deploy.ReqArg
import com.teradata.openapi.framework.message.response.FindToReqAsynYIK
import com.teradata.openapi.master.deploy.Master

/**
  * Created by John on 2016/7/12.
  */
trait ToolApis {
  def execute(reqID:String, master: Master,msg :List[ReqArg]) :FindToReqAsynYIK
}
