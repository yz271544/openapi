package com.teradata.openapi.framework

import akka.actor.ActorSystem
import com.teradata.openapi.framework.util.AkkaUtils

/**
  * 保存了一个api运行时的环境信息，不同类型的api为Master和Worker进行的配置,包括 Akka actor system，conf.
  * 通过全局变量找到OpenApiEnv,这样所有的线程都可以访问同样的OpenApiEnv
  * OpenApiEnv包含了整个OpenApi运行时的上下文信息，OpenApiConf只是调整一些状态的信息
  * Created by Administrator on 2016/3/31.
  */
class OpenApiEnv(val actorSystem: ActorSystem,val conf:OpenApiConf) extends OpenApiLogging{

}

object OpenApiEnv extends OpenApiLogging{

  private val env = new ThreadLocal[OpenApiEnv]
  private var lastSetOpenApiEnv : OpenApiEnv = _

  def set(e:OpenApiEnv): Unit ={
    lastSetOpenApiEnv = e
    env.set(e)
  }

  def get:OpenApiEnv = {
    Option(env.get()).getOrElse(lastSetOpenApiEnv)
  }

  /**
    * Returns the ThreadLocal OpenApiEnv
    * @return
    */
  def getThreadLocal:OpenApiEnv ={
    env.get()
  }

  private[openapi] def create(conf:OpenApiConf,
          hostname:String,
          port:Int,
          isOpenApiDriver:Boolean):OpenApiEnv = {

    val (actorSystem,boundPort) = AkkaUtils.createActorSystem("openapiClient",hostname,port,conf = conf)

    if(isOpenApiDriver && port == 0){
      conf.set("openapi.driver.port",boundPort.toString)
    }

    new OpenApiEnv(actorSystem,conf)

  }
























}
