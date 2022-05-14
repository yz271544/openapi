package com.teradata.openapi.formatPage


import com.codahale.jerkson.{Json, ParsingException}
import com.teradata.openapi.framework.deploy.{Encode, Format, FormatType}
import com.teradata.openapi.framework.rewrite.GBKTextOutputFormat
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, _}
import org.apache.hadoop.io.{DefaultStringifier, LongWritable, NullWritable, Text}
import org.apache.hadoop.mapreduce.lib.input.{FileInputFormat, FileSplit}
import org.apache.hadoop.mapreduce.lib.output.{FileOutputFormat, TextOutputFormat}
import org.apache.hadoop.mapreduce.{Counter, InputSplit, Job, Mapper}
import org.apache.hadoop.util.GenericOptionsParser

/**
  * Created by hdfs on 2016/6/6.
  */
class MapFormat extends Mapper[LongWritable, Text, Text, NullWritable] {
  private final val defaultDelimiter = '\u0001'

  private var fileType = ""
  //	private var inCode = ""
  //	private var outCode = ""
  private var inDelimiter = ""
  private var outDelimiter = ""
  private var title = ""
  private val word = new Text()

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

  override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, NullWritable]#Context): Unit = {

    /*如何在mapreduce方法中获取当前使用文件（get file name） (2012-07-02 10:58:17)转载▼
        标签： 文件名 数据块 获取 方法 所在 杂谈	分类： Hadoop
    找了好久的方法

    使用的0.20.2版本hadoop
        查了许久，如何在map方法中获取当前数据块所在的文件名，方法如下：
    //获取文件名
    InputSplit inputSplit=(InputSplit)context.getInputSplit();
    String filename=((FileSplit)inputSplit).getPath().getName();
    */
    val inputSplit: InputSplit = context.getInputSplit()
    val filename: String = inputSplit.asInstanceOf[FileSplit].getPath.getName.toUpperCase.trim
    val countPrint: Counter = context.getCounter("Map输出传递Value", filename)
    println("fileName is " + filename)
    val inCode = filename.split('.').reverse.head

    println("Map ++++++ = " + inCode)
    val countPrint1: Counter = context.getCounter("Map输出传递inCode", inCode)
    /*val row = inCode match {
      case "GBK"	=> new String(value.getBytes(), 0, value.getLength, "GBK")
      case "UTF8"	=> value.toString
    }*/

    val row = if (inCode.startsWith("GBK")) {
      new String(value.getBytes(), 0, value.getLength, "GBK")
    } else {
      value.toString
    }


    fileType match {
      case "XML" => {
        word.set(comb((getTitle, split(row)).zipped.toMap))
      }
      case "JSON" => {
        word.set(com.codahale.jerkson.Json.generate((getTitle, split(row)).zipped.toMap))
      }
      case "TXT" => {
        if (inDelimiter.equals(outDelimiter)) {
          word.set(row)
        } else {
          word.set(row.replace(FormatFunctionUtil.convertDelimiter(inDelimiter), FormatFunctionUtil.convertDelimiter(outDelimiter)))
        }
      }
      case _ => println(System.currentTimeMillis() + " : " + "The fileType is not correct")
    }


    println(System.currentTimeMillis() + " : " + System.currentTimeMillis() + "row is : " + row)
    context.write(word, NullWritable.get())

  }

  override def setup(context: Mapper[LongWritable, Text, Text, NullWritable]#Context): Unit = {

    fileType = DefaultStringifier.load(context.getConfiguration, "formatType", classOf[Text]).toString
    //		inCode 			= DefaultStringifier.load(context.getConfiguration, "inCode"		, classOf[Text]).toString
    //		outCode 		= DefaultStringifier.load(context.getConfiguration, "outCode"		, classOf[Text]).toString
    inDelimiter = DefaultStringifier.load(context.getConfiguration, "inFieldDelimiter", classOf[Text]).toString
    outDelimiter = DefaultStringifier.load(context.getConfiguration, "outFieldDelimiter", classOf[Text]).toString
    title = DefaultStringifier.load(context.getConfiguration, "titleString", classOf[Text]).toString
    println(System.currentTimeMillis() + " : " + "fileType is " + fileType)
  }

}


