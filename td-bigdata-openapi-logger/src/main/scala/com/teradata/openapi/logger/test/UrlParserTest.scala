package com.teradata.openapi.logger.test

import com.netaporter.uri.Uri

/**
  * Created by Evan on 2016/6/13.
  */
object UrlParserTest {

  def main(args: Array[String]) {

    val uri = "http://192.168.20.1:9080/restserver/router?CITY_CODE=01&method=tb_rpt_bo_mon&codeType=UTF-8&REGION_CODE=00&format=json&sign=90E0D3F0DEFF93B769531B4F5739EC30F49286E1&reqType=asyn&appKey=houbl&locale=zh_CN&fields=DEAL_DATE,REGION_CODE,CITY_CODE,BARGAIN_NUM,SHOULD_PAY,SHOULD_PAY_AMT&version=1&DEAL_DATE=201604"

    val pattern = "http://".r
    val p = pattern findFirstIn uri
    if(p.getOrElse().equals("http://")){
      println(uri)
    }


    val urlstr = Uri.parse(uri)

    //val urlstr = Uri.parse("http://example.com/path?a=b&a=c&d=e")
    val urlparameter = urlstr.query.paramMap

    println(urlparameter)

  }

}
