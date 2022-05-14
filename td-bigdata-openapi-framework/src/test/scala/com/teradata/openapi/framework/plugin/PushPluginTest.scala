package com.teradata.openapi.framework.plugin;

import org.junit.Test

import scala.util.matching.Regex

class PushPluginTest {
    @Test
    def testRegexDataFileName(): Unit = {
        val filemod = "dfasf$true$true.dfasfa$false$false"
        val dd = "20180418"
        val dn = 1

        val datafileName = regexDataFileName(filemod, dd, dn)

        println("dataFileName:" + datafileName)
    }

    @Test
    def testRegexChkFileName(): Unit = {

        val filemod = "adfadsf$true.fadsfas$false"
        val dd = "20180418"

        val chkFIleName = regexChkFileName(filemod, dd)

        println("CheckFileName:" + chkFIleName)

    }


    def regexDataFileName(fileNameModel: String, dataDate: String, dataNum: Int): String = {

        val sDataNum: String = "_" + "%03d".format(dataNum)

        val varPattern = new Regex("""(\w+)(\$true|\$false)(\$true|\$false)(.\w+)(\$true|\$false)(\$true|\$false)""", "pword", "perfixDate", "perfixNum", "sword", "suffixDate", "suffixNum")

        val targetFileName = varPattern replaceAllIn(fileNameModel, m => s"${m group "pword"}${if ((m group "perfixDate") equals """$true""") dataDate else ""}${if ((m group "perfixNum") equals """$true""") sDataNum else ""}${m group "sword"}${if ((m group "suffixDate") equals """$true""") dataDate else ""}${if ((m group "suffixNum") equals """$true""") sDataNum else ""}")

        targetFileName
    }

    def regexChkFileName(fileNameModel: String, dataDate: String): String = {
        val varPattern = new Regex("""(\w+)(\$true|\$false)(.\w+)(\$true|\$false)""", "pword", "perfixDate", "sword", "suffixDate")

        val targetFileName = varPattern replaceAllIn(fileNameModel, m => s"${m group "pword"}${if ((m group "perfixDate") equals """$true""") dataDate else ""}${m group "sword"}${if ((m group "suffixDate") equals """$true""") dataDate else ""}")

        targetFileName
    }
}