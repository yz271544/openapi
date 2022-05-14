package com.teradata.openapi.formatPage

import scala.util.matching.Regex

/**
  * Created by hdfs on 2016/6/16.
  */
object FormatFunctionUtil {
	val EscapeCharReg = new Regex("""(\\[a-zA-Z])""")
	val CharReg = new Regex("""([\u0020-\u007E])""")
	val EscapeUnicodeReg = new Regex("""(\\[uU][0-9a-fA-F]{4})""")
	val DefaultDelimiter = '\u0001'

	def checkDelimiter(delimiter:String): String = {

		delimiter match {
			case EscapeUnicodeReg(m) => {
				println("escapeUnicodeReg");
				m.toString
			}
			case EscapeCharReg(m) => {
				println("escape char")
				m.toString
			}
			case CharReg(m) => {
				println("charReg")
				m.toString
			}
			case _ => {
				println("Use default delimiter \\u0001!")
				DefaultDelimiter.toString
			}
		}
	}

	def convertDelimiter(regex:String):Char = {
		regex match {
			case EscapeUnicodeReg(m) => {
				println("escapeUnicodeReg")
				(0xffff & Integer.parseInt(m.takeRight(4).toString,16)).toByte.toChar
			}
			case EscapeCharReg(m) => {
				println("escape char")
				m match {
					case "\\b"  => '\b'
					case "\\t"  => '\t'
					case "\\n"  => '\n'
					case "\\f"  => '\f'
					case "\\r"  => '\r'
					case _ 		 =>	m.charAt(1)
				}
			}
			case CharReg(m) => {
				println("charReg")
				m.toCharArray.head
			}
			case _ => {
				println("Use default delimiter \\u0001!")
				DefaultDelimiter
			}
		}
	}

}
