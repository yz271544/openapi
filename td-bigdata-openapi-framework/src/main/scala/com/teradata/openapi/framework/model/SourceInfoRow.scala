package com.teradata.openapi.framework.model

/**
  * Created by lzf on 2016/5/13.
  */
case class SourceInfoRow(source_id: Int,
                         source_type_code: String,
                         drv_code: Int,
                         source_desc: String,
                         ip_addr: String,
                         port: Int,
                         deflt_schema: String,
                         user_name: String,
                         pwd: String,
                         priority: Int,
                         sync_strategy_id: Int,
                         asyn_strategy_id: Int,
                         drv_name: String,
                         sync_finder_flag: Boolean,
                         asyn_finder_flag: Boolean,
                         rss_finder_flag: Boolean
                        )
