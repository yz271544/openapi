package com.teradata.openapi.framework.plugin

import com.codahale.jerkson.Json
import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.deploy.{Format, PushArgs, TriggerInfo}
import com.teradata.openapi.framework.step.PushStep
import com.teradata.openapi.framework.util.DateUtils
import com.teradata.openapi.framework.util.pluginUtil.{BaseUtil, HDFSPathHelper, PluginEnv, SuffixType}
import com.teradata.openapi.framework.util.pushUtil.{FileTransferService, FtpService, SftpService}
import org.apache.hadoop.fs.Path

import scala.collection.immutable.TreeMap
import scala.util.matching.Regex

/**
  * Created by hdfs on 2016/7/8.
  */
private[openapi] class PushPlugin extends AsyncTaskPlugin[PushStep] with OpenApiLogging {

  var fileFormatType: String = _
  var formatFinger: String = _
  var pushPluginType: String = _
  var host: String = _
  var port: Int = _
  var username: String = _
  var password: String = _
  var ftpmode: String = _
  var ftpTargetPath: String = _
  var fileTransferService: FileTransferService = _
  var sourceDataFullFileList: List[(String, String)] = _
  var sourceCheckFullFile: String = _
  var targetCheckFileName: String = _
  var checkFileNameModel: String = _
  var dataFileNameModel: String = _
  var attachmentsFileNames: List[(String, String)] = _
  var dataDate: String = "None"

  /*  def getOutDir(retFormatFinger: String): String = {
      if (PluginEnv.plugFrmtTargetPath.endsWith(PluginEnv.plugFileDelimiter)) {
        PluginEnv.plugFrmtTargetPath + this.fileFormatType + PluginEnv.plugFileDelimiter + retFormatFinger
      } else {
        PluginEnv.plugFrmtTargetPath + PluginEnv.plugFileDelimiter + this.fileFormatType + PluginEnv.plugFileDelimiter + retFormatFinger
      }
    } */

  def getOutDir(retFormatFinger: String): String = {
    BaseUtil.getName(retFormatFinger, Some(BaseUtil.getTPathType(this.fileFormatType)), None, None)
  }

  def getDataFileList(): Unit = {
    /*    val sourceChkDir = String.format("%s/%s", PluginEnv.plugFrmtTargetPath, this.fileFormatType)
        val sourceChkFile = String.format("%s.chk", this.formatFinger)
        sourceCheckFullFile = String.format("%s/%s", sourceChkDir, sourceChkFile)
        */
    val sourceChkDir = BaseUtil.getName(this.formatFinger, Some(BaseUtil.getTPathType(this.fileFormatType)), None, None)
    sourceCheckFullFile = BaseUtil.getName(this.formatFinger, Some(BaseUtil.getTPathType(this.fileFormatType)), None, Some(SuffixType.CHK))
    log.debug("sourceCheckFullFile:" + sourceCheckFullFile)
    //val file: File = new File(sourceCheckFullFile)
    //val chkFullFile = String.format("%s/%s", sourceChkDir, sourceCheckFullFile)
    val chkFullFile = String.format("%s", sourceCheckFullFile)
    var sourceDataFullFileListTmp = List[(String, String)]()
    //if (file.exists) {
    if (HDFSPathHelper.exists(chkFullFile, PluginEnv.cacheFileSystem)) {
      //log.debug("The file " + file.getAbsoluteFile + " is exists!")
      log.debug("The file " + chkFullFile + " is exists!")
      try {
        //val ti: Iterator[String] = Source.fromFile(sourceCheckFullFile).getLines()
        val ti: Iterator[String] = HDFSPathHelper.getHDFSFileContextLines(new Path(chkFullFile), PluginEnv.cacheFileSystem)
        log.debug("Iterator ti: " + ti)
        var fileNum = 1
        while (ti.hasNext) {
          val line: String = ti.next()
          if (line.trim.length > 0) {
            val contextFileFullName: String = line.split("\\s+")(0)
            //val dataDate = line.split("\\s+")(2)
            val targetFileName: String = regexDataFileName(this.dataFileNameModel, dataDate, fileNum)
            log.debug("targetFileName:" + targetFileName)
            sourceDataFullFileListTmp = sourceDataFullFileListTmp :+ (contextFileFullName, targetFileName)
          }
          fileNum += 1
        }
      }
      catch {
        case e: Exception => {
          log.error("获取数据文件列表失败:" + e.printStackTrace + e)
          throw e
        }
      }
    }
    else {
      //log.debug("The file " + file.getName + " don't exists!")
      log.debug("The file " + chkFullFile + " don't exists!")
    }
    this.sourceDataFullFileList = sourceDataFullFileListTmp
    if ("txt" equals this.fileFormatType.toLowerCase) {
      log.debug("fileFormatType:" + fileFormatType)
      var attachmentsFileNamesTmp = List[(String, String)]()


      //val attachmentsFile = String.format("%s.til", this.formatFinger)
      //val attachmentsFullFile = String.format("%s/%s", sourceChkDir, attachmentsFile)
      val attachmentsFullFile = BaseUtil.getName(this.formatFinger, Some(BaseUtil.getTPathType(this.fileFormatType)), None, Some(SuffixType.TIL))
      //val file: File = new File(attachmentsFullFile)
      //if (file.exists()) {
      if (HDFSPathHelper.exists(attachmentsFullFile, PluginEnv.cacheFileSystem)) {
        log.info("The til file:" + attachmentsFullFile + "is exists.")
        val targetTilFileName = String.format("%s.til", this.targetCheckFileName)
        attachmentsFileNamesTmp = attachmentsFileNamesTmp :+ (attachmentsFullFile, targetTilFileName)
      } else {
        log.info("The attachmentsFullFile:" + attachmentsFullFile + " don't exits.")
      }
      this.attachmentsFileNames = attachmentsFileNamesTmp
    } else {
      this.attachmentsFileNames = List[(String, String)]()
    }
    log.debug("sourceDataFullFileList:" + sourceDataFullFileList)
    log.debug("attachmentsFileNames:" + attachmentsFileNames)
  }

  def regexDataFileName(fileNameModel: String, dataDate: String, dataNum: Int): String = {
    val sDataNum: String = "_" + "%03d".format(dataNum)

    val varPattern = new Regex("""(\w+)(\$true|\$false)(\$true|\$false)(.\w+)(\$true|\$false)(\$true|\$false)""", "pword", "perfixDate", "perfixNum", "sword", "suffixDate", "suffixNum")

    val targetFileName = varPattern replaceAllIn(fileNameModel, m => s"${m group "pword"}${if ((m group "perfixDate") equals """$true""") dataDate else ""}${if ((m group "perfixNum") equals """$true""") sDataNum else ""}${m group "sword"}${if ((m group "suffixDate") equals """$true""") dataDate else ""}${if ((m group "suffixNum") equals """$true""") sDataNum else ""}")

    targetFileName
  }

  def regexChkFileName(fileNameModel: String, dataDate: String): String = {
    val varPattern = new Regex("""(\w+)(\$true|\$false)(.\w+)(\$true|\$false)""", "pword", "perfixDate", "sword", "suffixDate")

    val targetFileName = varPattern replaceAllIn(fileNameModel, m => s"${m group "pword"}${if ((m group "perfixDate") equals """$true""") dataDate else ""}${m group "sword"}${if ((m group "suffixDate") equals """$true""") dataDate else ""}")

    targetFileName
  }

  override def init(step: PushStep, template: String, env: Map[String, Any]): Unit = {
    env.foreach(i => log.debug("Key:" + i._1 + " Value:" + i._2.toString))
    this.fileFormatType = step.format.getOrElse(Format(formType = "JSON", formDetail = None)).formType.toString.toLowerCase
    this.formatFinger = step.retFormatFinger
    this.pushPluginType = step.pushArgs.getOrElse(PushArgs(ftpHost = "None", ftpType = "None", ftpMode = "None", ftpPath = "None", ftpPort = 0, ftpProtocol = "None", isConnect = false, userName = "None", password = "None", checkFileName = Some("None"), dataFileName = Some("None"))).ftpType
    this.fileTransferService = this.pushPluginType match {
      case "ftp" => new FtpService()
      case "sftp" => new SftpService()
      case _ => null
    }

    this.host = step.pushArgs.getOrElse(PushArgs(ftpHost = "None", ftpType = "None", ftpMode = "None", ftpPath = "None", ftpPort = 0, ftpProtocol = "None", isConnect = false, userName = "None", password = "None", checkFileName = Some("None"), dataFileName = Some("None"))).ftpHost
    this.port = step.pushArgs.getOrElse(PushArgs(ftpHost = "None", ftpType = "None", ftpMode = "None", ftpPath = "None", ftpPort = 0, ftpProtocol = "None", isConnect = false, userName = "None", password = "None", checkFileName = Some("None"), dataFileName = Some("None"))).ftpPort
    this.username = step.pushArgs.getOrElse(PushArgs(ftpHost = "None", ftpType = "None", ftpMode = "None", ftpPath = "None", ftpPort = 0, ftpProtocol = "None", isConnect = false, userName = "None", password = "None", checkFileName = Some("None"), dataFileName = Some("None"))).userName
    this.password = step.pushArgs.getOrElse(PushArgs(ftpHost = "None", ftpType = "None", ftpMode = "None", ftpPath = "None", ftpPort = 0, ftpProtocol = "None", isConnect = false, userName = "None", password = "None", checkFileName = Some("None"), dataFileName = Some("None"))).password
    this.ftpmode = step.pushArgs.getOrElse(PushArgs(ftpHost = "None", ftpType = "None", ftpMode = "None", ftpPath = "None", ftpPort = 0, ftpProtocol = "None", isConnect = false, userName = "None", password = "None", checkFileName = Some("None"), dataFileName = Some("None"))).ftpMode
    this.ftpTargetPath = step.pushArgs.getOrElse(PushArgs(ftpHost = "None", ftpType = "None", ftpMode = "None", ftpPath = "None", ftpPort = 0, ftpProtocol = "None", isConnect = false, userName = "None", password = "None", checkFileName = Some("None"), dataFileName = Some("None"))).ftpPath
    this.checkFileNameModel = step.pushArgs.getOrElse(PushArgs(ftpHost = "None", ftpType = "None", ftpMode = "None", ftpPath = "None", ftpPort = 0, ftpProtocol = "None", isConnect = false, userName = "None", password = "None", checkFileName = Some("None"), dataFileName = Some("None"))).checkFileName.get
    this.dataFileNameModel = step.pushArgs.getOrElse(PushArgs(ftpHost = "None", ftpType = "None", ftpMode = "None", ftpPath = "None", ftpPort = 0, ftpProtocol = "None", isConnect = false, userName = "None", password = "None", checkFileName = Some("None"), dataFileName = Some("None"))).dataFileName.get

    val triggerInfos: Option[Any] = env.get("TriggerInfo")
    log.debug("triggerInfos:" + triggerInfos)
    val triggerInfosGetval: String = triggerInfos match {
      case e: Some[Any] => {
        e.get.toString
      }
      case _ => {
        Json.generate(TriggerInfo(trigger_methd = 0, Json.generate(TreeMap("None" -> List("None"))), rss_id = 0)).toString
      }
    }
    log.debug("triggerInfosGetval:" + triggerInfosGetval)
    val triggerInfoObj = Json.parse[TriggerInfo](triggerInfosGetval)
    val triggerInfoMap = Json.parse[Map[String, Any]](triggerInfoObj.trigger_sorc)
    log.debug("triggerInfoMap:" + triggerInfoMap)
    dataDate = triggerInfoMap.values.head.toString
    log.debug("chkDataDate:" + dataDate)
    dataDate = if (dataDate equals "None") DateUtils.getDate("yyyyMMdd") else dataDate
    log.debug("This DataDate:" + this.dataDate)
    this.targetCheckFileName = regexChkFileName(checkFileNameModel, dataDate)
    getDataFileList()
    log.debug("Push Plugin's initiation function is finished ...")
  }

  def transferTestService() = {
    this.fileTransferService.chkConnect(host, port, username, password, ftpmode, ftpTargetPath)
  }

  override def execute(): Int = {

    try {
      this.fileTransferService.getConnect(host, port, username, password, ftpmode, ftpTargetPath)
      log.info("数据文件清单:" + sourceDataFullFileList)
      if (sourceDataFullFileList.nonEmpty) {
        for (elem: (String, String) <- sourceDataFullFileList if elem._1.trim.nonEmpty) {
          try {
            log.info("上传数据文件:" + elem._1 + " to " + elem._2)
            this.fileTransferService.uploadFromHDFS(ftpTargetPath, elem._1, elem._2)
            log.info("上传数据文件成功:" + elem)
          } catch {
            case e: Exception => {
              log.error("上传数据文件失败:[" + elem + "] to Dir:" + ftpTargetPath)
              throw e
            }
          }
        }
      }
      log.debug("attachments number:" + attachmentsFileNames.length)
      if (attachmentsFileNames.nonEmpty) {
        log.info("上传附件:" + attachmentsFileNames)
        try {
          for (elem <- attachmentsFileNames) {
            this.fileTransferService.uploadFromHDFS(ftpTargetPath, elem._1, elem._2)
            log.info("上传附件文件成功:" + elem)
          }
        } catch {
          case e: Exception => {
            log.error("上传附件文件失败:[" + attachmentsFileNames + "] to Dir:" + ftpTargetPath)
            throw e
          }
        }
      }
      try {
        log.info("上传校验文件:" + sourceCheckFullFile + " targetCheckFileName:" + targetCheckFileName)
        this.fileTransferService.uploadFromHDFS(ftpTargetPath, sourceCheckFullFile, targetCheckFileName)
        log.info("上传校验文件成功:" + targetCheckFileName)
      } catch {
        case e: Exception => {
          log.error("上传校验文件失败:[" + sourceCheckFullFile + "] to Dir:" + ftpTargetPath + "/" + targetCheckFileName)
          throw e
        }
      }
    } catch {
      case e: Exception => {
        log.error("连接失败:[" + pushPluginType + "] host:" + host + " port:" + port + " username:" + username)
        throw e
      }
    } finally {
      this.fileTransferService.disConn
    }
    log.debug("Push Plugin's exceute function is finished... ")
    0
  }
}
