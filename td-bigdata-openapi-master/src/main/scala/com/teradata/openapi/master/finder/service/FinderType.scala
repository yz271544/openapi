package com.teradata.openapi.master.finder.service

/**
  * Created by John on 2016/4/22.
  */
private[finder] object ApiSnapshotUpdateType extends Enumeration {
  type ApiSnapshotUpdateType = Value
  val ADD, DROP = Value
}