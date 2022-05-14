package openapi.scheduler.cluster

/**
  * Created by Administrator on 2016/3/15.
  * 针对于集群调度系统而设计的backend接口
  */
private[openapi] trait SchedulerBackend {

  def start():Unit
  def stop():Unit
  def reviveOffers():Unit
  def defaultParallelism():Unit

}
