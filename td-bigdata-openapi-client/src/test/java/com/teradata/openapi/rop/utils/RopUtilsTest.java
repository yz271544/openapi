package com.teradata.openapi.rop.utils;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * 这个是签名的算法，指纹的算法在 AnnotationServletServiceRouter finger函数
 */
public class RopUtilsTest {
    @Test
    public void testSign() throws Exception {
        Map<String, String> paramValues = new HashMap<String, String>();
        paramValues.put("appKey","qichao1");
        paramValues.put("method","tb_rpt_bo_mon");
        paramValues.put("version","1");
        paramValues.put("format","json");
        paramValues.put("locale","zh_CN");
        paramValues.put("codeType","UTF-8");
        paramValues.put("reqType","syn");
        paramValues.put("fields","DEAL_DATE,CITY_CODE,SHOULD_PAY,BARGAIN_NUM,SHOULD_PAY_AMT,REGION_CODE");
        /*paramValues.put("CITY_CODE","01");
        paramValues.put("REGION_CODE","00");
        paramValues.put("_expression_","(deal_date[eq]201604@@region_code[in]01,00)@@BARGAIN_NUM[ge]300");
        */
        paramValues.put("DEAL_DATE","201304");

        String sign = RopUtils.sign(paramValues, "123");

        System.out.println(sign);

    }

    @Test
    public void testsign1() throws Exception {
        Map<String, String> paramValues = new HashMap<String, String>();
        paramValues.put("appKey","qichao1");
        paramValues.put("method","tb_rpt_bo_mon");
        paramValues.put("version","1");
        paramValues.put("format","json");
        paramValues.put("locale","zh_CN");
        paramValues.put("codeType","UTF-8");
        paramValues.put("reqType","syn");
        paramValues.put("fields","DEAL_DATE,REGION_CODE,CITY_CODE,BARGAIN_NUM,SHOULD_PAY,SHOULD_PAY_AMT");
        /*paramValues.put("CITY_CODE","01");
        paramValues.put("REGION_CODE","00");
        */
        paramValues.put("_expression_","(deal_date[eq]201601@@region_code[in]01,00)@@BARGAIN_NUM[ge]300");

        paramValues.put("DEAL_DATE","201601");

        String sign = RopUtils.sign(paramValues, "123");

        System.out.println(sign);
    }

}