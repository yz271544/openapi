package futureAndPromiseExample

/**
  * Created by Administrator on 2016/3/18.
  */
object ThreadsCreation {

  def main(args: Array[String]) {

    /*class MyThread extends Thread{
      override def run():Unit = {
        println("New thread running.")
        println("New thread running1.")
        println("New thread running2.")
        println("New thread running3.")
      }
    }
    val t = new MyThread
    t.start()
    t.join()
    println("New thread joined.")*/

    def thread(body: => Unit):Thread = {
      val t = new Thread{
        override def run() = body
      }
      t.start()
      t
    }

    val thread1 = thread(println("New thread running."))
    thread1.join()
    println("New thread joined.")

  }
}
