package com.teradata.openapi.framework.util.pluginUtil

/**
  * Created by hdfs on 2017/5/23.
  */

private[openapi] class TableColumnInfo(cyc:CycleInfo, norCol:scala.collection.mutable.HashMap[String, String]) extends Serializable {
	val cycle = cyc
	val norColumn = norCol
	def isParTable = cycle match {
		case null 	=> false
		case _ 		=> true
	}
	def getCycleName = cycle.field_name
	def getPartitionContext = cycle.field_name + " " + cycle.field_targt_type
	def getParTitleContext =  norColumn.keySet.mkString(",")
	def getTitleContext = getParTitleContext + "," + getCycleName
	def getHiveParFields = norColumn.toList.map(x=> x._1 + " " + x._2).mkString(",")
	def getHiveNoParFields= getHiveParFields + "," + cycle.field_name + " " + cycle.field_targt_type
	def getHiveParTitleContext=norColumn.keySet.mkString(",")

	def getTargetParDate = cycle.field_targt_type.toUpperCase.equals("DATE")

	def getCycleCondString(templateType:String, template:String, param:scala.collection.mutable.Map[String,String]):String = {
		templateType match {
			//"INSERT OVERWRITE TABLE  ${hiveTableName} SELECT ${titleContext} FROM ${hiveTmpTableName}"
			case "NoPartition" 		=>		template
			case "OnePartition"		=>		template.replace("${onlyCondition}", cycle.field_name + "=" + TypeConvertUtil.hivePartValueString(cycle.field_targt_type).format(cycle.field_value.head.toString))
			case "MulPartition"		=> {
				val Array(head,tail)= BaseUtil.replace(template, param).split('#')
				head + cycle.field_value.map(
					x=>tail.replace("${conditionMul}", cycle.field_name + "=" + TypeConvertUtil.hivePartValueString(cycle.field_targt_type).format(x))
				).mkString(" ")
			}
		}
	}

}
