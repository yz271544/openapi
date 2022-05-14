package openapi.deploy.master

/**
  * Created by Administrator on 2016/3/15.
  */
private[openapi] object ApplicationState extends Enumeration{

    type ApplicationState = Value

    val WITING,RUNNING,FINISHED,FAILED,UNKNOWN = Value

    val MAX_NUM_RETRY = 10

}
