package com.teradata.openapi.framework.plugin

import com.teradata.openapi.framework.step.{AsynStep, Step}


/**
  * Created by lzf on 2016/4/12.
  */
trait AsyncTaskPlugin[AsynStep] extends TaskPlugin[AsynStep] {


  def execute() :Int

}


