package com.teradata.openapi.logger.model


/**
  * Created by Evan on 2016/6/7.
  */
trait LoggerModels extends Serializable{}

object LoggerModels {}


/**
  * 日志详单信息
  * @param doTime 时间
  * @param modName 模块名称
  * @param lvl 日志输出级别
  * @param className 类名称
  * @param threadName 线程名称
  * @param threadId 线程ID
  * @param contents 日志输出内容
  */
case class LogDetailInfo(doTime:String,
                         modName:String,
                         lvl:String,
                         className:String,
                         threadName:String,
                         threadId:String,
                         contents:String) extends LoggerModels

/**
  * API访问信息
  * @param doTime 时间
  * @param appkey appKey
  * @param callMethod api名称
  * @param reqType 请求类型
  * @param apiVersion api版本号
  * @param threadId 处理线程id
  */
case class LogApiVisitInfo(doTime:String,
                           appkey:String,
                           callMethod:String,
                           reqType:String,
                           apiVersion:String,
                           threadId:String
                          ) extends LoggerModels

/**
  * API线程处理记录信息
  * @param doTime 时间
  * @param dealCell 处理单元
  * @param parentThreadId 父级线程id
  * @param threadId 线程id
  * @param contents 日志输出内容
  */
case class ApiThreadProcessInfo(doTime:String,
                                dealCell:String,
                                parentThreadId:String,
                                threadId:String,
                                contents:String) extends LoggerModels


/**
  * API线程关系信息
  * @param sentThreadId
  * @param recvThreadId
  */
case class ApiThreadProcessRelation(sentThreadId:String,recvThreadId:String)