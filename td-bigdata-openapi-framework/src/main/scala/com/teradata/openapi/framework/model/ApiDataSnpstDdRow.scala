package com.teradata.openapi.framework.model


/**
  * Created by John on 2016/5/5.
  */
case class ApiDataSnpstDdRow(api_id: Int, api_version:Int, source_id: Int, data_snpst_val: String, hit_times: Option[Int],last_visit_time :Option[Long])