package openapi.deploy
import scala.collection.Map
/**
  * Created by Administrator on 2016/3/15.
  */
private[openapi] case class Command(
       mainClass:String,
       arguments:Seq[String],
       environment: Map[String,String]) {

}
