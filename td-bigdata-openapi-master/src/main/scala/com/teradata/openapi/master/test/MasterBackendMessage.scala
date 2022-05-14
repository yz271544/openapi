package com.teradata.openapi.master.test

/**
  * Created by Administrator on 2016/3/31.
  */
private[openapi] sealed trait MasterBackendMessage extends Serializable

private[openapi] object MasterBackendMessage{

  case object StopMaster extends MasterBackendMessage

}
