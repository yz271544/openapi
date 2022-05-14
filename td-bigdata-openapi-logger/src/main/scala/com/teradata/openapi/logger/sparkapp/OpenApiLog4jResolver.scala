package com.teradata.openapi.logger.sparkapp

import com.netaporter.uri.Uri
import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.logger.dao.{ApiThreadProcessInfoDao, ApiThreadProcessRelationDao, LogApiVisitInfoDao, LogDetailInfoDao}
import com.teradata.openapi.logger.model.{ApiThreadProcessInfo, ApiThreadProcessRelation, LogApiVisitInfo, LogDetailInfo}
import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Durations, StreamingContext}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer


/**
  * Created by Evan on 2016/6/7.
  */
object OpenApiLog4jResolver extends OpenApiLogging{

   def main(args: Array[String]) {

      val sparkConf = new SparkConf().setMaster("spark://sparkmaster:7077").setAppName("OpenApiLog4jResolver")
      val ssc = new StreamingContext(sparkConf,Durations.seconds(15))
      var kafkaParameters: Map[String, String] = Map[String,String]()
      kafkaParameters += ("metadata.broker.list" -> "sparkmaster:9092,sparkwork1:9092,sparkwork2:9092")
      var topics = Set[String]()
      topics += "openapilog4j"

      val logDetailInfoDao = new LogDetailInfoDao()
      val logApiVisitInfoDao = new LogApiVisitInfoDao()
      val apiThreadProcessInfoDao = new ApiThreadProcessInfoDao()
      val apiThreadProcessRelationDao = new ApiThreadProcessRelationDao()

      val inputStream: InputDStream[(String, String)] = KafkaUtils.createDirectStream[String,String,
        StringDecoder,StringDecoder](ssc,kafkaParameters,topics)

      val logdetail: DStream[String] = inputStream.map{
         input => val splited:Array[String] = input._2.split("\\^")
            var dotime, modname, lvl, classname, threadname, threadid, contents = ""
          if(splited.length == 8){
             dotime = splited(1)
             modname = splited(2)
             lvl = splited(3)
             classname = splited(4)
             threadname = splited(5)
             threadid = splited(6)
             contents = splited(7)
          }else{
             dotime = "-"
             modname = "-"
             lvl = "-"
             classname = "-"
             threadname = "-"
             threadid = "-"
             contents = "-"
          }
            val logRecord = dotime + "^" + modname + "^" + lvl + "^" + classname + "^" + threadname + "^" + threadid + "^" + contents
            logRecord
      }

     logdetail.print

      //日志序列集
      var logSeq = ListBuffer[LogDetailInfo]()
      //把每条日志添加到log_info_detail中
      logInfo("循环遍历日志信息")
      logdetail.foreachRDD((rdd: RDD[String]) =>{
        //logInfo("----rdd 的Partition总数---"+rdd.getNumPartitions)
         /*rdd.foreachPartition((partition: Iterator[String]) =>{
           logInfo("----每个rdd的partition总数-----"+partition.size)
             while(partition.hasNext){
               val recored: String = partition.next()
               val splited = recored.split("\\^")
               val loginfo: LogDetailInfo = LogDetailInfo(splited(0),splited(1),splited(2),splited(3),splited(4),splited(5),splited(6))
               logInfo("*****"+loginfo)
               logSeq :+ loginfo
             }
         })*/
        rdd.collect.foreach((recored: String) =>{
          logInfo("----recored-----"+recored)
          val splited = recored.split("\\^")
          val doTime = splited(0).trim
          val modName = splited(1).trim
          val lvl = splited(2).trim
          val className = splited(3).trim
          val threadName = splited(4).trim
          val threadId = splited(5).trim
          val contents = splited(6).trim
          val loginfo: LogDetailInfo = LogDetailInfo(doTime,modName,lvl,className,threadName,threadId,contents)
          //保存日志流水信息
          logDetailInfoDao.saveLogDetailInfo(loginfo)
          //解析API请求URL
          if("openapi-client".equals(modName.trim)
            && "INFO".equals(lvl.trim)
            && !"1".equals(threadId.trim)){
            val pattern = "http://".r
            val patternedStr = pattern findFirstIn contents
            if("http://".equals(patternedStr.getOrElse())){
              val urlParameterMap: Map[String, Seq[String]] = Uri.parse(contents).query.paramMap
              var appKey = ""
              var callWay = ""
              var reqType = ""
              var version = ""
              if(urlParameterMap.contains("appKey")){
                appKey = urlParameterMap("appKey").head.trim
              }
              if(urlParameterMap.contains("method")){
                callWay = urlParameterMap("method").head.trim
              }
              if(urlParameterMap.contains("reqType")){
                reqType = urlParameterMap("reqType").head.trim
              }
              if(urlParameterMap.contains("version")){
                version = urlParameterMap("version").head.trim
              }
              val logApiVisitInfo = LogApiVisitInfo(doTime,appKey,callWay,reqType,version,threadId)
              //保存API访问信息
              //logInfo("保存API访问信息")
              logApiVisitInfoDao.saveLogApiVisitInfo(logApiVisitInfo)
            }

          }

          //处理具体每个API的线程处理过程
          //①处理client端部分
          if("openapi-client".equals(modName.trim)
            && "INFO".equals(lvl.trim)
            && !"1".equals(threadId.trim)
            && "org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean".equals(threadName.trim)){
            val apiThreadProcessInfo = new ApiThreadProcessInfo(doTime,modName,null,threadId,contents)
            apiThreadProcessInfoDao.saveApiThreadProcessInfo(apiThreadProcessInfo)
          }

          //②处理master端部分
          if("openapi-master".equals(modName.trim)
            && "INFO".equals(lvl.trim)
            && !"1".equals(threadId.trim)
            && "openapiMaster-akka.actor.default-dispatcher".equals(threadName.trim)){
            val pattern = "接收到".r
            val patternedStr = pattern findFirstIn contents
            var parentToSonMap: mutable.Map[String, String] = scala.collection.mutable.Map()
            if("接收到".equals(patternedStr.getOrElse())){
              val revThreadId = contents.split("\\:")(1).trim
              parentToSonMap += (threadId -> revThreadId)
              //val apiProcessWithRel = new ApiThreadProcessRelation(revThreadId,threadId)
              //apiThreadProcessRelationDao.saveApiThreadProcessRelation(apiProcessWithRel)
              val apiThreadProcessInfo = new ApiThreadProcessInfo(doTime,modName,revThreadId,threadId,contents)
              apiThreadProcessInfoDao.saveApiThreadProcessInfo(apiThreadProcessInfo)
            }else{
              //判断当前的线程id是否有对应的父线程id
              //如果有则把对应的父线程id保存到对应字段中，如果没有的话则保存null
              if(parentToSonMap.contains(threadId)) {
                val apiThreadProcessInfo = new ApiThreadProcessInfo(doTime, modName, parentToSonMap.get(threadId).get, threadId, contents)
                apiThreadProcessInfoDao.saveApiThreadProcessInfo(apiThreadProcessInfo)
              }else{
                val apiThreadProcessInfo = new ApiThreadProcessInfo(doTime, modName, null, threadId, contents)
                apiThreadProcessInfoDao.saveApiThreadProcessInfo(apiThreadProcessInfo)
              }

            }
          }

        })
      })



     ssc.start()
     ssc.awaitTermination()
     ssc.stop()

   }

}































