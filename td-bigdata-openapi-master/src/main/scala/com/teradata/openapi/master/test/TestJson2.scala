package com.teradata.openapi.master.test

import com.codahale.jerkson.{Json, ParsingException}
import com.teradata.openapi.framework.deploy.FormatType._
import com.teradata.openapi.framework.model.{DataSnpst, DataSnpstVal, ListDataSnpst}
import com.teradata.openapi.framework.util.DicMapFunc

/**
  * Created by John on 2016/7/29.
  */

object FormatType extends Enumeration {
  type FormatTypeInfo = Value
  val TXT, JSON, XML, XLS, CSV = Value
}

case class CellList(cellName: String, rowIndex: Int, colIndex: Int, rowSpan: Int, colSpan: Int) extends Serializable

case class ExcelTitles(rowIndex: Int, cellList: List[CellList]) extends Serializable

case class FormDetail(rptName: String, excelTitles: List[ExcelTitles]) extends Serializable

case class Format(formType: String, formDetail: Option[FormDetail]) extends Serializable


object TestJson2 extends DicMapFunc {


  def main(args: Array[String]) {

    var ff: Format = null

    //val testData = """{"formType" : "json","formDetail":{"excelTitles":[{"cellList":[{"rowSpan":3,"cellName":"月份","colSpan":1,"colIndex":0,"rowIndex":0},{"rowSpan":3,"cellName":"地市代码","colSpan":1,"colIndex":1,"rowIndex":0},{"rowSpan":3,"cellName":"地市","colSpan":1,"colIndex":2,"rowIndex":0},{"rowSpan":3,"cellName":"区县代码","colSpan":1,"colIndex":3,"rowIndex":0},{"rowSpan":3,"cellName":"区县","colSpan":1,"colIndex":4,"rowIndex":0},{"rowSpan":1,"cellName":"中高端客户数","colSpan":6,"colIndex":5,"rowIndex":0},{"rowSpan":1,"cellName":"离网率","colSpan":6,"colIndex":11,"rowIndex":0},{"rowSpan":2,"cellName":"聚类业务","colSpan":3,"colIndex":17,"rowIndex":0}],"rowIndex":0},{"cellList":[{"rowSpan":1,"cellName":"客户保有","colSpan":3,"colIndex":5,"rowIndex":1},{"rowSpan":1,"cellName":"价值保有","colSpan":3,"colIndex":8,"rowIndex":1},{"rowSpan":1,"cellName":"不含11位无线固话","colSpan":3,"colIndex":11,"rowIndex":1},{"rowSpan":1,"cellName":"含11位无线固话","colSpan":3,"colIndex":14,"rowIndex":1}],"rowIndex":1},{"cellList":[{"rowSpan":1,"cellName":"中高端客户数","colSpan":1,"colIndex":5,"rowIndex":2},{"rowSpan":1,"cellName":"保有客户数","colSpan":1,"colIndex":6,"rowIndex":2},{"rowSpan":1,"cellName":"保有率","colSpan":1,"colIndex":7,"rowIndex":2},{"rowSpan":1,"cellName":"中高端客户收入","colSpan":1,"colIndex":8,"rowIndex":2},{"rowSpan":1,"cellName":"保有收入","colSpan":1,"colIndex":9,"rowIndex":2},{"rowSpan":1,"cellName":"保有率","colSpan":1,"colIndex":10,"rowIndex":2},{"rowSpan":1,"cellName":"到达客户数（不含11位无线固话）","colSpan":1,"colIndex":11,"rowIndex":2},{"rowSpan":1,"cellName":"离网客户数（不含11位无线固话）","colSpan":1,"colIndex":12,"rowIndex":2},{"rowSpan":1,"cellName":"离网率","colSpan":1,"colIndex":13,"rowIndex":2},{"rowSpan":1,"cellName":"到达客户数（含11位无线固话）","colSpan":1,"colIndex":14,"rowIndex":2},{"rowSpan":1,"cellName":"离网客户数（含11位无线固话）","colSpan":1,"colIndex":15,"rowIndex":2},{"rowSpan":1,"cellName":"离网率","colSpan":1,"colIndex":16,"rowIndex":2},{"rowSpan":1,"cellName":"到达客户数","colSpan":1,"colIndex":17,"rowIndex":2},{"rowSpan":1,"cellName":"聚类客户数","colSpan":1,"colIndex":18,"rowIndex":2},{"rowSpan":1,"cellName":"聚类业务渗透率","colSpan":1,"colIndex":19,"rowIndex":2}],"rowIndex":2}],"rptName":"存量用户监控"}}"""
    val testData = """{"formType" : "json""""
    try {
      ff = Json.parse[Format](testData)
    } catch {
      case e: ParsingException => println(e.getMessage + e)
      case e: Exception => {
        println(e.getMessage + e)
      }
    }

    println(ff)

    if (ff != null){
      println(ff.formType)
      val aa = FormatType.withName(ff.formType.toUpperCase())
      println("aa:" + aa)
    }

    /*val tt = Format(FormatType.XLS,FormDetail("存量用户监控",List(ExcelTitles(rowIndex = 0,List(CellList(cellName="月份",rowIndex = 0,colIndex = 0, rowSpan = 3, colSpan = 1),CellList(cellName="中高端客户数",rowIndex = 0,colIndex = 5, rowSpan = 1, colSpan = 6))))))
    val tj: String = Json.generate(tt)
    println(tj)*/
  }
}
