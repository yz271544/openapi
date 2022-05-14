package org.programminginscala.chapter4

/**
  * Created by Administrator on 2016/3/13.
  */
class ChecksumAccumulator {

  private var sum = 0
  def add(b:Byte){sum += b}
  def checksum(): Int = ~(sum & 0xFF) + 1

}

object ChecksumAccumulator{

  import scala.collection.mutable.Map
  private val cache = Map[String,Int]()

  /***
    * 用来计算方法参数String的字符校验和；
    * 以及私有字段cache，缓存之前计算过的校验和的可变映射
    * @param s
    * @return
    */
  def calculate(s:String): Int=
     //检查缓存，看看是否字符串参数已经作为键存在于映射当中。如果是，就返回映射的值
     if(cache.contains(s))
       cache(s)
     //执行计算校验和
     else {
       val acc = new ChecksumAccumulator
       for (c <- s)
         acc.add(c.toByte)
       val cs = acc.checksum()
       cache += (s -> cs)
       cs
     }
}
