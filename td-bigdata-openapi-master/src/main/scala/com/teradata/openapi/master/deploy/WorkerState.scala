package com.teradata.openapi.master.deploy

/**
  * Created by Evan on 2016/4/20.
  */
private[openapi] object WorkerState extends Enumeration{

  type WorkerState = Value

  val ALIVE, DEAD, DECOMMISSIONED, UNKNOWN = Value

}
