package regular

import java.util

import scala.collection.mutable
import scala.util.matching.Regex
import scala.collection.JavaConverters._
object ExpressionParser {

  val operation = Map("eq" -> "=", "gt" -> ">", "lt" -> "<", "ge" -> ">=", "le" -> "<=", "in" -> "in", "nin" -> "not in")
  val logicOper = Map("&&" -> " and ", "\\|\\|" -> " or ")

  val pattern: Regex = "(\\(|\\)|\\&|\\|)".r
  val matchArr: (String) => Array[String] = (originalExpression: String) => pattern.split(originalExpression).filter(m => m.trim.length > 0).map(m => m.trim)


  private def extractArgs(equation: String): (String, String, String) = {
    val operArrToStr: String = operation.keys.map(e => e.mkString("\\[", "", "\\]")).mkString("|")
    val pattern = new Regex(s"(.+)($operArrToStr)(.+)", "propName", "operation", "propThreshold")
    val prop = pattern.unapplySeq(equation)
    val (propName, oper, propThreshold) = prop match {
      case Some(v) => (v.head, v(1), v(2))
      case _ =>
        throw new ExpressionParserException(s"Invalid expression is $equation")
        //("","","")
    }
    (propName, oper, propThreshold)
  }

  def expressionParser(originalExpression: String): String = {
    val splitArr = matchArr(originalExpression)
    var argsMap = mutable.Map[String, String]()
    var i = 0
    for (elem <- splitArr) {
      argsMap += ("arg" + i -> elem)
      i += 1
    }
    val argsMap2 = argsMap.map(m => (m._1, m._2.replaceAll("\\[", "\\\\[").replaceAll("\\]", "\\\\]"))).toMap
    //把Value替换成arg$i为Key的值
    val deal1 = MyStringUtils.replaceByValueToKey(originalExpression, argsMap2)
    val argsMap4 = argsMap.map(m => {
      val (propName: String, oper: String, propThreshold: String) = extractArgs(m._2)
      val solvePropThreshold = MyStringUtils.splitAroundLink(propThreshold, ",", "'", ",")
      if (List("[in]","[nin]") contains oper){
        (m._1, propName + oper + "(" + solvePropThreshold + ")")
      } else {
        (m._1, propName + oper + solvePropThreshold)
      }
    }).toMap
    val argsMap3 = argsMap4.map(m => (m._1, MyStringUtils.replaceByKeyBrackets(m._2, operation)))
    //将args$i的Key值替换回处理过的Value
    //val deal2 = StringUtils.replaceByKey(deal1, argsMap3)
    //val deal3 = StringUtils.replaceByKey(deal2, logicOper)
    //deal3
    MyStringUtils.replaceByKey(MyStringUtils.replaceByKey(deal1, argsMap3), logicOper)
  }

  def getColumns(originalExpression: String): List[String] = {
    val splitArr = matchArr(originalExpression)
    splitArr.map(e => extractArgs(e)._1).toList
  }

  def getColumnsInJava(originalExpression: String): util.List[String] = {
    getColumns(originalExpression).asJava
  }

  def main(args: Array[String]): Unit = {
    val orgi = """(deal_date[eq]201708&&prodt_code[in]abc,123)||total_flow[ge]5000"""
    println(getColumns(orgi))
    val deal3 = expressionParser(orgi: String)
    println(deal3)
    //println("propName:" + propName + " operation:" + oper + " propThreshold:" + propThreshold)
    //val oper: String = pattern2.replaceAllIn(a, m => s"${m group "operation"}")
    //println("oper:" + oper)
    /*val vars = Map("x" -> "a var", "y" -> """some $ and \ signs""")
    val text = "A text with variables %x, %y and %z."
    val varPattern = """%(\w+)""".r
    val mapper = (m: Match) => vars get (m group 1) map (quoteReplacement(_))
    val repl = varPattern replaceSomeIn (text, mapper)

    println(repl)*/


    /*import scala.util.matching.Regex
    val datePattern = new Regex("""(\d\d\d\d)-(\d\d)-(\d\d)""", "year", "month", "day")
    val text2 = "From 2011-07-15 to 2011-07-17"
    val repl2 = datePattern replaceAllIn (text2, m => s"${m group "month"}/${m group "day"}")
    println(repl2)*/


    /*val pattern = "Scala".r
    val str = "Scala is Scalable and cool"

    println(pattern findFirstIn str)
    val aaa = pattern.findAllMatchIn(str)
    for (elem <- aaa) {
      println(elem)
    }*/
  }
}
