package com.teradata.openapi.framework.plugin

/**
  * Created by lzf on 2016/4/12.
  */
trait SyncTaskPlugin[SyncStep] extends TaskPlugin[SyncStep] {



  def init(step: SyncStep, template: String, env: Map[String, Any])

  def execute(): String
  override def toString = {
    "SyncTaskPlugin"
  }
}


