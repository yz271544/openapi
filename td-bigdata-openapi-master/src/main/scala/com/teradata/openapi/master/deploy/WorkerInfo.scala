package com.teradata.openapi.master.deploy

import akka.actor.ActorRef
import com.teradata.openapi.framework.util.Utils

/**
  * Created by Evan on 2016/4/20.
  */
private[openapi] class WorkerInfo(val id:String,
                                  val host:String,
                                  val port:Int,
                                  val actor:ActorRef,
                                 val publicAddress:String) extends Serializable{

  Utils.checkHost(host, "Expected hostname")
  assert (port > 0)

  //注解@transient，在JVM中为transient字段，表明是非序列化的一部分，常用于临时保存的缓存数据，或易于重新计算的数据
  @transient var state: WorkerState.Value = _

  init()

  private def init(): Unit ={
    state = WorkerState.ALIVE
  }
}
