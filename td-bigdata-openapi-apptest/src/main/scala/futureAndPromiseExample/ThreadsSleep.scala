package futureAndPromiseExample

/**
  * Created by Administrator on 2016/3/18.
  */
object ThreadsSleep{

  def main(args: Array[String]) {

    def thread(body: => Unit):Thread = {
      val t = new Thread{
        override def run() = body
      }
      t.start()
      t
    }

    val t = thread{
      Thread.sleep(1000)
      println("New thread running.")
      Thread.sleep(1000)
      println("Still running.")
      Thread.sleep(1000)
      println("Completed.")
    }
    t.join()
    println("New thread joined.")

  }

}
