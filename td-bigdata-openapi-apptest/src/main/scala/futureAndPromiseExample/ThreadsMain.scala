package futureAndPromiseExample

/**
  * Created by Administrator on 2016/3/18.
  */
object ThreadsMain {

  def main(args: Array[String]) {
    val t: Thread = Thread.currentThread()
    val name = t.getName
    //插值字符串以字符s开头，可以在其封装范围中包含符号$和任意标识符
    println(s"I am the thread $name")
  }

}
