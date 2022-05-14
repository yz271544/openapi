import com.teradata.openapi.framework.message.request.ReqArg;
import com.teradata.openapi.framework.message.request.SorcType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReqArgTest {
    public static void main(String[] args) {
        List<String> fieldValue = new ArrayList<String>();
        fieldValue.add("(deal_date[eq]201604@@region_code[in]01,00)@@BARGAIN_NUM[ge]300");
        ReqArg reqArg = new ReqArg();
        reqArg.setFieldName("_expression_");
        reqArg.setFieldTargtType("");
        reqArg.setCalcPrincId(0);
        reqArg.setMustType(2);
        reqArg.setFieldValue(fieldValue);
        reqArg.setField_sorc_type(new ArrayList<SorcType>());

        SorcType sorcType = new SorcType();
        sorcType.setSchemaName("schName");
        sorcType.setSorc_field_type("fieldType");

        List<SorcType> sorcTypes = new ArrayList<SorcType>();
        sorcTypes.add(sorcType);

        Map<String,List<SorcType>> eastm = new HashMap<String,List<SorcType>>();
        eastm.put("column1",sorcTypes);

        reqArg.setExpressionAtomSorcTypeMap(eastm);

        System.out.println(reqArg);

    }
}
