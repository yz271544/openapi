package com.teradata.openapi.framework.scheduler

/**
  * Created by Administrator on 2016/3/31.
  */
trait CoarseThreadBackend {

  def start():Unit

  def stop():Unit

}
