package model

case class ReqArg1(var fieldName: String, //finger
                   var fieldTargtType: String,
                   var field_sorc_type: List[SorcType1],
                   var calcPrincId: Int, //finger
                   var fieldValue: List[Any], //finger
                   var mustType: Int,
                   var expressionAtomSorcTypeMap: Option[Map[String, List[SorcType1]]]
                  ) extends Serializable {
  def this() {
    this(null, null, null, 0, null, 0, null)
  }
}

case class SorcType1(var sorc_id: Int,
                     var schemaName: String,
                     var tabName: String,
                     var tabAlias: String,
                     var sorc_field_type: String,
                     var sorc_format: String,
                     var sorc_max_len: Int,
                     var sorc_total_digit: Int,
                     var sorc_prec_digit: Int) extends Serializable


case class TestOption(var sorc_id: Int,
                      var prop: Option[String]) extends Serializable