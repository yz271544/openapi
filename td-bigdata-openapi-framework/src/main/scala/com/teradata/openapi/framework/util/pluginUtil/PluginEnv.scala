package com.teradata.openapi.framework.util.pluginUtil

import java.io.{File, FileInputStream}
import java.net.URI
import java.util.Properties

import com.teradata.openapi.framework.OpenApiLogging
import com.teradata.openapi.framework.deploy.Encode
import com.teradata.openapi.framework.model.SourceInfoRow
import com.teradata.openapi.framework.util.Utils
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.security.UserGroupInformation

/**
  * Created by hdfs on 2016/7/5.
  */
//object PluginEnv extends App with OpenApiLogging{
object PluginEnv extends OpenApiLogging {
  def ini = {
    log.debug("PluginEnv is initialization is starting.....")
    val defaultPlugProps = "plugin.properties"
    try {
      //文件要放到resource文件夹下
      val path: String = Utils.getOpenApiClassLoader.getResource(defaultPlugProps).getPath
      properties.load(new FileInputStream(path))
    } catch {
      case e: Exception => log.debug(e.getMessage)
    }
  }

  val properties = new Properties()
  this.ini

  private val url = this.getClass.getProtectionDomain().getCodeSource().getLocation().getPath()

  private val path =
    try {
      new File(java.net.URLDecoder.decode(url, "utf8"))
    } catch {
      case e: Exception => new File(url)
    }

  val (libPath, mainPath) =
    if (path.isFile()) {
      (path.getParent() + File.separator, path.getParentFile().getParent() + File.separator)
    }
    else {
      (path.getAbsolutePath() + File.separator, path.getParent() + File.separator)
    }

	val plugBlank: String = properties.getProperty("plug.blank", "\\u0020")
	val plugFieldDelimiter: String = properties.getProperty("plug.fieldDelimiter", "\\u0002")
	val plugFtchFieldDelimiter: String = properties.getProperty("plug.fetchPlug.fieldDelimiter", "\\u0002")
	val plugFrmtFieldDelimiter: String = properties.getProperty("plug.formatPlug.fieldDelimiter", "\\u0009")

	/**
		* 新增文件分隔符，导出文件分割    modify by qiwei 2018-01-02 分隔符为十六进制的2即\x02
		* 同时修改plugFieldDelimiter和plugFtchFieldDelimiter 两个变量的默认值为\\u0002
 		*/
	val plugFieldRawDelimiter: String = properties.getProperty("plug.fieldRawDelimiter", "")
	val plugFileDelimiter: String = properties.getProperty("plug.fileDelimiter", "/")
	val plugDBConnSign: String = properties.getProperty("plug.dbConnSign", ".")

  val plugFileCode: String = properties.getProperty("plug.fileCode", Encode.UTF8.toString)
  val plugFtchFileCode: String = properties.getProperty("plug.fetchPlug.fileCode", Encode.UTF8.toString)
  val plugFtchTdFileCode: String = properties.getProperty("plug.fetchPlug.tdFileCode", Encode.GBK.toString)
  /*
    val plugCmdFileAppend: String = properties.getProperty("plug.cmdFileAppend", SuffixType.CMD.toString)
    val plugFrmtCheckFileAppend: String = properties.getProperty("plug.formatPlug.checkFileAppend", SuffixType.CHK.toString)
    val plugFrmtTitleFileAppend: String = properties.getProperty("plug.formatPlug.titleFileAppend", SuffixType.TIL.toString)
  */

  val plugHDFSUri: String = properties.getProperty("plug.HDFSInfo.uri","hdfs://master01:8020/")
  val plugHDFSUser: String = properties.getProperty("plug.HDFSInfo.user")

  val plugUseFuseFlag: Boolean = properties.getProperty("plug.useFuseFlag", "false").toBoolean

  /**
    * Local Path ( absolute paths )
    */
  val plugHdfsFullPath: String = properties.getProperty("plugHDFSFullPath", this.plugHDFSUri)
  val hdfsWorkDir: String = properties.getProperty("plug.hdfsWorkDir") // /user/oapi/data

  /**
    * Relative paths
    */
  val plugTargetDir: String = properties.getProperty("plug.targetDir")

  val plugFtchTargetDir: String = properties.getProperty("plug.ftchPlug.targetDir")
  val plugFrmtSourceDir: String = properties.getProperty("plug.frmtPlug.sourceDir")
  val plugFrmtTargetDir: String = properties.getProperty("plug.frmtPlug.targetDir")
  val plugFtchTmpDir: String = properties.getProperty("plug.FtchPlug.tmpFileDir")
  val plugTmpCmdDir: String = properties.getProperty("plug.tmpCmdDir")
  val plugSysFuseDir: String = properties.getProperty("plug.sysFuseDir")
  val plugTmpCachDir: String = properties.getProperty("plug.tmpCachDir")

  /**
    * Database relations
    */
  val plugFtchAstSchema: String = properties.getProperty("plug.ftchPlug.astDatabaseSchema")
  val plugCachSchema: String = properties.getProperty("plug.CachPlug.hiveDefaultSchema")
  val plugCachTmpSchema: String = properties.getProperty("plug.CachPlug.hiveDefaultTmpSchema")

  val plugTmpTableNamePrefix: String = properties.getProperty("plug.tmpTableNamePrefix")

