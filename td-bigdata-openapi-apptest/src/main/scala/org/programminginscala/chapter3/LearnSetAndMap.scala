package org.programminginscala.chapter3

/**
  * Created by Administrator on 2016/3/12.
  */
class LearnSetAndMap{


}
object LearnSetAndMap{

  def main(args: Array[String]) {

    /**mutable Set */
    /*import scala.collection.mutable.Set

    var jetSet = Set("Boeing","AirBus")
    jetSet += "Lear"
    println(jetSet.contains("Cessna"))*/

    /**mutable Map*/
    import scala.collection.mutable.Map
    val treasureMap = Map[Int,String]()
    treasureMap += (1 -> "Go to island.")
    treasureMap += (2 -> "Find big X on ground.")
    treasureMap += (3 -> "Dig.")
    println(treasureMap(2))

  }
}

