package com.teradata.openapi.framework.expression

import com.teradata.openapi.framework.OpenApiLogging

object CheckOut extends OpenApiLogging{
  def checkOut(str: String): Unit ={
    log.debug("===========checkOut=============:[" + str + "]")
  }
}
