package com.teradata.openapi.framework.model

/**
  * Created by John on 2016/5/13.
  */
case class StructApiArgRow(api_id: Int,
                           api_version: Int,
                           source_id: Int,
                           tab_alias: String,
                           schema_name: String,
                           tab_name: String,
                           field_name: String,
                           field_alias: String,
                           field_eff_stat: Int,
                           field_sorc_type: String,
                           field_targt_type: String,
                           field_title: String,
                           must_type: Int,
                           must_one_grp_id: Int,
                           req_arg_id: Char,
                           req_arg_deflt_val: String,
                           respn_arg_id: Char,
                           respn_arg_samp_val: String,
                           field_file_desc: String,
                           calc_princ_id: Int,
                           value_range: String)