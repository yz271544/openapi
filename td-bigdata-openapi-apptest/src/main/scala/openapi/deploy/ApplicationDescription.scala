package openapi.deploy

/**
  * Created by Administrator on 2016/3/15.
  */
class ApplicationDescription(val name:String,val command:Command)
     extends Serializable{

  val user = System.getProperty("user.name", "<unknown>")
  override def toString:String = "ApplicationDescription(" + name + ")"

}
