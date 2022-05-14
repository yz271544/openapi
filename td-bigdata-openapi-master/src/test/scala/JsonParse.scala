import com.codahale.jerkson.Json
import com.teradata.openapi.framework.deploy.ReqToFind

/**
  * Created by John on 2016/9/28.
  */
object JsonParse {
  val testJson = """{"apiClsCode":1,"apiId":1001,"apiName":"tb_rpt_bo_mon","apiSort":1,"api_version":1,"appKey":"qichao1","encode":"UTF-8","format":{"formDetail":{},"formType":"json"},"isSyn":0,"priority":0,"repArgs":[{"fieldAlias":"SHOULD_PAY_AMT","fieldName":"SHOULD_PAY_AMT","fieldTargtType":"Double","fieldTitle":"实收金额"},{"fieldAlias":"BARGAIN_NUM","fieldName":"BARGAIN_NUM","fieldTargtType":"Integer","fieldTitle":"交易数量"},{"fieldAlias":"SHOULD_PAY","fieldName":"SHOULD_PAY","fieldTargtType":"Double","fieldTitle":"应收账款"},{"fieldAlias":"CITY_CODE","fieldName":"CITY_CODE","fieldTargtType":"String","fieldTitle":"区县代码"},{"fieldAlias":"REGION_CODE","fieldName":"REGION_CODE","fieldTargtType":"String","fieldTitle":"地市代码"},{"fieldAlias":"DEAL_DATE","fieldName":"DEAL_DATE","fieldTargtType":"Integer","fieldTitle":"处理日期"}],"reqArgs":[{"calcPrincId":3002,"fieldName":"DEAL_DATE","fieldTargtType":"Integer","fieldValue":["201604"],"field_sorc_type":[{"schemaName":"RPTMART3","sorc_field_type":"I","sorc_format":"yyyyMM","sorc_id":1,"sorc_max_len":4,"sorc_prec_digit":0,"sorc_total_digit":0,"tabAlias":"T1","tabName":"TB_RPT_BO_MON"},{"schemaName":"RPTMART3","sorc_field_type":"integer","sorc_format":"no format","sorc_id":2,"sorc_max_len":4,"sorc_prec_digit":0,"sorc_total_digit":0,"tabAlias":"T1","tabName":"TB_RPT_BO_MON"}],"mustType":3}],"reqID":"64A9EED6-4157-4579-BE73-1818309E33DE","retDataFinger":"d891cfb2394b0a7000238274e8cf00d9","retFormatFinger":"930545dd11ccdd0d8ad931888c16bc82","timeStamp":1475032630213}"""

  def main(args: Array[String]) {
    val aa = Json.parse[ReqToFind](testJson)

    println(aa)
  }

}
