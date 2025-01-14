package com.teradata.openapi.framework.util

/**
  * Created by Administrator on 2016/4/1.
  */
private[openapi] object OpenApiExitCode {

  /** The default uncaught exception handler was reached. */
  val UNCAUGHT_EXCEPTION = 50

  /** The default uncaught exception handler was called and an exception was encountered while
      logging the exception. */
  val UNCAUGHT_EXCEPTION_TWICE = 51

  /** The default uncaught exception handler was reached, and the uncaught exception was an
      OutOfMemoryError. */
  val OOM = 52

}
