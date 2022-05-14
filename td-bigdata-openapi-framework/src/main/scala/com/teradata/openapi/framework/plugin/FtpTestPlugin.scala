package com.teradata.openapi.framework.plugin

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.step.FtpTest
import com.teradata.openapi.framework.util.pluginUtil.PluginEnv
import com.teradata.openapi.framework.util.pushUtil.{FileTransferService, FtpService, SftpService}

/**
  * Created by John on 2016/8/15.
  */
@SerialVersionUID(1L)
class FtpTestPlugin extends SyncTaskPlugin[FtpTest] with OpenApiLogging {
  var pushPluginType: String = _
  var host: String = _
  var port: Int = _
  var username: String = _
  var password: String = _
  var ftpmode: String = _
  var directory: String = _
  var fileTransferService: FileTransferService = _
  //初始化参数
  override def init(step: FtpTest, template: String, env: Map[String, Any]): Unit = {
    this.pushPluginType = step.pushPluginType
    this.host = step.host
    this.port = step.port
    this.username = step.username
    this.password = step.password
    this.ftpmode = step.ftpmode
    this.directory = step.directory

    this.fileTransferService = this.pushPluginType match {
      case "ftp" => new FtpService()
      case "sftp" => new SftpService()
      case _ => null
    }


    val testTransSourceDir: String = PluginEnv.ftpTestPushDir
    val testTransSourceFileName: String = PluginEnv.ftpTestPushFileName
    val testTransSourceFullFile: String = testTransSourceDir + "/" + testTransSourceFileName

    log.debug("testTransSourceDir:[" + testTransSourceDir + "]")
    log.debug("testTransSourceFileName:[" + testTransSourceFileName + "]")
    log.debug("testTransSourceFullFile:[" + testTransSourceFullFile + "]")


  }

  //执行 def execute() 直接使用父类接口中的方法
  @throws[Exception]
  override def execute(): String = {
    var testTransConnChk: Boolean = false
    try {
      testTransConnChk = transferTestService()
    } catch {
      case e: Exception => throw e
    }
    testTransConnChk.toString
  }


  def transferTestService(): Boolean = {
    this.fileTransferService.chkConnect(host, port, username, password, ftpmode, directory)
  }

}
