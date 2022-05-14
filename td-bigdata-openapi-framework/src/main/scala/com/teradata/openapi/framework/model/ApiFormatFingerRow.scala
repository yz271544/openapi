package com.teradata.openapi.framework.model

/**
  * Created by John on 2016/5/13.
  */
case class ApiFormatFingerRow(data_finger :String,form_finger :String,file_loc :String,hit_times :Int,eff_flag :Int,eff_time :Long,exp_time :Long,last_visit_time: Option[Long])
