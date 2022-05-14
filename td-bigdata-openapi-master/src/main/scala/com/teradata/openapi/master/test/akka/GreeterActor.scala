package com.teradata.openapi.master.test.akka

import akka.actor.Actor

/**
  * Created by Administrator on 2016/3/28.
  */
class GreeterActor extends Actor{

  override def receive: Receive = {

    case HelloWorld2(mes:String) =>{
      println(mes)
      sender ! GreeterMessage("GREET")
    }
    case _ =>
      unhandled("nothing```")
  }

}
