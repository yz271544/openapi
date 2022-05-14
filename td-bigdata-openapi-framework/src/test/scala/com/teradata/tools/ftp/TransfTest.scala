package com.teradata.tools.ftp

import com.teradata.openapi.framework.util.pushUtil.SftpService

object TransfTest {
  def main(args: Array[String]): Unit = {

    val host = "192.168.20.110"
    val port = 22
    val username = "oapi"
    val password = "oapi"
    val ftpmode = "active"
    val ftpTargetPath = "/data/open_api/huzy"

    val sftp = new SftpService

    val trasChk: Boolean = sftp.chkConnect(host, port, username, password, ftpmode, ftpTargetPath)

    println(trasChk)
  }
}
