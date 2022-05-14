package com.teradata.openapi.framework

/**
  * Created by Administrator on 2016/4/1.
  */
private[openapi] class ApplicationDescription(val apiName:String)
  extends Serializable{

  override def toString: String = "ApplicationDescription(" + apiName + ")"

}
