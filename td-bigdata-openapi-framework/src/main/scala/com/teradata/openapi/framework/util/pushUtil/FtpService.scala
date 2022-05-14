package com.teradata.openapi.framework.util.pushUtil

import java.io._

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.util.pluginUtil.{HDFSPathHelper, PluginEnv}
import org.apache.commons.net.ftp.{FTPClient, FTPFile, FTPReply}
import org.apache.hadoop.fs.FSDataInputStream

/**
  * Created by John on 2016/9/23.
  */

class FtpService extends FileTransferService with OpenApiLogging{
  private var ftp: FTPClient = null
  //	private static String encoding = "";

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
    try {
      ftp = new FTPClient
      ftp.connect(host, port)
      ftp.login(username, password)
      if ("passive" equals ftpmode) {
        ftp.enterLocalPassiveMode()
      }else {
        ftp.enterLocalActiveMode()
      }
      val reply: Int = ftp.getReplyCode
      if (!FTPReply.isPositiveCompletion(reply)) {
        disConn
        throw new IOException
      }
    } catch {
      case e: Exception => {
        println(e.getMessage + e)
        log.error(e.getMessage + e)
        throw new Exception("连接ftp服务器失败,请检查主机[" + host + "],端口[" + port + "],用户名[" + username + "],密码[" + password + "]是否正确,以上信息正确的情况下请检查网络连接是否正常或者请求被防火墙拒绝.")
      }
    }
    try {
      log.debug("i want connect directory:" + directory)
      ftp.changeWorkingDirectory(directory)
    } catch {
      case e: Exception => {
        log.debug("Don't connect directory:" + directory)
        log.error(e.getMessage + " exceptionOBJ:" + e)
        if (directory != null && "" != directory.trim) {
          val pathes: Array[String] = directory.split("/|\\\\")
          for (onepath <- pathes if onepath != null && onepath != "") {
            if (!ftp.changeWorkingDirectory(onepath)) {
              ftp.makeDirectory(onepath)
              ftp.changeWorkingDirectory(onepath)
            }
          }
        }
      }
    }
  }
  /**
    * 断开ftp/sftp服务器连接
    *
    * @throws Exception
    */
  @throws[Exception]
  def disConn {
    try {
      if (ftp.isConnected) {
        ftp.logout
        ftp.disconnect()
      }
    }
    catch {
      case e: Exception => {
        throw new Exception("断开ftp连接失败")
      }
    }
  }
  /**
    * 从本地文件系统上传文件
    *
    * @param directory
    * @param uploadLocalFile
    * @param targetRemoteFile
    * @throws Exception
    */
  @throws[Exception]
  def upload(directory: String, uploadLocalFile: String, targetRemoteFile: String) {
    log.info("upload -> directory:" + directory)
    log.info("upload -> uploadLocalFile:" + uploadLocalFile)
    log.info("upload -> targetRemoteFile:" + targetRemoteFile)
    try {
      val file: File = new File(uploadLocalFile)
      val input: FileInputStream = new FileInputStream(file)
      ftp.storeFile(targetRemoteFile, input)
      input.close()
    }
    catch {
      case e: Exception => {
        e.printStackTrace()
        throw new Exception("文件上传失败！Please try the passive mode" + e.getMessage)
      }
    }
  }

  /**
    * 从本地文件系统上传文件
    *
    * @param directory
    * @param uploadLocalFile
    * @param targetRemoteFile
    * @throws Exception
    */
  @throws[Exception]
  def uploadFromHDFS(directory: String, uploadLocalFile: String, targetRemoteFile: String) {
    try {
      val input: FSDataInputStream = HDFSPathHelper.loadInputStreamFromHDFS(uploadLocalFile, PluginEnv.cacheFileSystem)
      try {
        val hasFile = ftp.listNames(targetRemoteFile)
        log.info("hasFile:" + hasFile)
        if (ftp.listNames(targetRemoteFile).length > 0) ftp.deleteFile(targetRemoteFile)
      } catch {
        case e: Exception =>
          log.info("The file " + targetRemoteFile + " don't exits.")
      }
      ftp.storeFile(targetRemoteFile, input)
      input.close()
    }
    catch {
      case e: Exception => {
        e.printStackTrace()
        throw new Exception("文件上传失败！Please try the passive mode" + e.getMessage)
      }
    }
  }

  /**
    * 下载文件
    *
    * @param directory
    * @param downloadFile
    * @param saveFile
    * @throws Exception
    */
  @throws[Exception]
  def download(directory: String, downloadFile: String, saveFile: String) {
    println("download directory:" + directory + " downloadFile:" + downloadFile + " saveDir:" + saveFile)
    log.info("download directory:" + directory + " downloadFile:" + downloadFile + " saveDir:" + saveFile)
    try {
      ftp.changeWorkingDirectory(directory)
      val fs: Array[FTPFile] = ftp.listFiles
      var hasFile: Boolean = false
      for (ff <- fs) {
        if (ff.getName == downloadFile) {
          hasFile = true
          val localFile: File = new File(saveFile)
          if (!localFile.exists) {
            localFile.mkdirs
          }
          val is: OutputStream = new FileOutputStream(localFile + "/" + downloadFile)
          ftp.retrieveFile(ff.getName, is)
          is.close()
        }
      }
      if (!hasFile) {
        throw new Exception("没有在指定目录" + directory + "找到需要下载的文件" + downloadFile)
      }
    }
    catch {
      case e: Exception => {
        throw new Exception(e)
      }
    }
  }
  /**
    * 删除文件
    *
    * @param directory
    * @param deleteFile
    * @throws Exception
    */
  @throws[Exception]
  def delete(directory: String, deleteFile: String) {
    try {
      if (!ftp.deleteFile(directory + "/" + deleteFile)) {
        throw new Exception("删除文件失败，原因为" + ftp.getReplyString)
      }
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
    *
    * @param directory
    * @throws Exception
    * @return
    */
  @throws[Exception]
  def remoteList(directory: String): List[String] = {
    val listfiles: Array[String] = ftp.listNames
    val filenamelist: List[String] = listfiles.toList
    filenamelist
  }
}
