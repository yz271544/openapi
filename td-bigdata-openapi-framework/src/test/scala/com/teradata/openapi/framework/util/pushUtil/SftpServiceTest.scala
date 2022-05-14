package com.teradata.openapi.framework.util.pushUtil;



//import static org.junit.Assert.*;

object SftpServiceTest {

    def main(args: Array[String]): Unit = {
        val host = "192.168.20.110"
        val port = 22
        val username = "oapi"
        val password = "oapi"
        val ftpmode = "active"
        val ftpTargetPath = "/data/open_api/huzy"

        val sftp = new SftpService

        val transRst = sftp.chkConnect(host, port, username, password, ftpmode, ftpTargetPath)

        println(transRst)
    }

}