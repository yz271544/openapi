package openapi.scheduler.local

import openapi.executor.ExecutorURLClassLoader
import openapi.{OpenApiEnv, Utils, Logging, OpenApiContext}
import openapi.scheduler.TaskScheduler

//import scala.actors.threadpool.AtomicInteger

/**
  * Created by Administrator on 2016/3/14.
  */
private[openapi] class LocalScheduler(threads:Int,maxFailures:Int,ac:OpenApiContext )
      extends TaskScheduler
      with Logging{

  //var attemptId = new AtomicInteger(0)
  var threadPool = Utils.newDaemonFixedThreadPool(threads)
  val env = OpenApiEnv.get
  val classLoader = new ExecutorURLClassLoader(Array(), Thread.currentThread.getContextClassLoader)

  override def start(){

    println("thread is start")
  }

  override def stop(){
    threadPool.shutdown()
  }


}
