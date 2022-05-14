package com.teradata.openapi.rop.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * 如果标注在请求类的属性上，则表示该属性无需进行签名，如下所示： 请求对象（{@link com.rop.RopRequest}
 * ）中不需要签名校验的属性（默认都要签名）。
 * <p>
 * Copyright: Copyright (c) 2016年3月29日 下午5:36:45
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
@Target({ ElementType.FIELD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreSign {
}
