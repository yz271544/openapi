package com.teradata.openapi.framework.exceptions.slick

/**
  * Created by Evan on 2016/4/11.
  */
case class StaleObjectStateException[T](staleObject: T)
  extends ActiveSlickException(s"Optimistic locking error - object in stale state: $staleObject")
