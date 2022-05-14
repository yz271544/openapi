package openapi

import akka.actor.ActorSystem
import openapi.util.AkkaUtils

/**
  * Created by Administrator on 2016/3/14.
  */
class OpenApiEnv(val executorId:String,val actorSystem:ActorSystem) {

}

object OpenApiEnv extends Logging{

  private val env = new ThreadLocal[OpenApiEnv]

  def set(e: OpenApiEnv): Unit ={
    env.set(e)
  }

  def get: OpenApiEnv = {
    env.get()
  }

  def createFromSystemProperties(
      executorId:String,
      hostname:String,
      port:Int,
      isDriver:Boolean,
      isLocal:Boolean): OpenApiEnv = {

    val (actorSystem, boundPort) = AkkaUtils.createActorSystem("openapi", hostname, port)

    if (isDriver && port == 0) {
      System.setProperty("openapi.driver.port", boundPort.toString)
    }

    val classLoader = Thread.currentThread.getContextClassLoader

    def instantiateClass[T](propertyName: String, defaultClassName: String): T = {
      val name = System.getProperty(propertyName, defaultClassName)
      Class.forName(name, true, classLoader).newInstance().asInstanceOf[T]
    }

    new OpenApiEnv(executorId,actorSystem)

  }

}



























