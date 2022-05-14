package com.teradata.openapi.framework.util.pushUtil

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.util.pluginUtil.PluginEnv

/**
  * sftp和ftp文件传输的接口
  * Created by John on 2016/9/23.
  */
trait FileTransferService extends Serializable with OpenApiLogging{

  protected val testTransSourceDir: String = PluginEnv.ftpTestPushDir
  protected val testTransSourceFileName: String = PluginEnv.ftpTestPushFileName
  protected val testTransSourceFullFile: String = testTransSourceDir + "/" + testTransSourceFileName

  def printSysInfo() = {

    println("testTransSourceDir:[" + testTransSourceDir + "]")
    println("testTransSourceFileName:[" + testTransSourceFileName + "]")
    println("testTransSourceFullFile:[" + testTransSourceFullFile + "]")

    log.debug("testTransSourceDir:[" + testTransSourceDir + "]")
    log.debug("testTransSourceFileName:[" + testTransSourceFileName + "]")
    log.debug("testTransSourceFullFile:[" + testTransSourceFullFile + "]")

  }

  @throws[Exception]
  def chkConnect(host: String, port: Int, username: String, password: String, ftpmode: String, directory: String): Boolean

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
  def getConnect(host: String, port: Int, username: String, password: String, ftpmode: String, directory: String)

  /**
    * 断开ftp/sftp服务器连接
 *
    * @throws Exception
    */
  @throws[Exception]
  def disConn

  /**
    * 从本地文件系统上传文件
    *
    * @param directory 远程目标路径
    * @param uploadLocalFile 本地路径
    * @param targetRemoteFile 远程目标文件名称
    * @throws Exception
    */
  @throws[Exception]
  def upload(directory: String, uploadLocalFile: String, targetRemoteFile: String)

  /**
    * 从HDFS文件系统上传文件
    * @param directory
    * @param uploadLocalFile
    * @param targetRemoteFile
    * @throws Exception
    */
  @throws[Exception]
  def uploadFromHDFS(directory: String, uploadLocalFile: String, targetRemoteFile: String)
  /**
    * 下载文件
 *
    * @param directory
    * @param downloadFile
    * @param saveFile
    * @throws Exception
    */
  @throws[Exception]
  def download(directory: String, downloadFile: String, saveFile: String)

  /**
    * 删除文件
 *
    * @param directory
    * @param deleteFile
    * @throws Exception
    */
  @throws[Exception]
  def delete(directory: String, deleteFile: String)

  /**
    * 获取远端路径中文件/文件夹列表
 *
    * @param directory
    * @throws Exception
    * @return
    */
  @throws[Exception]
  def remoteList(directory: String): List[String]
}
