package com.teradata.openapi.httpclient

import java.io.{BufferedWriter, File, FileWriter}
import com.codahale.jerkson.{Json, ParsingException}
import com.teradata.openapi.util.HttpClientEnv
import scala.io.Source
import scala.xml.XML

/**
  * Created by John on 2016/11/29.
  * XML URL:http://192.168.20.110:9080/restserver/router?appKey=qichao1&method=CycleRcd&version=1&format=xml&locale=zh_CN&codeType=UTF-8&reqType=syn&fields=reqID,retCode,retMsg,reqStat&TABLENAME=RPTMART3&SOURCEID=1&SCHEMANAME=TB_RPT_BO_MON&CYCLECOLUMN=DEAL_DATE&UPDCYCLETYPE=ADD&CYCLEVALUS=201601&sign=E0DD497F78A801FFF0DBE1FE03AB508E8C69C2CA
  * Json URL:http://192.168.20.110:9080/restserver/router?appKey=qichao1&method=CycleRcd&version=1&format=json&locale=zh_CN&codeType=UTF-8&reqType=syn&fields=reqID,retCode,retMsg,reqStat&TABLENAME=TB_RPT_BO_MON&SOURCEID=1&SCHEMANAME=RPTMART3&CYCLECOLUMN=DEAL_DATE&UPDCYCLETYPE=ADD&CYCLEVALUS=201605&sign=B7207AC58FFBE18E0CC5D726F096389B982BD9DC
  * [Error]Data URL:http://192.168.20.110:9080/restserver/router?appKey=qichao1&method=tb_rpt_bo_mon&version=1&format=json&locale=zh_CN&codeType=UTF-8&reqType=syn&fields=SHOULD_PAY_AMT,REGION_CODE,BARGAIN_NUM,SHOULD_PAY,CITY_CODE,DEAL_DATE&DEAL_DATE=201604&sign=5E4EA5737D4196DDDFBDB164B5FA60E2E1D348B5
  */
case class JsonResult(retCode: String, retMsg: String, reqID: String, reqStat: String)

case class XmlResult(retCode: String, retMsg: String, reqID: String, reqStat: String) {
  //序列化操作
  def toXML = {
    <findToReqAsynYIK>
      <retCode>
        {retCode}
      </retCode>
      <retMsg>
        {retMsg}
      </retMsg>
      <reqID>
        {reqID}
      </reqID>
      <reqStat>
        {reqStat}
      </reqStat>
    </findToReqAsynYIK>
  }

  override def toString = "XmlResult(" + retCode + "," + retMsg + "," + reqID + "," + reqStat + ")"
}

object GetToolsInfo extends App {
  // we need an ActorSystem to host our application in
  //反序列化操作
  def fromXML(xml: scala.xml.Elem): XmlResult = {
    new XmlResult((xml \ "retCode").text, (xml \ "retMsg").text, (xml \ "reqID").text, (xml \ "reqStat").text)
  }

  var url: String = ""

  if (args.length > 0) {
    url = if (args(0) != null) args(0) else ""
  }
  if (("" equals url) || (!url.startsWith("http"))) {
    println("Useage:java -jar openapi-httpclient.jar URL")
    System.exit(1)
  }

  var respon = ""
  try {
    respon = Source.fromURL(url, "UTF-8").mkString
    println("respon:" + respon)
    val ret = Json.parse[JsonResult](respon)
    if (!("0" equals ret.retCode) || ("" equals respon)) throw new RuntimeException("Response Ret Exception!")
    println(ret)
  } catch {
    case e: Exception =>
      try {
        val ret: XmlResult = fromXML(XML.loadString(respon))
        if (!("0" equals ret.retCode) || ("" equals respon)) throw new RuntimeException("Response Ret Exception!")
        println(ret)
      } catch {
        case e: Exception =>
          val fileName = String.format("%s/%s", HttpClientEnv.sourcePath, System.currentTimeMillis().toString)
          try {
            val exceptionDir = new File(HttpClientEnv.sourcePath)
            if (!exceptionDir.exists()) exceptionDir.mkdirs()
            val file = new File(fileName)
            if (!file.exists()) file.createNewFile()
            val fileWriter = new FileWriter(file.getAbsoluteFile, true)
            val bufferedWriter = new BufferedWriter(fileWriter)
            bufferedWriter.write(url)
            bufferedWriter.close()
            println("Response exception, has been submitted to the background processing process [" + fileName + "].")
          } catch {
            case e: Exception => println("[error]:" + e.printStackTrace())
          }
      }
  }
}
