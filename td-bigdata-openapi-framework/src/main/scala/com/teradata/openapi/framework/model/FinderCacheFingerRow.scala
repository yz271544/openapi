package com.teradata.openapi.framework.model

/**
  * Created by John on 2016/5/19.
  */
case class FinderCacheFingerRow(
                       req_id: String
                       , api_id: Int
                       , api_version: Int
                       , schema_name: String
                       , tab_name: String
                       , cyclecolumn: String
                       , cyclevalues: String
                       , form_finger: String
                     )
