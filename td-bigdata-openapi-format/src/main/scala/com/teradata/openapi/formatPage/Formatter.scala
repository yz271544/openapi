package com.teradata.openapi.formatPage

import java.io.ByteArrayOutputStream

import com.codahale.jerkson.{Json, ParsingException}
import com.teradata.openapi.framework.deploy.{Encode, Format, FormatType}
import com.teradata.openapi.framework.rewrite.GBKTextOutputFormat
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.io.{DefaultStringifier, LongWritable, NullWritable, Text}
import org.apache.hadoop.mapred.JobConf
import org.apache.hadoop.mapreduce.lib.input.{FileInputFormat, FileSplit}
import org.apache.hadoop.mapreduce.lib.output.{FileOutputFormat, TextOutputFormat}
import org.apache.hadoop.mapreduce.{InputSplit, Job, Mapper}
import org.apache.hadoop.util.GenericOptionsParser
import org.apache.poi.hssf.usermodel.{HSSFCell, HSSFCellStyle, HSSFSheet}
import org.apache.poi.hssf.util.Region

/**
  * Created by lzf on 2016/10/9.
  */
trait BaseFormatter[KEYIN, VALUEIN, KEYOUT, VALUEOUT] extends Mapper[KEYIN, VALUEIN, KEYOUT, VALUEOUT]{

  private final val defaultDelimiter = '\u0001'

  //private var fileType = ""
  //	private var inCode = ""
  //	private var outCode = ""
  private[formatPage] var inDelimiter = ""
  private[formatPage] var outDelimiter = ""
  private var title = ""
  private[formatPage] val word = new Text()
  private[formatPage] var inCode = ""
  private[formatPage] var formatDetail = ""

  def getTitle: List[String] = this.title.split(',').toList

  /*	  private val fileType =  load(conf, "FileType" ,classOf[String])
      println(System.currentTimeMillis() + " : " + System.currentTimeMillis() + "fileType is " + fileType.toString)*/
  /**
    * 按照键值对存储
    *
    * @param mapInData
    * @return
    */
  def comb(mapInData: Predef.Map[String, String]) = {
    var result = "<line>"
    for ((k, v) <- mapInData) {
      result += s"<${k.toString}>${v.toString}</${k.toString}>"
    }
    result += "</line>"
    result
  }


  def replace(ctext: String): String = ctext.replace(this.inDelimiter, this.outDelimiter)

  def split(str: String): List[String] = {
    val re = this.inDelimiter match {
      case e: String => str.split(this.inDelimiter).toList
      case _ => str.split(this.defaultDelimiter).toList
    }
    re
  }

  def row(value: Text): String = {
    val ret: String =
    if (inCode.startsWith("GBK")) {
      new String(value.getBytes(),0,value.getLength, "GBK")
    } else {
      value.toString
    }
    ret
  }

  def getIncode(context: Mapper[KEYIN, VALUEIN, KEYOUT, VALUEOUT]#Context): Unit ={
    val inputSplit: InputSplit = context.getInputSplit()
    val filename: String = inputSplit.asInstanceOf[FileSplit].getPath.getName.toUpperCase.trim
    inCode = filename.split('.').last
  }

  override def setup(context: Mapper[KEYIN, VALUEIN, KEYOUT, VALUEOUT]#Context): Unit = {

    //fileType = DefaultStringifier.load(context.getConfiguration, "formatType", classOf[Text]).toString
    //		inCode 			= DefaultStringifier.load(context.getConfiguration, "inCode"		, classOf[Text]).toString
    //		outCode 		= DefaultStringifier.load(context.getConfiguration, "outCode"		, classOf[Text]).toString
    inDelimiter = DefaultStringifier.load(context.getConfiguration, "inFieldDelimiter", classOf[Text]).toString
    outDelimiter = DefaultStringifier.load(context.getConfiguration, "outFieldDelimiter", classOf[Text]).toString
    title = DefaultStringifier.load(context.getConfiguration, "titleString", classOf[Text]).toString
    //println(System.currentTimeMillis() + " : " + "fileType is " + fileType)
    formatDetail =  DefaultStringifier.load(context.getConfiguration, "formatDetail", classOf[Text]).toString

  }
}

class JsonFormatter extends BaseFormatter[LongWritable, Text, Text, NullWritable] {

  override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, NullWritable]#Context): Unit = {

    getIncode(context)
    word.set(com.codahale.jerkson.Json.generate((getTitle, split(row(value))).zipped.toMap))
    context.write(word, NullWritable.get())
  }

}

