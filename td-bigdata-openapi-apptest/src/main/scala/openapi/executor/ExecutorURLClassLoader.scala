package openapi.executor

import java.net.{URLClassLoader, URL}

/**
  * Created by Administrator on 2016/3/14.
  */
private[openapi] class ExecutorURLClassLoader(urls:Array[URL],parent:ClassLoader)
  extends URLClassLoader(urls,parent){

  override def addURL(url:URL): Unit ={
    super.addURL(url)
  }

}
