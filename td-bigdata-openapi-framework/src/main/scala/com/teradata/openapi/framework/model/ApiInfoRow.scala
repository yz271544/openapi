package com.teradata.openapi.framework.model

/**
  * Created by John on 2016/5/13.
  */
case class ApiInfoRow(
                       api_id: Int,
                       api_version: Int,
                       api_cls_code: Int,
                       api_stat_code: Int,
                       api_sort: Int,
                       data_strct_type_code: Int,
                       relse_type: Int,
                       api_name: String,
                       api_desc: String,
                       data_cycle_type: Int,
                       relse_persn: String,
                       relse_time: String,
                       tab_scale_type: Int,
                       exam_stat: Int,
                       api_class_name: String,
                       trigger_methd: Int)