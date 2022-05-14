package com.teradata.openapi.framework.exceptions.slick

/**
  * Created by Evan on 2016/4/11.
  */
class RowNotFoundException[T](notFoundRecord: T) extends ActiveSlickException(s"Row not found: $notFoundRecord")
