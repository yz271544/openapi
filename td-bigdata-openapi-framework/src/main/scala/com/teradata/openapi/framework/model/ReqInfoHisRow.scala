package com.teradata.openapi.framework.model

/**
  * Created by John on 2016/5/19.
  */
case class ReqInfoHisRow(
                          req_id: String
                          , req_stat: Int
                          , req_time: Long
                          , end_time: Long
                          , appkey: String
                          , api_id: Int
                          , api_version: Int
                          , form_code: String
                          , encode: String
                          , req_arg: String
                          , respn_arg: String
                          , push_arg: String
                          , retn_form_finger: String
                          , retn_data_finger: String
                          , api_visit_methd: Int
                          , trigger_methd: Int
                          , trigger_sorc: String
                          , finder_loc: String
                          , rss_id: Int
                          , priority: Int
                          , arch_time: Long
                          , arch_proc: String
                        )
