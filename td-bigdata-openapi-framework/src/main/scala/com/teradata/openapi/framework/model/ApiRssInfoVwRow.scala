package com.teradata.openapi.framework.model

/**
  * Created by John on 2016/5/20.
  */
case class ApiRssInfoVwRow(rss_id: Int,
                           rss_start_time: Long,
                           rss_end_time: Long,
                           app_key: String,
                           api_id: Int,
                           api_version: Int,
                           api_name: String,
                           api_sort: Int,
                           api_cls_code: Int,
                           form_code: String,
                           encode: String,
                           req_arg: String,
                           respn_arg: String,
                           push_arg: String,
                           retn_form_finger: String,
                           retn_data_finger: String,
                           trigger_methd: Int,
                           trigger_sorc: String,
                           priority: Int,
                           eff_flag: Int)

