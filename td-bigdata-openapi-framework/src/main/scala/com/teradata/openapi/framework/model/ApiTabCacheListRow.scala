package com.teradata.openapi.framework.model

/**
  * Created by John on 2016/6/29.
  */
case class ApiTabCacheListRow(schema_name:String, tab_name: String, refresh_time :Long, hit_times:Option[Int])