package com.teradata.openapi.framework.model

/**
  * Created by John on 2016/5/13.
  */
case class ApiCacheTabInfoRow(
                       api_id: Int,
                       api_version: Int,
                       source_id: Int,
                       schema_name: String,
                       tab_name: String)