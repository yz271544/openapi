package openapi.deploy.master

import openapi.Utils

/**
  * Created by Administrator on 2016/3/15.
  */
private[openapi] class MasterArguments(args:Array[String]) {

  var ip = Utils.localHostName()
  var port = 7077

}
