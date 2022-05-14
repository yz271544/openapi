package openapi

/**
  * Created by Administrator on 2016/3/15.
  */
class OpenApiException(message:String,cause:Throwable)
      extends Exception(message,cause){
  def this(message:String) = this(message,null)

}
