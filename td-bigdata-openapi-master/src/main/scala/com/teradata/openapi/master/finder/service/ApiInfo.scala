package com.teradata.openapi.master.finder.service

import com.teradata.openapi.framework.model.{ApiInfoRow, ToolApiInfoRow}
import com.teradata.openapi.master.deploy.dao.ToolApiInfoDao
import com.teradata.openapi.master.finder.dao.StructApiArgDao

/**
  * Created by John on 2016/7/12.
  */
class ApiInfo(apiinfo :Seq[ApiInfoRow]) {
  def getToolApiinfos: Seq[ApiInfoRow] = {
    apiinfo
  }
  def reloadToolApiinfos: Seq[ApiInfoRow] = apiinfo
}

object ApiInfo {
  val structApiArg = StructApiArgDao()
  val apiinfo: Seq[ApiInfoRow] = structApiArg.loadApiInfo
  def apply() = new ApiInfo(apiinfo)
}