/*

class MapFormatGBK extends Mapper[LongWritable, Text, Text, NullWritable]{
	private final val defaultDelimiter = '\u0001'

	private var fileType = ""
//	private var inCode = ""
	//	private var outCode = ""
	private var inDelimiter = ""
	private var outDelimiter = ""
	private var title = ""
	private val word = new Text()

	def getTitle:List[String] = this.title.split(',').toList

	/*	  private val fileType =  load(conf, "FileType" ,classOf[String])
		  println(System.currentTimeMillis() + " : " + System.currentTimeMillis() + "fileType is " + fileType.toString)*/
	/**
	  * 按照键值对存储
	  * @param mapInData
	  * @return
	  */
	def comb(mapInData: Predef.Map[String, String]) = {
		var result = "<line>"
		for((k,v) <- mapInData) {
			result += s"<${k.toString}>${v.toString}</${k.toString}>"
		}
		result += "</line>"
		result
	}


	def replace(ctext:String):String = ctext.replace(this.inDelimiter, this.outDelimiter)

	def split(str:String):List[String] ={
		val re = this.inDelimiter match {
			case e:String 	=>  str.split(this.inDelimiter).toList
			case _ 			=> 	str.split(this.defaultDelimiter).toList
		}
		re
	}

	override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, NullWritable]#Context): Unit = {
		val inCode = "GBK"
		val row = inCode match {
			case "GBK" 		=> new String(value.getBytes(), 0, value.getLength, "GBK")
			case "UTF8"	=> value.toString
		}


		fileType  match {
			case "XML"  	=> {
				word.set(comb((getTitle,split(row)).zipped.toMap))
			}
			case "JSON" 	=> {
				word.set(com.codahale.jerkson.Json.generate((getTitle,split(row)).zipped.toMap))
			}
			case "TXT" 		=> {
				if(inDelimiter.equals(outDelimiter)) {
					word.set(row)
				} else {
					word.set(row.replace(FormatFunctionUtil.convertDelimiter(inDelimiter), FormatFunctionUtil.convertDelimiter(outDelimiter)))
				}
			}
			case _			=> println(System.currentTimeMillis() + " : " + "The fileType is not correct")
		}


		println(System.currentTimeMillis() + " : " + System.currentTimeMillis() + "row is : " + row)
		context.write(word, NullWritable.get())

	}

	override def setup(context: Mapper[LongWritable, Text, Text, NullWritable]#Context): Unit = {

		fileType 		= DefaultStringifier.load(context.getConfiguration, "formatType" 		, classOf[Text]).toString
//		inCode 			= DefaultStringifier.load(context.getConfiguration, "inCode"		, classOf[Text]).toString
		//		outCode 		= DefaultStringifier.load(context.getConfiguration, "outCode"		, classOf[Text]).toString
		inDelimiter 	= DefaultStringifier.load(context.getConfiguration, "inFieldDelimiter"	, classOf[Text]).toString
		outDelimiter 	= DefaultStringifier.load(context.getConfiguration, "outFieldDelimiter"	, classOf[Text]).toString
		title 			= DefaultStringifier.load(context.getConfiguration, "titleString"	, classOf[Text]).toString
		println(System.currentTimeMillis() + " : " + "fileType is " + fileType)
	}

}
*/

object FormatFunction {

