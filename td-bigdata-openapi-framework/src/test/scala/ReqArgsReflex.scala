import com.codahale.jerkson.Json
import model.{ReqArg1, TestOption}

object ReqArgsReflex {
  def main(args: Array[String]): Unit = {
    val ra = """[{"fieldName":"CITY_CODE","fieldTargtType":"String","field_sorc_type":[{"sorc_id":1,"schemaName":"RPTMART3","tabName":"TB_RPT_BO_MON","tabAlias":"T1","sorc_field_type":"CF","sorc_format":"X(2)","sorc_max_len":2,"sorc_total_digit":0,"sorc_prec_digit":0}],"calcPrincId":3002,"fieldValue":["00"],"mustType":0},{"fieldName":"DEAL_DATE","fieldTargtType":"Integer","field_sorc_type":[{"sorc_id":1,"schemaName":"RPTMART3","tabName":"TB_RPT_BO_MON","tabAlias":"T1","sorc_field_type":"I","sorc_format":"yyyyMM","sorc_max_len":4,"sorc_total_digit":0,"sorc_prec_digit":0}],"calcPrincId":3002,"fieldValue":["201605"],"mustType":3}]"""
    val raObject = Json.parse[List[ReqArg1]](ra)
    println(raObject)

    val ton = TestOption(1,None)
    val tonJs = Json.generate(ton)
    println(tonJs)

    val jsTon = """{"sorc_id":1}"""
    val tons = Json.parse[TestOption](jsTon)
    println(tons)

  }
}

class ReqArgsReflex {

}

