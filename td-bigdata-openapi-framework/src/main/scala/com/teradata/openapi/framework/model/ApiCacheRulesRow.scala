package com.teradata.openapi.framework.model

/**
  * Created by John on 2016/6/29.
  */
case class ApiCacheRulesRow(rule_id:Int
                            ,rule_type:Int
                            ,rule_name:String
                            ,rule_range:String
                            ,rule_priority:Int
                            ,hit_times_min:Option[Int]
                            ,visit_time_max:Option[Int]
                            ,visit_time_unit:Option[String]
                            ,rule_chg_time:Long
                            ,eff_stat:Int )


