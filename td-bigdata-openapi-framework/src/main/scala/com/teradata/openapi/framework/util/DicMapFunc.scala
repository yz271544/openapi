package com.teradata.openapi.framework.util

import com.codahale.jerkson.Json._
import com.teradata.openapi.framework.model.DataSnpst

import scala.collection.immutable.TreeMap
import scala.collection.mutable.ListBuffer
import scala.collection.{Map, mutable}
import scala.reflect.ClassTag
import scala.util.parsing.json.JSON
/**
  * Created by John on 2016/4/21.
  */
trait DicMapFunc {

  type parseMuMapType = mutable.Map[String, Any]
  type parseImuMapType = Map[String, Any]

  /**
    * 将a识别或转为mutable.ArraySeq[Any]
    *
    * @param a ：识别对象
    * @return
    */
  def ckToArraySeq(a: Any): mutable.ArraySeq[Any] = {
    //println("a:" + a)
    var parseList = mutable.ArraySeq[Any]()
    type parseListType = Seq[Any]
    val tempList = a match {
      case list: parseListType => list
      case _ => Seq()
    }

    parseList = mutable.ArraySeq(tempList: _*)
    //parseList1.foreach(println)

    //println("parseList:" + parseList)
    parseList
  }

  /**
    * 将a识别或转为Set[Any]
    *
    * @param a 识别对象
    * @return
    */
  def ckToSet(a: Any): Set[Any] = {
    var parseList = mutable.ArraySeq[Any]()
    type parseListType = Seq[Any]
    val tempList = a match {
      case list: parseListType => list
      case Some(lists: parseListType) => lists
      case _ => Seq()
    }
    parseList = mutable.ArraySeq(tempList: _*)
    parseList.toSet
  }

  /**
    * 将a识别或转为Set
    *
    * @param a 识别对象
    * @tparam T 识别对象类型
    * @return
    */
  def ckSet[T: ClassTag](a: T): Set[T] = {
    type parseListType = Set[T]
    val parseList = a match {
      case set: parseListType => set
      case _ => Set(a)
    }
    parseList
  }

  /**
    * 识别a是否为Map，并将结果放到mutable.Map[String, Any]中，如果不是Map，则结果放Map()
    *
    * @param a 识别对象
    * @return
    */
  def ckMap(a: Any): mutable.Map[String, Any] = {
    //println("a:" + a)
    type parseListType = Map[String, Any]
    val ppList: mutable.Map[String, Any] = mutable.Map()
    val parseList = a match {
      case map: parseListType => map
      case _ => Map()
    }
    //println("parseList:" + parseList)
    ppList ++ parseList
  }

  def mapTolist(a: Map[Any,List[Any]]): List[List[Any]] = {
    var list = List[List[Any]]()
    a.foreach(e => {
      val (k,v) = e
      list = list :+ List(k,v)
    })
    list
  }

  def mapTolistGenel(a: Map[Any,Any]): List[List[Any]] = {
    var list = List[List[Any]]()
    a.foreach(e => {
      val (k,v) = e
      list = list :+ List(k,v)
    })
    list
  }

  def mapToTreeMapVallst(a: Map[String,Any]): TreeMap[String, List[Any]] = {
    var treeMap = TreeMap[String,List[Any]]()
    a.foreach(e => {
      val (k,v) = e
      treeMap += (k -> ckList(v))
    })
   treeMap
  }


  def treeMapToMapVallst(a: TreeMap[String,Any]): scala.collection.immutable.Map[String, List[Any]] = {
    var map = scala.collection.immutable.Map[String,List[Any]]()
    a.foreach(e => {
      val (k,v) = e
      map += (k -> ckList(v))
    })
    map
  }