class XmlFormatter extends BaseFormatter[LongWritable, Text, Text, NullWritable] {

  override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, NullWritable]#Context): Unit = {
    getIncode(context)
    word.set(comb((getTitle, split(row(value))).zipped.toMap))
    context.write(word, NullWritable.get())
  }
}

class TxtFormatter extends BaseFormatter[LongWritable, Text, Text, NullWritable] {

  override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, NullWritable]#Context): Unit = {
    getIncode(context)
    if (inDelimiter.equals(outDelimiter)) {
      word.set(row(value))
    } else {
      word.set(row(value).replace(FormatFunctionUtil.convertDelimiter(inDelimiter), FormatFunctionUtil.convertDelimiter(outDelimiter)))
    }
    context.write(word, NullWritable.get())
  }

}

class XlsFormatter extends BaseFormatter [LongWritable, Text, Text, NullWritable]{

  import org.apache.poi.hssf.usermodel.HSSFWorkbook

  val book = new HSSFWorkbook()
  var sheet: HSSFSheet = _
  var rownum, count = 0
  var format:Format = _

  val style = book.createCellStyle()
  style.setAlignment(HSSFCellStyle.ALIGN_CENTER)
  style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER)
  //	style.setFillForegroundColor(150)
  //
  //	val font = book.createFont()
  //	font.setFontName("黑体")
  //	font.setFontHeightInPoints(10)
  //	style.setFont(font)

  def genTitle: Unit = {
    if(formatDetail==null || formatDetail.equals("")) return
    val dtl = format.formDetail.get
    val titles = dtl.excelTitles

    titles.foreach(f=> {
      val rowIndex = f.rowIndex
      val cels = f.cellList

      val row =sheet.createRow(rowIndex)
      rownum +=1

      cels.foreach(cel=> {
        val cell = row.createCell(cel.colIndex.toShort)
        cell.setEncoding(HSSFCell.ENCODING_UTF_16)
        cell.setCellValue(cel.cellName)
        cell.setCellStyle(style)

        if(cel.rowSpan + cel.colSpan >0)
          sheet.addMergedRegion(new Region(rowIndex, cel.colIndex.toShort, rowIndex+ cel.rowSpan -1, (cel.colIndex + cel.colSpan-1).toShort))
      })

    })
  }

  override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, NullWritable]#Context): Unit = {

    getIncode(context)
    if(rownum % 65536 ==0) {
      rownum = 0
      sheet = book.createSheet()
      genTitle
    }

    val erow = sheet.createRow(rownum)
    rownum += 1
    count +=1
    var colnum = 0
    split(row(value)).foreach(col=> {
      val cell = erow.createCell(colnum.toShort)
      cell.setEncoding(HSSFCell.ENCODING_UTF_16)
      cell.setCellValue(col)
      cell.setCellStyle(style)
      colnum += 1
    })

  }

  override def setup(context: Mapper[LongWritable, Text, Text, NullWritable]#Context): Unit = {

    super.setup(context)
    if(formatDetail!=null)  format =Json.parse[Format](formatDetail)
  }

  override def cleanup(context: Mapper[LongWritable, Text, Text, NullWritable]#Context): Unit = {

    val os = new ByteArrayOutputStream()
    book.write(os)
    os.flush()
    os.close()
    word.set(os.toByteArray)
    context.write(word, NullWritable.get())

    super.cleanup(context)

  }

}

object Formatter {

