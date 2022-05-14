package openapi.deploy.master

import java.util.Date

import akka.actor.ActorRef
import openapi.deploy.ApplicationDescription

/**
  * Created by Administrator on 2016/3/15.
  */
private[openapi] class ApplicationInfo(
      val startTime:Long,
      val id:String,
      val desc:ApplicationDescription,
      val submitDate:Date,
      val driver:ActorRef){


     var state = ApplicationState.WITING

}





