  log.debug("plug.tmpTableNamePrefix is " + properties.getProperty("plug.tmpTableNamePrefix"))
  log.debug(properties.getProperty("plug.AstTableNameMax"))
//  log.debug("Int is " + properties.getProperty("plug.AstTableNameMax").toInt)

  val plugAstTableNameMax: Int = properties.getProperty("plug.AstTableNameMax", "45").toInt

  // Import/Export tools relations
  /**
    * TDCH
    * split.by.hash|split.by.value|split.by.partition|split.by.amp
    */
  val plugCacheTDCHSplitMethod: String = properties.getProperty("plug.CachePlug.TDCHDefaultSplitMethod")
  /**
    * textfile | sequencefile | rcfile | orcfile
    */
  val plugCacheHiveDefaultFileType: String = properties.getProperty("plug.CachePlug.hiveDefaultFileType")

  /**
    * Format Jar Path
    */
  val plugFrmtJarPath: String = properties.getProperty("plug.formatPlug.jarPath")
  val plugFrmtLibJars: String = properties.getProperty("plug.formatPlug.libJars")

  /**
    * SQL statements Template
    */
  val plugCachPlugColumnInfoQuery: String = properties.getProperty("plug.CachPlug.columnInfoQuery")
  val plugCachCreatePartTable: String = properties.getProperty("plug.CachPlug.hiveCreatePartTable")
  val plugCachCreateTableTemplate: String = properties.getProperty("plug.CachPlug.hiveCreateNoPartTable")
  val plugCachCreateExternalTableTemplate: String = properties.getProperty("plug.CachPlug.hiveCreateExternalTable")
  val plugCachCreateParTableTemplate: String = properties.getProperty("plug.CachPlug.hiveCreatePartition")
  val plugCachDropPartitionTemplate: String = properties.getProperty("plug.CachPlug.hiveDropPartition")
  val plugDropTableTemplate: String = properties.getProperty("plug.dropTableTemplate")


  val plugAstCreateTableAsTemplate: String = properties.getProperty("plug.astCreateTableAsTemplate")

  /**
    * Default Database Source
    */
  val plugCachPostgresSourceInfo: SourceInfoRow = BaseUtil.getSourceInfo(properties.getProperty("plug.CachPlug.postgresSourceInfo","4,04,4,OpenAPI元数据库,192.168.20.1,5432,open_api,open_api,123456,0,0,0,org.postgresql.Driver,true,true,true"))
  val plugCachHiveSourceInfo: SourceInfoRow = BaseUtil.getSourceInfo(properties.getProperty("plug.CachPlug.hiveSourceInfo","3,03,3,山西移动Hive数据交换中心,192.168.20.119,10000,default,oapi,oapi,1,5, 6,org.apache.hive.jdbc.HiveDriver,false,true,true"))

  /**
    * Kerberos 参数配置 modify Add  2017-10-24 14:59 	by qiwei
    * 用户
    * 路径
    */
  val krbOffOn: Boolean = properties.getProperty("hadoop.isKrb", "false").toBoolean
  val krbconfigpath: String = properties.getProperty("linux.krb5.conf.path", "/etc/krb5.conf")
  val securiAuthen: String = properties.getProperty("hadoop.security.authentication", "kerberos")
  val principal: String = properties.getProperty("dfs.namenode.kerberos.principal", "hdfs/_HOST@SXMCC.COM")
  val keyatabuser: String = properties.getProperty("hdfs.kerberos.user", "hdptd@SXMCC.COM")
  val keytabPath: String = properties.getProperty("hdfs.kerberos.path", "/home/hdptd/open_api/util/krb5/hdptd.keytab")

  /*
  modify delete 2017-10-24 14:59 by qiwei
  val cacheFileSystem: FileSystem = {
    org.apache.hadoop.fs.FileSystem.get(new URI(this.plugHDFSUri), new Configuration())
  }
  */
  /**
    * HDFS FileSystem object
    */
  val cacheFileSystem: FileSystem = {
    val conf: Configuration = new Configuration()

    if (krbOffOn){
      System.setProperty("java.security.krb5.conf", this.krbconfigpath)
      conf.set("fs.hdfs.impl", classOf[org.apache.hadoop.hdfs.DistributedFileSystem].getName)
      conf.set("fs.webhdfs.impl", classOf[org.apache.hadoop.hdfs.web.WebHdfsFileSystem].getName)
      conf.setBoolean("hadoop.security.authentication", true)
      conf.set("hadoop.security.authentication", this.securiAuthen)
      conf.set("dfs.namenode.kerberos.principal", this.principal)


      UserGroupInformation.setConfiguration(conf)
      try {
        UserGroupInformation.loginUserFromKeytab(this.keyatabuser, this.keytabPath)
      } catch {
        case e: Exception => {
          log.error("身份认证异常： " + e.getMessage)
          e.printStackTrace()
        }

      }
    }
    org.apache.hadoop.fs.FileSystem.get(new URI(this.plugHDFSUri), conf)
  }

  /**
    * ftp test push dir and filename
    */
  val ftpTestPushDir: String = properties.getProperty("ftp.test.push.dir", "D:\\iProject\\openAPI\\td-bigdata-openapi-framework\\src\\main\\resources")
  val ftpTestPushFileName: String = properties.getProperty("ftpTest.txt","ftpTest.txt")
}
