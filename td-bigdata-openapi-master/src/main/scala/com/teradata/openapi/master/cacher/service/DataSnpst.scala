package com.teradata.openapi.master.cacher.service

import com.teradata.openapi.framework.util.DicMapFunc

import scala.util.control.Breaks._
/**
  * Created by John on 2016/6/30.
  */
case class DataSnpst(schemaName:String = "",tableName :String = "",data_snpst_name_val :Map[String,List[Any]] = scala.collection.immutable.TreeMap())
  extends DicMapFunc{
  def canEqual(other: Any): Boolean = other.isInstanceOf[DataSnpst]

  override def equals(other: Any): Boolean = other match {
    case that: DataSnpst =>
      (that canEqual this) &&
        schemaName == that.schemaName &&
        tableName == that.tableName &&
        //data_snpst_name_val == that.data_snpst_name_val
        mapKeySetEquels(data_snpst_name_val,that.data_snpst_name_val) &&
        compareMapVal4List(data_snpst_name_val,that.data_snpst_name_val) &&
        compareMapVal4List(that.data_snpst_name_val,data_snpst_name_val)
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(schemaName, tableName, data_snpst_name_val)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }

  def compareMapVal4List(a:Map[String,List[Any]],b:Map[String,List[Any]]) :Boolean = {
    var cmpResult :Boolean = true
    breakable {
      a.foreach(e => {
        val (column, columnVal4List) = e
        if(!compareList(columnVal4List, b.getOrElse(column, List())))
          cmpResult = false
          break()
      })
    }
    cmpResult
  }

}
