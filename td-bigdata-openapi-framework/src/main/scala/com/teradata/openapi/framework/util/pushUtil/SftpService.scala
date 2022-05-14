package com.teradata.openapi.framework.util.pushUtil

import java.io.{File, FileInputStream, FileOutputStream, InputStream}
import java.util
import java.util.Properties

import com.jcraft.jsch._
import com.teradata.openapi.framework.util.pluginUtil.{HDFSPathHelper, PluginEnv}
import org.apache.hadoop.fs.FSDataInputStream

import scala.io.Source

/**
  * Created by John on 2016/9/23.
  */
class SftpService extends FileTransferService{
  private var session: Session = null
  private var channel: Channel = null
  private var sftp: ChannelSftp = null


  @throws[Exception]
  override def chkConnect(host: String, port: Int, username: String, password: String, ftpmode: String, directory: String): Boolean = {
    log.debug("testTransSourceFullFile:[" + testTransSourceFullFile + "]")
    log.debug("testTransSourceFileName:[" + testTransSourceFileName + "]")
    try {
      getConnect(host, port, username, password, ftpmode, directory)
      upload(directory, testTransSourceFullFile, testTransSourceFileName)
      delete(directory, testTransSourceFileName)
      true
    } catch {
      case e: Exception => throw e
    } finally {
      disConn
    }
  }
  /**
    * 连接ftp/sftp服务器
    *
    * @param host
    * @param port
    * @param username
    * @param password
    * @param ftpmode
    * @param directory
    * @throws Exception
    */
  @throws[Exception]
  def getConnect(host: String, port: Int, username: String, password: String, ftpmode: String, directory: String) {
    val jsch: JSch = new JSch
    session = jsch.getSession(username, host, port)
    session.setPassword(password)
    val config: Properties = new Properties
    config.put("StrictHostKeyChecking", "no")
    session.setConfig(config)
    try {
      session.connect()
    }
    catch {
      case e: Exception => {
        if (session.isConnected) session.disconnect()
        throw new Exception("连接服务器失败,请检查主机[" + host + "],端口[" + port + "],用户名[" + username + "],端口[" + port + "]是否正确,以上信息正确的情况下请检查网络连接是否正常或者请求被防火墙拒绝.")
      }
    }
    channel = session.openChannel("sftp")
    try {
      channel.connect()
    }
    catch {
      case e: Exception => {
        if (channel.isConnected) channel.disconnect()
        throw new Exception("连接服务器失败,请检查主机[" + host + "],端口[" + port + "],用户名[" + username + "],密码[" + password + "]是否正确,以上信息正确的情况下请检查网络连接是否正常或者请求被防火墙拒绝.")
      }
    }
    sftp = channel.asInstanceOf[ChannelSftp]
    log.debug("Connect DIR:" + directory)
    if (directory != null && directory.trim != "") {
      try {
        sftp.cd(directory)
        log.debug("Remote DIR:" + sftp.pwd())
      }
      catch {
        case sException: SftpException => {
          sException.printStackTrace()
          if (ChannelSftp.SSH_FX_NO_SUCH_FILE == sException.id) {
            sftp.mkdir(directory)
            sftp.cd(directory)
          }
        }
      }
    }
  }
  /**
    * 断开ftp/sftp服务器连接
    * @throws Exception
    */
  @throws[Exception]
  def disConn {
    if (null != sftp) {
      sftp.disconnect()
      sftp.exit()
      sftp = null
    }
    if (null != channel) {
      channel.disconnect()
      channel = null
    }
    if (null != session) {
      session.disconnect()
      session = null
    }
  }
  /**
    * 上传文件
    * @param directory 本地文件目录
    * @param uploadLocalFile 上传路径
    * @param targetRemoteFile 上传后名称
    * @throws Exception
    */
  @throws[Exception]
  def upload(directory: String, uploadLocalFile: String, targetRemoteFile: String) {
    try {
      val file: File = new File(uploadLocalFile)
      sftp.put(new FileInputStream(file), targetRemoteFile)
    }
    catch {
      case e: Exception => {
        throw new Exception(e.getMessage, e)
      }
    }
  }


  @throws[Exception]
  def uploadFromHDFS(directory: String, uploadLocalFile: String, targetRemoteFile: String) {
    try {
      val input: FSDataInputStream = HDFSPathHelper.loadInputStreamFromHDFS(uploadLocalFile, PluginEnv.cacheFileSystem)
      try {
        log.debug("Is the file " + targetRemoteFile + " exists ?")
        val filePermisson = sftp.lstat(targetRemoteFile).getPermissionsString
        log.info("filePermission:" + filePermisson)
        if (filePermisson.nonEmpty) sftp.rm(targetRemoteFile)
      } catch {
        case e: Exception =>
          log.info("The file " + targetRemoteFile + " don't exits.")
      }
      sftp.put(input, targetRemoteFile)
    }
    catch {
      case e: Exception => {
        throw new Exception(e.getMessage, e)
      }
    }
  }
  /**
    * 下载文件
    * @param directory
    * @param downloadFile
    * @param saveFile
    * @throws Exception
    */
  @throws[Exception]
  def download(directory: String, downloadFile: String, saveFile: String) {
    try {
      sftp.cd(directory)
      val file: File = new File(saveFile)
      var bFile: Boolean = false
      bFile = false
      bFile = file.exists
      if (!bFile) {
        bFile = file.mkdirs
      }
      val downLoadStream: FileOutputStream = new FileOutputStream(new File(saveFile, downloadFile))
      sftp.get(downloadFile, downLoadStream)
      downLoadStream.close
    }
    catch {
      case e: Exception => {
        e.printStackTrace()
        throw new Exception(e.getMessage, e)
      }
    }
  }
  /**
    * 删除文件
    * @param directory
    * @param deleteFile
    * @throws Exception
    */
  @throws[Exception]
  def delete(directory: String, deleteFile: String) {
    try {
      sftp.cd(directory)
      sftp.rm(deleteFile)
    }
    catch {
      case e: Exception => {
        throw new Exception(e.getMessage, e)
      }
    } finally {
      disConn
    }
  }
  /**
    * 获取远端路径中文件/文件夹列表
    * @param directory
    * @throws Exception
    * @return
    */
  @throws[Exception]
  def remoteList(directory: String): List[String] = {
    val vectors: util.Vector[_] = sftp.ls(directory)
    var filenamelist: List[String] = List[String]()
    import scala.collection.JavaConversions._
    for (vectorName <- vectors) {
      filenamelist = filenamelist.+:(vectorName.toString)
    }
    filenamelist
  }
}
