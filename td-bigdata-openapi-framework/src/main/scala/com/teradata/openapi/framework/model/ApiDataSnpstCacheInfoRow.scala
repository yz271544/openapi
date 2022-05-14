package com.teradata.openapi.framework.model

/**
  * Created by John on 2016/6/29.
  */
case class ApiDataSnpstCacheInfoRow(schema_name: String, tab_name: String, data_snpst_name_val :String, isnt_cache:Int, hit_times:Option[Int] ,last_visit_time :Option[Long])
