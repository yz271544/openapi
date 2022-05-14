package org.programminginscala.chapter6

/**
  * Created by Administrator on 2016/3/25.
  */
class Rational(n:Int,d:Int) {

  require(d != 0)

  private val g = gcd(n.abs,d.abs)
  //添加字段
  val numer : Int = n / g
  val denom : Int = d / g

  //辅助构造器
  def this(n: Int) = this(n,1)

  override def toString = numer + "/" + denom

  def add(that:Rational):Rational =
     new Rational(
       numer * that.denom + that.numer * denom,
       denom * that.denom
     )

  private def gcd(a:Int,b:Int):Int =
  if (b == 0) a else gcd(b,a % b)

}

object Rational{

  def main(args: Array[String]) {
    val tmp = new Rational(66,42)
    println(tmp)
  }
}
