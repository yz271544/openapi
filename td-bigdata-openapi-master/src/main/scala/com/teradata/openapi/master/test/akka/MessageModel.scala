package com.teradata.openapi.master.test.akka

/**
  * Created by Administrator on 2016/3/28.
  */
abstract class MessageModel

case class GreeterMessage(message:String)
case class HelloWorld2(message:String)