  def main(args: Array[String]): Unit = {

    println(System.currentTimeMillis() + " : " + "start")
    val conf = new JobConf()
    val otherArgs: Array[String] = new GenericOptionsParser(conf, args).getRemainingArgs()

    /**
      * 这里必须有输入/输出
      */
    if (otherArgs.length != 7) {
      println(System.currentTimeMillis() + " : " + "otherArgs.length = " + otherArgs.length)
      System.err.println(System.currentTimeMillis() + " : " + System.currentTimeMillis() + "Usage: FormatPlugin <inPath> <inCode> <inFieldDelimiter> <outPath> <outCode> <outFieldDelimiter> <formatType> <titleString>");
      System.exit(7)
    }

    val inPath: String = otherArgs {
      0
    }
    val inFieldDelimiter: String = FormatFunctionUtil.checkDelimiter(otherArgs {
      1
    })
    val outPath: String = otherArgs {
      2
    }
    val outCode: String = otherArgs {
      3
    }.toUpperCase
    val outFieldDelimiter: String = FormatFunctionUtil.checkDelimiter(otherArgs {
      4
    })
    val formatType: String = otherArgs {
      5
    }
    val titleString: String = otherArgs {
      6
    }.toUpperCase

    val encode: String =
      try {
        Encode.withName(outCode).toString
      } catch {
        case e: Exception => Encode.UTF8.toString
      }
    println("encode:" + encode )

    val format =
      try {
        Json.parse[Format](formatType)
      } catch {
        case e: ParsingException => {
          println(System.currentTimeMillis() + " : " + e.getMessage)
          Format(FormatType.withName(formatType).toString, None)
        }
        case e: Exception => {
          println(System.currentTimeMillis() + " : " + e.getMessage)
          Format(FormatType.JSON.toString, None)
        }
      }

    println("formatChkType:" + format.formType)
    println(System.currentTimeMillis() + " : " + " Start store parameter....")
    DefaultStringifier.store(conf, new Text(inFieldDelimiter), "inFieldDelimiter")
    DefaultStringifier.store(conf, new Text(encode), "outCode")
    DefaultStringifier.store(conf, new Text(outFieldDelimiter), "outFieldDelimiter")
    val formatDetail = if(format.formDetail.nonEmpty) formatType else ""
    DefaultStringifier.store(conf, new Text(formatDetail), "formatDetail")
    DefaultStringifier.store(conf, new Text(titleString), "titleString")

    println(System.currentTimeMillis() + " : " + "Store the parameter end.")

    conf.setJobName("Formatter")
    conf.setNumMapTasks(1)
    val job = new Job(conf)

    println(System.currentTimeMillis() + " : " + "Job is construct.")

    job.setJarByClass(this.getClass) //主类

    val clazz =
    format.formType.toUpperCase match {
      case "XML" => classOf[XmlFormatter]
      case "JSON" => classOf[JsonFormatter]
      case "TXT" => classOf[TxtFormatter]
      case "XLS" => classOf[XlsFormatter]
      case _ =>
        println(System.currentTimeMillis() + " : " + "The fileType is not correct")
        throw new IllegalArgumentException
    }
    job.setMapperClass(clazz); //mapper


    //    job.setCombinerClass(IntSumReducer.class);//作业合成类
    //    job.setReducerClass(IntSumReducer.class);//reducer
    job.setMapOutputKeyClass(classOf[Text]) //设置作业输出数据的关键类
    job.setMapOutputValueClass(classOf[NullWritable]) //设置作业输出值类

    job.setOutputKeyClass(classOf[Text]) //设置作业输出数据的关键类
    job.setOutputValueClass(classOf[NullWritable]) //设置作业输出值类

    job.setNumReduceTasks(0) //map-only job add by lizf

    /**
      * judge encoding
      */
    println(System.currentTimeMillis() + " : " + "judge Out file encoding")
    outCode match {
      case "GBK" => {
        //				println(System.currentTimeMillis() + " : "  + "GBKTextOutputFormat match and \"Set setOutputFormatClass.\" :" +  classOf[GBKTextOutputFormat[Text,NullWritable]].toString)
        job.setOutputFormatClass(classOf[GBKTextOutputFormat[Text, NullWritable]])
      }
      case "UTF8" => {
        //				println(System.currentTimeMillis() + " : " + "TextOutputFormat match and \\\"Set setOutputFormatClass.\\\" :" +  classOf[TextOutputFormat[Text,NullWritable]].toString)
        job.setOutputFormatClass(classOf[TextOutputFormat[Text, NullWritable]])
      }
      case _ => println(System.currentTimeMillis() + " : " + "********  Can't match outCode ********")
    }

    println(System.currentTimeMillis() + " : " + "Set setOutputFormatClass.")

    /**
      * delete output dictionary
      */
    val outPutPath = new Path(outPath)
    val hdfs = FileSystem.get(conf)
    if (hdfs.exists(outPutPath)) hdfs.delete(outPutPath, true)


    FileInputFormat.addInputPath(job, new Path(inPath)) //文件输入
    //		println(System.currentTimeMillis() + " : " + "Set addInputPath.")
    FileOutputFormat.setOutputPath(job, outPutPath) //文件输出
    println(System.currentTimeMillis() + " : " + "Set setOutputPath.")
    println(System.currentTimeMillis() + " : " + "Start process.")
    System.exit(if (job.waitForCompletion(true)) 0 else 1); //等待完成退出.
  }

}