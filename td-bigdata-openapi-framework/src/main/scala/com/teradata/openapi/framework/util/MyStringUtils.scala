package com.teradata.openapi.framework.util

import java.io.UnsupportedEncodingException

object MyStringUtils {

  /**
    * Replace the origStr contains replaceMap characters contained in the Key content of the Value
    * 把origStr中的Key字符串替换成Value字符串
    *
    * @param origStr    要被替换的字符串
    * @param replaceMap 替换关系Map，把Key替换为Value
    * @return 替换结果
    */
  def replaceByKey(origStr: String, replaceMap: Map[String, String]): String = {
    var targetStr: String = origStr
    replaceMap.foreach(arg => targetStr = targetStr.replaceAll(arg._1, arg._2))
    targetStr
  }

  def replaceByKey(origStr: String, replaceMap: Map[String, String], default: String): String = {
    //println("origStr:" + origStr + " replaceMap:" + replaceMap)
    var targetStr: String = origStr
    replaceMap.foreach(arg => targetStr = targetStr.replaceAll(arg._1, if(arg._2.isEmpty) default else arg._2))
    targetStr
  }

  def replaceByKeyBrackets(origStr: String, replaceMap: Map[String, String]): String = {
    var targetStr: String = origStr
    replaceMap.foreach(arg => targetStr = targetStr.replaceAll("\\["+ arg._1 + "\\]", " " + arg._2 + " "))
    targetStr
  }

  def replaceByValueToKey(origStr: String, replaceMap: Map[String, String]): String = {
    var targetStr: String = origStr
    replaceMap.foreach(arg => targetStr = targetStr.replaceAll(arg._2, arg._1))
    targetStr
  }
  /**
    * 把origStr字符串中包含了replacedList中的元素字符串，统一替换为replaceValue
    *
    * @param origStr      要被替换的原始字符串
    * @param replacedList 要被替换的元素
    * @param replaceValue 要被统一替换的值
    * @return 替换结果
    */
  def replaceDefault(origStr: String, replacedList: List[String], replaceValue: String): String = {
    var targetStr: String = origStr
    replacedList.foreach(arg => targetStr = targetStr.replaceAll(arg, replaceValue))
    targetStr
  }

  /**
    * 把字符串按splitStr分割为List，然后将每个元素用aroundStr围绕，最后用linkStr连接为一个字符串
    * elem.split(",").map(e => e.mkString("'", "", "'")).mkString("", ",", "")
    * "A,B,C" => 'A','B','C'
    *
    * @param origStr   = "A,B,C"
    * @param splitStr  = ","  按splitStr分割字符串
    * @param aroundStr = "'" 将分割后的每个元素用该字符围绕
    * @param linkStr   = "," 用linkStr连接元素为一个字符串
    * @return = 'A','B','C'
    */
  def splitAroundLink(origStr: String, splitStr: String, aroundStr: String, linkStr: String): String = {
    origStr.split(splitStr).map(e => e.mkString(aroundStr, "", aroundStr)).mkString("", linkStr, "")
  }

  /**
    * 将List[String]中的每个字符串用splitAroundLink对其元素挨个处理，形成新的List
    * List("A,B,C","D,E,F") => List("'A','B','C'","'D','E','F'")
    *
    * @param orgiList = List("A,B,C","D,E,F")
    * @return = List("'A','B','C'","'D','E','F'")
    */
  def dealElemBySplitAroundLink(orgiList: List[String]): List[String] = {
    orgiList.map(e => splitAroundLink(e, ",", "'", ","))
  }

  /**
    * 将List[String]中每个元素都合并起来成为一个String，中间用splitStr拼接，最终在放到List[String]中，其中只有一个元素
    * @param orgiList = List("'A'","'B'","'C'")
    * @param splitStr = "|"
    * @return List("'A'|'B'|'C'")
    */
  def dealElemMosaicOnlyOne(orgiList: List[String],splitStr: String): List[String] = {
    List(orgiList.mkString(splitStr))
  }

  /**
    * 将字符串转换为十六进制字符串
    */
  private val hexString: String = "0123456789ABCDEF"

  def encodeHex(str: String): String = {
    val bytes = str.getBytes()
    val sb = new StringBuilder(bytes.length * 2)
    for (i <- 0 until bytes.length) {
      sb.append(hexString.charAt((bytes(i) & 0xf0) >> 4))
      sb.append(hexString.charAt(bytes(i) & 0x0f))
    }
    sb.toString()
  }

  @throws[UnsupportedEncodingException]
  def strFromHex(hex: String): String = {
    val hex1: String = hex.replaceAll("^(00)+", "")
    val bytes = new Array[Byte](hex1.length / 2)
    var i = 0
    while (i < hex1.length) {
      {
        bytes(i / 2) = ((Character.digit(hex1.charAt(i), 16) << 4) + Character.digit(hex1.charAt(i + 1), 16)).toByte
      }
      i += 2
    }
    new String(bytes)
  }
}
