package com.teradata.openapi.rop.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 默认情况下，请求对象的所有field都会作为请求参数提交，如果希望某个field不作为参数提交，可以打上{@Temporary}
 * 注解，如下所示：
 * 
 * <pre class="code">
 * <DIV>&nbsp; public class MyRopRequest implements RopRequest{
 * <DIV>&nbsp;</DIV>
 * <DIV>&nbsp; private String field1;</DIV>
 * <DIV>&nbsp;</DIV>
 * <DIV>&nbsp; @Temporary</DIV>
 * <DIV>&nbsp; private String field2;</DIV>&nbsp;}
 * </pre>
 * 
 * @version 1.0
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Temporary {

}