  /**
    * @param args
    * ${inPath} <--${inCode}> ${inFieldDelimiter} ${outPath} ${outCode} ${outFieldDelimiter} ${formatType} ${titleString}
    */
  def main(args: Array[String]) {
    println(System.currentTimeMillis() + " : " + "start")
    val conf: Configuration = new Configuration()
    val otherArgs: Array[String] = new GenericOptionsParser(conf, args).getRemainingArgs()

    /**
      * 这里必须有输入/输出
      */
    if (otherArgs.length != 7) {
      println(System.currentTimeMillis() + " : " + "otherArgs.length = " + otherArgs.length)
      System.err.println(System.currentTimeMillis() + " : " + System.currentTimeMillis() + "Usage: FormatPlugin <inPath> <inCode> <inFieldDelimiter> <outPath> <outCode> <outFieldDelimiter> <formatType> <titleString>");
      System.exit(7);
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
    }.toUpperCase
    val titleString: String = otherArgs {
      6
    }.toUpperCase

    /*		println(System.currentTimeMillis() + " : " + System.currentTimeMillis() + "inPath = |" + inPath + "|")
        println(System.currentTimeMillis() + " : " + System.currentTimeMillis() + "inCode = |" + inCode + "|")
        println(System.currentTimeMillis() + " : " + System.currentTimeMillis() + "otherArgs2 = |" + otherArgs{2} + "|")
        println(System.currentTimeMillis() + " : " + System.currentTimeMillis() + "inFieldDelimiter = |" + inFieldDelimiter + "|")
        println(System.currentTimeMillis() + " : " + System.currentTimeMillis() + "outPath = |" + outPath + "|")
        println(System.currentTimeMillis() + " : " + System.currentTimeMillis() + "outCode = |" + outCode + "|")
        println(System.currentTimeMillis() + " : " + System.currentTimeMillis() + "otherArgs5 = |" + otherArgs{5} + "|")
        println(System.currentTimeMillis() + " : " + System.currentTimeMillis() + "outFieldDelimiter = |" + outFieldDelimiter + "|")
        println(System.currentTimeMillis() + " : " + System.currentTimeMillis() + "formatType = |" + formatType + "|")
        println(System.currentTimeMillis() + " : " + System.currentTimeMillis() + "titleString = |" + titleString + "|")*/

    val encode: String =
    try {
      Encode.withName(outCode).toString
    } catch {
      case e: Exception => Encode.UTF8.toString
    }
    println("encode:" + encode)

    val formatChkType: String =
    try {
      val formatRich: Format = Json.parse[Format](formatType)
      FormatType.withName(formatRich.formType).toString
    } catch {
      case e: ParsingException => {
        println(System.currentTimeMillis() + " : " + e.getMessage)
        FormatType.withName(formatType).toString
      }
      case e: Exception => {
        println(System.currentTimeMillis() + " : " + e.getMessage)
        FormatType.JSON.toString
      }
    }
    println("formatChkType:" + formatChkType)
    println(System.currentTimeMillis() + " : " + " Start store parameter....")
    DefaultStringifier.store(conf, new Text(inFieldDelimiter), "inFieldDelimiter")
    DefaultStringifier.store(conf, new Text(encode), "outCode")
    DefaultStringifier.store(conf, new Text(outFieldDelimiter), "outFieldDelimiter")
    DefaultStringifier.store(conf, new Text(formatChkType), "formatType")
    DefaultStringifier.store(conf, new Text(titleString), "titleString")

    println(System.currentTimeMillis() + " : " + "Store the parameter end.")

    val job = new Job(conf, "FormatPlugin")

    println(System.currentTimeMillis() + " : " + "Job is construct.")

    job.setJarByClass(this.getClass) //主类
    job.setMapperClass(classOf[MapFormat]); //mapper


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

    //    MultipleOutputs.addNamedOutput(job, "format", classOf[TextOutputFormat], classOf[Text], classOf[NullWritable]);
    /**
      * 判断如果多个输入文件就设置为多个输入的格式
      * 一个输入文件就用原来的输入格式
      */
    /*		def getFile(f:File):scala.collection.mutable.Map[String,File]={
          val rs = new scala.collection.mutable.HashMap[String,File]()
          if (f.isDirectory){
            f.listFiles().foreach((s:File) => getFile(s))
          }else{
            if(f.isFile  && f.getName.contains('.') &&
                (f.getName.endsWith("GBK")||f.getName.endsWith("UTF8"))
            ) {
              rs += (f.getName.split('.').reverse.head -> f)
            }
          }
          rs
        }*/

    /*
    println("-----111------")
    def getHDFSFile(inFilePath:Path):scala.collection.mutable.Map[String,Path] = {
      val rs = new scala.collection.mutable.HashMap[String,Path]()
        if(hdfs.getFileStatus(inFilePath).isDirectory) {
//					rs += hdfs.listStatus(inFilePath).map((x:FileStatus) => getHDFSFile(x.getPath))
          hdfs.listStatus(inFilePath).foreach((x:FileStatus) => {rs ++= getHDFSFile(x.getPath)})
        } else {
          val tmp = inFilePath.getName.toUpperCase
          if(hdfs.getFileStatus(inFilePath).isFile  && tmp.contains('.') &&
              (tmp.endsWith("GBK")||tmp.endsWith("UTF8"))
          ) {
            rs += (tmp.split('.').reverse.head -> inFilePath)
            println("Path is " + inFilePath + " | " + inFilePath.toString)
          }
        }
      rs
    }
    println("-----222------")

    val pathMap = getHDFSFile(new Path(inPath))
    println("-----333------")
    println(pathMap)

    for((code:String, pathName:Path) <- pathMap){
      println("--++ " + code + " : " + pathName )
      (code, pathName) match {
        case ("GBK", pathName)	=> {
          println("GBK")
          MultipleInputs.addInputPath(job, pathName, classOf[TextInputFormat], classOf[MapFormatGBK])
        }
        case ("UTF8",pathName)	=> {
          println("UTF8")
          MultipleInputs.addInputPath(job, pathName, classOf[TextInputFormat], classOf[MapFormat])
        }
        case _ 		=> println("can't match")

      }
    }
    println("-----444------")
*/

    FileInputFormat.addInputPath(job, new Path(inPath)) //文件输入
    //		println(System.currentTimeMillis() + " : " + "Set addInputPath.")
    FileOutputFormat.setOutputPath(job, outPutPath) //文件输出
    println(System.currentTimeMillis() + " : " + "Set setOutputPath.")
    println(System.currentTimeMillis() + " : " + "Start process.")
    System.exit(if (job.waitForCompletion(true)) 0 else 1); //等待完成退出.

  }


}