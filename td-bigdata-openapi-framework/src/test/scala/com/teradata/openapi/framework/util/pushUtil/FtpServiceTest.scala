package com.teradata.openapi.framework.util.pushUtil;

import org.junit.Test

class FtpServiceTest {

  @Test
  def connTest(): Unit = {
    val ftpService = new FtpService
    ftpService.getConnect()
  }
}