package com.teradata.openapi.master.deploy

/**
  * Created by Administrator on 2016/4/1.
  */
private[openapi] object RecoveryState extends Enumeration{

  type MasterState = Value

  val STANDBY, ALIVE, RECOVERING, COMPLETING_RECOVERY = Value

}
