package com.teradata.openapi.formatPage

import java.io.FileOutputStream

import com.codahale.jerkson.Json
import com.teradata.openapi.framework.deploy.Format
import org.apache.poi.hssf.usermodel.{HSSFCell, HSSFCellStyle, HSSFWorkbook}
import org.apache.poi.hssf.util.Region


/**
  * Created by hdfs on 2016/6/23.
  */
object Test extends App{


	val json= "{\"formType\":\"json\"}"
	val format =Json.parse[Format](json)
	println(format)

	val a = "\\u007c"
	val b = "\\t"
	val c = "|"
	val d = "saljf"

	val aa = "aksjdlf\tslafidujoi\tfjownef\talsjkdfl"
	val bb = "2938\t9283j\tojasidf"
	val cc = "aksjdlf	slafidujoi	fjownef	alsjkdfl"

/*	println(aa.replace(a, "|"))
	println(bb.replace(b,"&"))*/
	println(FormatFunctionUtil.convertDelimiter(a))
	println(FormatFunctionUtil.convertDelimiter(b))
	println(FormatFunctionUtil.convertDelimiter(c))
	println(FormatFunctionUtil.convertDelimiter(d))


}
