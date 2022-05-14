package com.teradata.openapi.framework.plugin

import java.io.File

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.step.CleanFileStep
import com.teradata.openapi.framework.util.pluginUtil.PluginEnv

/**
  * Created by hdfs on 2016/7/8.
  */
class CleanFpsPlugin extends AsyncTaskPlugin[CleanFileStep] with OpenApiLogging {

  private var step: CleanFileStep = _
  private var template: String = _
  private var fpPath: String = _

  def deleteFp(fpId: String): Unit = {

    this.template.replaceAll("\\$\\{fpPath\\}", this.fpPath).replaceAll("\\$\\{fpId\\}", fpId).split('|').foreach { fileName =>
      val storeFile = new File(fileName.trim)
      if (storeFile.exists) {
        log.debug("CleanFs file [" + storeFile + "]")
        org.apache.commons.io.FileUtils.forceDelete(storeFile)
      }
    }
  }

  def deleteFpList() {
    this.step.finger.foreach {
      fpFile =>
        if (!fpFile.isEmpty)
          deleteFp(fpFile)
    }
  }

  override def init(step: CleanFileStep, template: String, env: Map[String, Any]): Unit = {
    log.debug("CleanFs Plugin's initiation function is starting ...")
    this.step = step
    this.template = template
//    this.fpPath = PluginEnv.plugFrmtTargetPath
//      log.debug(BaseUtil.getName("/", None, None, None))
      this.fpPath = PluginEnv.plugFrmtTargetDir
    log.debug("CleanFs Plugin's initiation function is finished ...")
  }

  override def execute(): Int = {
    log.debug("CleanFs Plugin's exceute function is starting... ")
    try {
      deleteFpList()
    } catch {
      case e: Exception =>
        log.warn("Couldn't del fp  ." + e.getMessage)
        throw e
    }
    log.debug("CleanFs Plugin's exceute function is finished... ")
    0
  }
}
