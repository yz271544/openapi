package openapi.scheduler

/**
  * Created by Administrator on 2016/3/14.
  */
private[openapi] trait TaskScheduler {

  def start(): Unit

  def stop(): Unit

}
