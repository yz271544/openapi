package com.teradata.openapi.framework.util


/**
  * Created by John on 2016/12/26.
  */
object SystemParameterNames {
  val METHOD: String = "method"
  val FORMAT: String = "format"
  val LOCALE: String = "locale"
  val SESSION_ID: String = "sessionId"
  val APP_KEY: String = "appKey"
  val VERSION: String = "version"
  val SIGN: String = "sign"
  val REQ_TYPE: String = "reqType"
  val CODE_TYPE: String = "codeType"
  val FIELDS: String = "fields"
  val JSONP: String = "callback"

  def getSystemParamsName: String = {
    METHOD + "," + FORMAT + "," + LOCALE + "," + APP_KEY + "," + VERSION + "," + SIGN + "," + REQ_TYPE + "," + CODE_TYPE + "," + FIELDS
  }
}