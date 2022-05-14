package openapi.scheduler.cluster

import openapi.{Logging, OpenApiContext}
import openapi.scheduler.TaskScheduler

/**
  * Created by Administrator on 2016/3/15.
  * TaskScheduler的实现，在集群上运行task。
  */
private[openapi] class ClusterScheduler(val oc:OpenApiContext)
   extends TaskScheduler
   with Logging{

  var backend: SchedulerBackend = null

  def initialize(context: SchedulerBackend) {
    backend = context
  }

  override def start(){
    backend.start()
  }

  override def stop(){

  }

}
