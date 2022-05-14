package regular;

import java.util.List;

public class TestExpression {
    public static void main(String[] args) {
        String orgi = "(deal_date[eq]201708&&prodt_code[in]abc,123)||total_flow[ge]5000";
        /*System.out.println(ExpressionParser.getColumns(orgi));
        System.out.println(ExpressionParser.expressionParser(orgi));*/
        List<String> columns = ExpressionParser.getColumnsInJava(orgi);
        for (String column : columns) {
            System.out.println(column);
        }
    }
}
