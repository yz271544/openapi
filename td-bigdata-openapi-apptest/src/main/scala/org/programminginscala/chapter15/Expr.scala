package org.programminginscala.chapter15

/**
  * Created by Administrator on 2016/3/25.
  */
abstract class Expr

case class Var(name:String) extends Expr
case class Number(num:Double) extends Expr
case class UnOp(operator:String,arg:Expr) extends Expr
case class BinOp(operator:String,left:Expr,right:Expr) extends Expr
