package com.teradata.openapi.framework.util

import akka.actor.Actor
import org.slf4j.Logger

/**
  * Created by Administrator on 2016/4/1.
  */
trait ActorLogReceive {

  self:Actor =>

  override def receive:Receive = new Receive{

    private val _receiveWithLogging = receiveWithLogging
    override def isDefinedAt(o: Any): Boolean = _receiveWithLogging.isDefinedAt(o)
    override def apply(o:Any):Unit = {
      if(log.isDebugEnabled){
        log.debug(s"[actor] received message $o from ${self.sender}")
      }
      val start = System.nanoTime
      _receiveWithLogging.apply(o)
      val timeTaken = (System.nanoTime - start).toDouble / 1000000
      if (log.isDebugEnabled) {
        log.debug(s"[actor] handled message ($timeTaken ms) $o from ${self.sender}")
      }
    }
  }

  def receiveWithLogging:Receive

  protected def log:Logger



}
