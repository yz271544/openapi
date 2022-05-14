package com.teradata.openapi.framework.model

/**
  * Created by lzf on 2016/5/13.
  */
case class PluginInfoRow(source_type_code:String,
                        plugin_id:Int,
                        plugin_name:String,
                        plugin_desc:String,
                        class_name:String,
                        creat_time:Long,
                        creat_persn:String,
                        template:String,
                        api_visit_methd:Int,
                        plugin_type:Int
                        )
