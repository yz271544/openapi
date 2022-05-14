package com.teradata.openapi.framework.model

/**
  * Created by John on 2016/5/19.
  */
case class ReqInfoVwRow(
                       req_id: String,
                       req_stat: Int,
                       req_time: Long,
                       end_time: Long,
                       appkey: String,
                       api_id: Int,
                       api_version: Int,
                       api_name :String,
                       api_sort: Int,
                       api_cls_code:Int,
                       form_code: String,
                       encode: String,
                       req_arg: String,
                       respn_arg: String,
                       push_arg: String,
                       retn_form_finger: String,
                       retn_data_finger: String,
                       api_visit_methd: Int,
                       trigger_sorc: String,
                       finder_loc: String,
                       priority: Int
                     )