  def upperTreeMapKey(jsonCondtionMap: TreeMap[String, List[Any]]): TreeMap[String, List[Any]] = {
    //var upperCondtionMap: TreeMap[String, List[Any]] = TreeMap[String, List[Any]]()
    val upperCondtionMap: TreeMap[String, List[Any]] = for((k,v) <- jsonCondtionMap if !("" equals k)) yield (k.toUpperCase, v)
    upperCondtionMap
  }
  /**
    * 识别Json a转为mutable.Map[String, Any]，如果转换失败，返回mutable.Map()
    *
    * @param a 识别对象
    * @return
    */
  def jsonToMap(a: String): mutable.Map[String, Any] = {
    type parseMapType = scala.collection.immutable.Map[String, Any]
    val ppList: mutable.Map[String, Any] = mutable.Map()
    val parseList = JSON.parseFull(a) match {
      case Some(map: parseMapType) => map
      case None => scala.collection.immutable.Map()
      case _ => scala.collection.immutable.Map()
    }
    ppList ++ parseList
  }

  def jsonToMapLML(a:String):mutable.Map[String,Any] = {
    type parseMapType = Map[String, List[Map[String,List[Any]]]]
    val map = mutable.Map[String, Any]()
    val initMap = parse[parseMapType](a)
    //println("initMap:"+initMap)
    initMap.foreach( e => {
      val (k,v)=e
      //println("Key:" + k + " Value:" + v)
      map += (k -> v)
    })
    map
  }

  /**
    * 识别两个Map的Keys是否一致，一致返回true,不一致返回false
    *
    * @param a 识别对象A
    * @param b 识别对象B
    * @tparam K 键类型
    * @tparam V 值类型
    * @return true  false
    */
  def mapKeySetEquels[K <: String, V](a: Map[K, V], b: Map[K, V]): Boolean = {
    var isMapKeySetEquels = false
    if (((a.keySet | b.keySet) &~ a.keySet).isEmpty && ((a.keySet | b.keySet) &~ b.keySet).isEmpty)
      isMapKeySetEquels = true
    isMapKeySetEquels
  }

  def compareList[A](a:List[A],b:List[A]):Boolean = {
    if ((a diff b).isEmpty && (b diff a).isEmpty)
      true
    else false
  }
  /**
    * 笛卡尔生成器 val columns = TreeMap("deal_date" -> List(201601, 201602, 201603), "region_code" -> List("00", "01", "02", "03"))
    * cartesianProductStr(columns.keySet.map(columns).toList)
    *
    * @param xss :  List(List(201601, 201602, 201603), List(00, 01, 02, 03))
    * @tparam T : 任何类型
    * @return List(List()):  List(List(201601, 00), List(201601, 01), List(201601, 02), List(201601, 03), List(201602, 00), List(201602, 01), List(201602, 02), List(201602, 03), List(201603, 00), List(201603, 01), List(201603, 02), List(201603, 03))
    */
  def cartesianProductStr[T](xss: List[List[T]]): List[List[T]] = xss match {
    case Nil => List(Nil)
    case h :: t => for (xh <- h; xt <- cartesianProductStr(t)) yield xh :: xt
  }

