package com.teradata.openapi.framework.exceptions

/**
  * Created by Administrator on 2016/4/1.
  */
class OpenApiException(message:String,cause:Throwable)
     extends Exception(message,cause){

     def this(message:String) = this(message,null)
}
