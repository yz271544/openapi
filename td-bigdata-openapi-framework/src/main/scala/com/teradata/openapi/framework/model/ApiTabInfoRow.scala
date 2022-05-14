package com.teradata.openapi.framework.model

/**
  * Created by John on 2016/6/29.
  */
case class ApiTabInfoRow(api_id: Int
                         , api_version: Int
                         , source_id: Int
                         , schema_name: String
                         , tab_name: String
                         , tab_alias: String
                         , field_name: String
                         , field_alias: String
                         , field_type: String
                         , field_len: Int
                         , field_tot_digit: Int
                         , field_prec_digit: Int)


