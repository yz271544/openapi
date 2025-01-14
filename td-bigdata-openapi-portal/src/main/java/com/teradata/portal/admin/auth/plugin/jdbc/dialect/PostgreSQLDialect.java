package com.teradata.portal.admin.auth.plugin.jdbc.dialect;

/**
 * Created by Evan on 2016/7/4.
 */
public class PostgreSQLDialect extends Dialect {

    public boolean supportsLimit(){
        return true;
    }

    public boolean supportsLimitOffset(){
        return true;
    }

    public String getLimitString(String sql,int offset,String offsetPlaceholder,int limit,String limitPlaceholder){
        return new StringBuffer( sql.length()+20 )
                .append(sql)
                .append(offset > 0 ? " limit "+limitPlaceholder+" offset "+offsetPlaceholder : " limit "+limitPlaceholder)
                .toString();
    }
}