  /**
    * 将笛卡尔集重构为Map
    *
    * @param cartesian 笛卡尔集: List(List(201601, "00"), List(201601, "01"), List(201601, "02"), List(201601, "03"), List(201602, "00"), List(201602, "01"), List(201602, "02"), List(201602, "03"), List(201603, "00"), List(201603, "01"), List(201603, "02"), List(201603, "03"))
    * @param columns   字段Map：TreeMap("deal_date" -> List(201601, 201602, 201603), "region_code" -> List("00", "01", "02", "03"))
    * @return 返回结果是将笛卡尔集，重新构造成为Set(Map)的 组合。 Set(Map(deal_date -> List(201601), region_code -> List(03)), Map(deal_date -> List(201602, 201603), region_code -> List(00, 01, 02, 03)))
    */
  def redistributeMap(cartesian: List[List[Any]], columns: TreeMap[String, List[Any]]): TreeMap[String, Any] = {
    //var reSet = Set[Any]()
    var reMap = TreeMap[String, Any]()
    val columnKeyName = columns.keySet.toList.reverse
    if (cartesian.flatten.isEmpty){
      columns.foreach(e=>{
        val (columnName,columnVals) = e
        reMap += (columnName -> List())
      })
      return reMap
    }
    val cartesianBuf = cartesian.map(ListBuffer[Any](_: _*))
    val cartesianBuffer = ListBuffer[ListBuffer[Any]]()
    val cartesianBufferNext = ListBuffer[ListBuffer[Any]]()
    cartesianBuf.foreach(e => {
      cartesianBuffer += e
    })
    var i = columns.size - 1
    var j = 0
    for (loop <- columnKeyName if i >= 0) {
      var bufferList = List[Any]()
      var groupByLast = List[Any]()
      for (elem <- cartesianBuffer) {
        //List从右往左获取数据，elem可能是ListBuffer(elem...)，也可能是ListBuffer(List(elem...),List(resElem...))
        val aggregationVal = if (j > 0) ckList(ckList(elem).head)(i) else ckList(ckList(elem))(i)
        //上一次的聚合值 aggregationValLast
        val aggregationValLast = ckList(ckList(elem).tail).flatten(ckList)
        val groupByThisTmp = if (j > 0) (ckList(ckList(elem).head) diff List(aggregationVal)).flatten(ckList) else (ckList(ckList(elem)) diff List(aggregationVal)).flatten(ckList)
        //groupByThis:本次循环的elem中，除去聚合字段，剩余的维度字段；groupByLast：上一个循环elem，除去聚合字段，剩余的维度字段；
        val groupByThis = if (j > 0 && groupByThisTmp.nonEmpty && aggregationValLast.nonEmpty) List(groupByThisTmp, aggregationValLast)
        else if (j > 0 && groupByThisTmp.nonEmpty && aggregationValLast.isEmpty) groupByThisTmp
        else if (j > 0 && groupByThisTmp.isEmpty && aggregationValLast.nonEmpty) aggregationValLast
        else groupByThisTmp
        if (!(((groupByThis diff groupByLast).isEmpty && (groupByLast diff groupByThis).isEmpty) || groupByLast.isEmpty)) {
          cartesianBufferNext += ListBuffer(groupByLast, bufferList)
          bufferList = bufferList intersect List()
        }
        //bufferList存放聚合值的剔重集
        bufferList = (bufferList ++ List(aggregationVal).flatten(ckList)).distinct
        //println("OUT elem:" + elem + " i:" + i + " j:" + j + " elem(i):" + aggregationVal + " diffSet:" + groupByThis + " groupByThis:" + groupByThis + " groupByLast:" + groupByLast + " bufferList:" + bufferList)
        groupByLast = groupByThis
        cartesianBuffer.drop(i)
      }
      cartesianBufferNext += ListBuffer(groupByLast, bufferList)
      i = i - 1
      j = j + 1
      cartesianBuffer.clear()
      cartesianBuffer ++= cartesianBufferNext
      cartesianBufferNext.clear()
    }
    val cartesianBufferTotalNew = restructureListDic(cartesianBuffer)
    //println("cartesianBufferTotalNew:" + cartesianBufferTotalNew)
    cartesianBufferTotalNew.foreach(e => {
      var i = 0
      //println( "ckList(e):"+ckList(e) + " columnKeyName(i):" + columnKeyName(i))
      ckList(e).foreach(e => {
        //println("e:" + e)
        if (ckList(e).nonEmpty) {
          reMap += (columnKeyName(i) -> e)
          i = i + 1
        }
      })
      //reSet += reMap
    })
    //reSet
    reMap
  }
  /**
    * 本接口专属函数，将ListBuffer[ListBuffer[Any]扁平化为List[Any]
    *
    * @param listbufferDic : 识别目标
    * @return
    */
  private def restructureListDic(listbufferDic: ListBuffer[ListBuffer[Any]]): List[Any] = {
    //ListBuffer[ListBuffer[Any]]()
    var restructListDic = List[Any]()
    for (elem <- listbufferDic) {
      val mergeList = mergeListDic(elem)
      //println("restructureListDic mergeList: " + mergeList)
      restructListDic = restructListDic ++ List(mergeList)
    }
    restructListDic
  }

