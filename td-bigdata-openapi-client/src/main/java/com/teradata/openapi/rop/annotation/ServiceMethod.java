package com.teradata.openapi.rop.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.teradata.openapi.rop.ServiceMethodDefinition;

/**
 * 
 * 使用该注解对服务方法进行标注，这些方法必须是Spring的Service:既打了@Service的注解。 <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年3月29日 下午6:02:57
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServiceMethod {

	/**
	 * 所属的服务分组
	 * 
	 * @return
	 */
	String group() default ServiceMethodDefinition.DEFAULT_GROUP;

	/**
	 * 所属的服务分组的标识
	 * 
	 * @return
	 */
	String groupTitle() default ServiceMethodDefinition.DEFAULT_GROUP_TITLE;

	/**
	 * 请求方法，默认不限制
	 * 
	 * @return
	 */
	HttpAction[] httpAction() default {};

	/**
	 * 是否忽略签名检查，默认不忽略
	 * 
	 * @return
	 */
	IgnoreSignType ignoreSign() default IgnoreSignType.DEFAULT;

	/**
	 * 服务的方法名，即由method参数指定的服务方法名
	 * 
	 * @return
	 */
	String method() default "";

	/**
	 * 服务方法需要需求会话检查，默认要检查
	 * 
	 * @return
	 */
	NeedInSessionType needInSession() default NeedInSessionType.DEFAULT;

	/**
	 * 服务方法是否已经过期，默认不过期
	 * 
	 * @return
	 */
	ObsoletedType obsoleted() default ObsoletedType.DEFAULT;

	/**
	 * 标签，可以打上多个标签
	 * 
	 * @return
	 */
	String[] tags() default {};

	/**
	 * 访问过期时间，单位为毫秒，即大于这个过期时间的链接会结束并返回错误报文，如果 为0或负数则表示不进行过期限制
	 * 
	 * @return
	 */
	int timeout() default -1;

	/**
	 * 服务的中文名称
	 * 
	 * @return
	 */
	String title() default "";

	/**
	 * 该方法所对应的版本号，对应version请求参数的值，版本为空，表示不进行版本限定
	 * 
	 * @return
	 */
	String version() default "";
}
