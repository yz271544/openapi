package com.teradata.openapi.framework.plugin


/**
  * Created by lzf on 2016/4/12.
  */
trait TaskPlugin[Step] extends Serializable {

  private[openapi] val out = scala.collection.mutable.Map[String,Any]()
  def init(step: Step, template: String, env: Map[String,Any])

}


