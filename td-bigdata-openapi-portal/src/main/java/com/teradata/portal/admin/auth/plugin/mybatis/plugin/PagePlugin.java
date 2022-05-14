package com.teradata.portal.admin.auth.plugin.mybatis.plugin;

import com.teradata.portal.admin.auth.plugin.jdbc.dialect.Dialect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.xml.bind.PropertyException;

import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;

/**
 * Mybatis的分页查询插件，通过拦截StatementHandler的prepare方法来实现。
 * 只有在参数列表中包括Page类型的参数时才进行分页查询。
 * 在多参数的情况下，只对第一个Page类型的参数生效。
 * 另外，在参数列表中，Page类型的参数无需用@Param来标注
 * Created by Evan on 2016/7/6.
 */

/**
 * 每一个拦截器必须实现三个方法，其中：
 * 1. Object intercept(Invocation invocation)是实现拦截逻辑的地方，内部要通过invocation.proceed()显式推进责任链前进，也就是调用下一个拦截器拦截目标方法
 * 2. Object plugin(Object target)就是用当前这个拦截器生成对目标target的代理，实际是通过Plugin.wrap(target,this)来完成的，把目标target和拦截器this传给了包装函数
 * 3. setProperties(Properties properties)用于设置额外的参数，参数配置在拦截器的Properties节点里
 */

@SuppressWarnings("unchecked")
@Intercepts( { @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class PagePlugin implements Interceptor{

    private static Dialect dialectObject = null;//数据库方言
    private static String pageSqlId = ""; //mybaits的数据库xml映射文件中需要拦截的ID(正则匹配)


    @Override
    public Object intercept(Invocation ivk) throws Throwable {

        if(ivk.getTarget() instanceof RoutingStatementHandler){
            //StatementHandler的默认实现类是RoutingStatementHandler，因此拦截的实际对象是RoutingStatementHandler
            //RoutingStatementHandler的主要功能是分发，它根据配置Statement类型创建真正执行数据库操作的StatementHandler，
            // 并将其保存到delegate属性里。由于delegate是一个私有属性并且没有提供访问它的方法，因此需要借助反射的方法来获得相应的属性信息
            RoutingStatementHandler statementHandler = (RoutingStatementHandler) ivk.getTarget();
            BaseStatementHandler delegate = (BaseStatementHandler) ReflectHelper.getValueByFieldName(statementHandler, "delegate");
            MappedStatement mappedStatement = (MappedStatement) ReflectHelper.getValueByFieldName(delegate, "mappedStatement");
            /**
             * 方法1：通过ＩＤ来区分是否需要分页．.*Page
             * 方法2：传入的参数是否有page参数，如果有，则分页，
             */
        System.out.println("------1-----");
            BoundSql boundSql = delegate.getBoundSql();
            Object parameterObject = boundSql.getParameterObject();// 分页SQL<select>中parameterType属性对应的实体参数，即Mapper接口中执行分页方法的参数,该参数不得为空
            if (parameterObject == null) {
                System.out.println("------2-----");
                //throw new NullPointerException("boundSql.getParameterObject() is null!");
                return ivk.proceed();
            } else {

                PageView pageView = null;
                if (parameterObject instanceof PageView) { // 参数就是Pages实体
                    pageView = (PageView) parameterObject;
                } else if (parameterObject instanceof Map) {
                    for (Map.Entry entry : (Set<Map.Entry>) ((Map) parameterObject).entrySet()) {
                        if (entry.getValue() instanceof PageView) {
                            pageView = (PageView) entry.getValue();
                            System.out.println("---------intercept inside pageView -------"+pageView.getPageSize());
                            break;
                        }
                    }
                } else { // 参数为某个实体，该实体拥有Pages属性
                    pageView = ReflectHelper.getValueByFieldType(parameterObject, PageView.class);
                    if (pageView == null) {
                        return ivk.proceed();
                    }
                }
                if (pageView == null) {
                    return ivk.proceed();
                }
                String sql = boundSql.getSql();
                PreparedStatement countStmt = null;
                ResultSet rs = null;
                try {
                    Connection connection = (Connection) ivk.getArgs()[0];
                    String countSql = "select count(1) from (" + sql + ") tmp_count"; // 记录统计
                    countStmt = connection.prepareStatement(countSql);
                    ReflectHelper.setValueByFieldName(boundSql, "sql", countSql);
                    DefaultParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
                    parameterHandler.setParameters(countStmt);
                    rs = countStmt.executeQuery();
                    int count = 0;
                    if (rs.next()) {
                        count = ((Number) rs.getObject(1)).intValue();
                    }
                    pageView.setRowCount(count);
                } finally {
                    try {
                        rs.close();
                    } catch (Exception e) {
                    }
                    try {
                        countStmt.close();
                    } catch (Exception e) {
                    }
                }
                String pageSql = generatePagesSql(sql, pageView);
                ReflectHelper.setValueByFieldName(boundSql, "sql", pageSql); // 将分页sql语句反射回BoundSql.
            }

        }
        return ivk.proceed();
    }

    /**
     * 根据数据库方言，生成特定的分页sql
     *
     * @param sql
     * @param page
     * @return
     */
    private String generatePagesSql(String sql, PageView page) {
        if (page != null && dialectObject != null) {
            //pageNow默认是从1，而已数据库是从0开始计算的．所以(page.getPageNow()-1)
            int pageNow = page.getPageNow();
            return dialectObject.getLimitString(sql, (pageNow<=0?0:pageNow-1)
                    * page.getPageSize(), page.getPageSize());
        }
        return sql;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    /**
     * 判断变量是否为空
     *
     * @param s
     * @return
     */
    public boolean isEmpty(String s) {
        if (null == s || "".equals(s) || "".equals(s.trim()) || "null".equalsIgnoreCase(s)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setProperties(Properties p) {
        String dialect = ""; // 数据库方言
        dialect = p.getProperty("dialect");
        if (isEmpty(dialect)) {
            try {
                throw new PropertyException("dialect property is not found!");
            } catch (PropertyException e) {
                e.printStackTrace();
            }
        } else {
            try {
                dialectObject = (Dialect) Class.forName(dialect)
                        .getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(dialect + ", init fail!\n" + e);
            }
        }
        pageSqlId = p.getProperty("pageSqlId");//根据id来区分是否需要分页
        if (isEmpty(pageSqlId)) {
            try {
                throw new PropertyException("pageSqlId property is not found!");
            } catch (PropertyException e) {
                e.printStackTrace();
            }
        }
    }
}
