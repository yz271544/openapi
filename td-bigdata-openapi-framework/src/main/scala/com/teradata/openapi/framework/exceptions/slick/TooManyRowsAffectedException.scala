package com.teradata.openapi.framework.exceptions.slick

/**
  * Created by Administrator on 2016/4/11.
  */
class TooManyRowsAffectedException(affectedRowCount: Int, expectedRowCount: Int)
  extends ActiveSlickException(s"Expected $expectedRowCount row(s) affected, got $affectedRowCount instead")