  /**
    * 本接口专属函数，将ListBuffer[Any]扁平化为List[Any]
    * a可能为 List(List,List) List(A,B,C,D)
    *
    * @param a :识别目标
    * @return
    */
  private def mergeListDic(a: ListBuffer[Any]): List[Any] = {
    type NestList = List[Any]
    val mergeList =
      a.head match {
        case ((nestlisthead: NestList) :: (nestlisttail: NestList)) => ckList(a.head).:+(ckList(a.tail).flatten(ckList)) //ckList(a.tail).flatten(flat2) :: List(ckList(nestlist))
        case nestlist: NestList => List(ckList(a.head)).:+(ckList(a.tail).flatten(ckList))
        case _ => List(a.head)
      }
    mergeList
  }

  def ckNestList[T](a: List[Any]): List[List[Any]] = {
    type parseListType = List[List[T]]
    //type parseListBuffer = ListBuffer[ListBuffer[T]]
    val tempList = a match {
      case list: parseListType => list
      //case lb : parseListBuffer => lb.toList  //listbfToList(lb)
      case _ => List(List(a))
    }
    tempList
  }
  /**
    * 检查入参any是否为List、ListBuffer、A，分别转为List
    *
    * @param any 识别目标
    * @return
    */
  def ckList(any: Any): List[Any] =
    any match {
      case i: List[_] => i
      case j: ListBuffer[_] => j.toList
      case _ => List(any)
    }

  def convert(any :Any,convertType :String): Any =
    any match {
      case i: String =>
        try {
          convertType match {
            case "Integer" => i.toInt
            case "Double" => i.toDouble
            case "String" => i
            case "Short" => i.toShort
            case "Long" => i.toLong
            case "DATE" => i.toString
            case _ => i
          }
        } catch {
          case e:Exception => throw e
        }
      case _ => any
    }

  def treeMap2ListDataSnpst(jsonCondtionMap: TreeMap[String, List[Any]]): List[DataSnpst] ={
    var listDataSnpst: List[DataSnpst] = List[DataSnpst]()
    for (elem <- jsonCondtionMap) {
      listDataSnpst = listDataSnpst.::(DataSnpst(elem._1, elem._2))
    }
    listDataSnpst
  }

  def listDataSnpst2TreeMap(listDataSnpst: List[DataSnpst]): TreeMap[String, List[Any]] ={
    var jsonCondtionMap: TreeMap[String, List[Any]] = TreeMap[String, List[Any]]()
    for (elem: DataSnpst <- listDataSnpst) {
       jsonCondtionMap += (elem.columnName -> elem.columnValues)
    }
    jsonCondtionMap
  }


  def mergedsort[T](less: (T, T) => Boolean)(input: List[T]): List[T] = {
    /**
      * @param xList 要合并的有序列表
      * @param yList 要合并的有序列表
      * @return 合并的有序列表
      */
    def merge(xList: List[T], yList: List[T]): List[T] =
      (xList, yList) match {
        case (Nil, _) => yList
        case (_, Nil) => xList
        case (x :: xtail, y :: ytail) =>
          if (less(x, y)) x :: merge(xtail, yList)
          else y :: merge(xList, ytail)
      }
    val n = input.length / 2
    if (n == 0) input
    else {
      val (x, y) = input splitAt n //把要排序的列表input平均分成两个列表
      merge(mergedsort(less)(x), mergedsort(less)(y)) //先对分后的两个列表归并排序，再对排好的有序表进行归并
    }
  }

  def mergedsortBySmallToBig[T] = mergedsort((x: T, y: T) => x.toString < y.toString) _

  def mergedsortByBigToSmall[T] = mergedsort((x: T, y: T) => x.toString > y.toString) _
}
