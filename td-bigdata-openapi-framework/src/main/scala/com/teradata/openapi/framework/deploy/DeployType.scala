package com.teradata.openapi.framework.deploy

/**
  * Created by John on 2016/4/22.
  */

/**
  * 格式封装类型
  */
object FormatType extends Enumeration {
  type FormatTypeInfo = Value
  val TXT, JSON, XML, XLS = Value
}

/**
  * Format 格式类型
  * @param formType 类型String
  * @param formDetail 结合类型，将格式详细信息配置于此，目前只实现了Excel的格式定义
  */
case class Format(formType: String, formDetail: Option[FormDetail]) extends Serializable

/**
  * FormDetail 格式明细信息：rptName[报表名称]，excelTitles:List(ExcelTitles)[Excel表头容器]
  * ExcelTitles 【表头】定义：rowIndex[表头第几行]，cellList:List(CellList)[该行中单元格容器]
  * CellList 单元格定义：cellName[单元格内容],rowIndex[第几行，从0开始],colIndex[第几列，从0开始],rowSpan[纵向合并几行,最小值1代表不合并],colSpan[横向合并几列,最小值1代表不合并]
  */
case class FormDetail(rptName: String, excelTitles: List[ExcelTitles]) extends Serializable
case class ExcelTitles(rowIndex: Int, cellList: List[CellList]) extends Serializable
case class CellList(cellName: String, rowIndex: Int, colIndex: Int, rowSpan: Int, colSpan: Int) extends Serializable

/**
  * 数据编码类型
  */
object Encode extends Enumeration {
  type EncodeTypeInfo = Value
  val UTF8, GBK = Value
}

/**
  * 字段、参数必选类型
  */
private[openapi] object ArgsNecy extends Enumeration {
  type EncodeType = Value
  val NECESSARY, MUSTONE, OPTION = Value
}

/**
  * 异步请求探查返回状态
  * HIVEDATA：有数据，等的哇
  * NODATA：没数据，歇了哇
  */
private[openapi] object ReqStat extends Enumeration {
  type ReqStatType = Value
  val HIVEDATA, NODATA = Value
}

/**
  * 更新周期信息标识
  * ADD: 增加或更新周期
  * DROP: 删除周期
  */
private[openapi] object UpdCycleFlag extends Enumeration {
  type UpdCycleFlag = Value
  val ADD, DROP = Value
}

/**
  * 关联类型
  * INNER: inner join
  * LEFT: left join
  * RIGHT: right join
  * FULL: full join
  */
private[openapi] object JoinType extends Enumeration {
  type JoinType = Value
  val INNER, LEFT, RIGHT, FULL = Value
}

/**
  * 插件类型
  * CACHE: 缓存
  * FETCH: 取数
  * FORMAT: 格式化
  * PUSH: 推送
  */
private[openapi] object PluginType extends Enumeration {
  type PluginType = Value
  val CACHE, FETCH, FORMAT, PUSH, CLEAN, CLEAN_FILE = Value
}

/**
  * 字段 在数据源 中的类型
  *
  * @param sorc_id          数据源id
  * @param schemaName       字段存在表的SchemaName
  * @param tabName          字段存在表的TableName
  * @param tabAlias         字段存在表所在的API的表别名
  * @param sorc_field_type  源字段类型
  * @param sorc_format      源字段格式
  * @param sorc_max_len     源字段最大长度
  * @param sorc_total_digit 源字段数字长度
  * @param sorc_prec_digit  源字段数字精度
  */
case class SorcType(var sorc_id: Int,
                    var schemaName: String,
                    var tabName: String,
                    var tabAlias: String,
                    var sorc_field_type: String,
                    var sorc_format: String,
                    var sorc_max_len: Int,
                    var sorc_total_digit: Int,
                    var sorc_prec_digit: Int) extends Serializable

/**
  * 【业务请求参数】字段、参数属性
  *
  * @param fieldName      字段、参数名称
  * @param fieldTargtType 字段、参数类型
  * @param calcPrincId    字段、参数计算法则
  * @param fieldValue     字段、参数值
  * @param mustType       字段、参数是否必选
  */
case class ReqArg(var fieldName: String, //finger
                  var fieldTargtType: String,
                  var field_sorc_type: List[SorcType],
                  var calcPrincId: Int, //finger
                  var fieldValue: List[Any], //finger
                  var mustType: Int,
                  var expressionAtomSorcTypeMap: Option[Map[String, List[SorcType]]]
                 ) extends Serializable {
  def this() {
    this(null, null, null, 0, null, 0, null)
  }
}


/**
  * 【业务响应参数】字段、参数属性
  *
  * @param fieldAlias     字段、参数别名
  * @param fieldName      字段、参数名称
  * @param fieldTargtType 字段、参数类型
  * @param fieldTitle     字段、参数描述
  */
case class RepArg(var fieldAlias: String, //finger
                  var fieldName: String,
                  var fieldTargtType: String,
                  var fieldTitle: String
                 ) extends Serializable {
  def this() {
    this(null, null, null, null)
  }
}

/**
  * 【关联】
  *
  * @param joinType      关联类型
  * @param joinCondition 关联条件
  */
class JoinArg(joinType: JoinType.JoinType, joinCondition: String) extends Serializable

/**
  * 为GetAsynTaskData提供的下载文件参数
  *
  * @param formCode
  * @param formatFileFinger
  */
case class DownLoadAttrArgs(formCode: String, formatFileFinger: String) extends Serializable

/**
  * Push参数，目前采用ftp或者sftp协议
  *
  * @param ftpHost     "192.168.20.110"
  * @param ftpMode     "active"
  * @param ftpPath     "/data/open_api/receive/"
  * @param ftpPort     21
  * @param ftpProtocol "TLS"
  * @param ftpType     "ftp"
  * @param isConnect   true
  * @param userName    "oapi"
  * @param password    "oapi"
  */
case class PushArgs(ftpHost: String, ftpMode: String, ftpPath: String, ftpPort: Int, ftpProtocol: String, ftpType: String, isConnect: Boolean, userName: String, password: String, checkFileName: Option[String], dataFileName: Option[String]) extends Serializable



