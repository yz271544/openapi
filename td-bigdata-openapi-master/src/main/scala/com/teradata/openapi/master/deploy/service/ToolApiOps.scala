package com.teradata.openapi.master.deploy.service

import com.teradata.openapi.framework.model.ToolApiInfoRow
import com.teradata.openapi.master.deploy.dao.ToolApiInfoDao

/**
  * Created by John on 2016/7/12.
  */
class ToolApiOps(toolApiinfo :Seq[ToolApiInfoRow]) {
  def getToolApiinfos: Seq[ToolApiInfoRow] = {
    toolApiinfo
  }
}
object ToolApiOps {
  val toolApiInfoDao = ToolApiInfoDao()
  val toolApiinfo: Seq[ToolApiInfoRow] = toolApiInfoDao.loadAll
  def apply() = new ToolApiOps(toolApiinfo)
}
