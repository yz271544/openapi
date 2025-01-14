package com.teradata.openapi.rop.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.teradata.openapi.rop.ServiceMethodDefinition;

/**
 * <pre>
 *    在服务类中标该类，以便确定服务方法所属的组及相关信息。由于ApiMethodGroup已经标注了
 * Spring的{@link Component}注解，因此标注了{@link ServiceMethodBean}的类自动成为Spring的Bean.
 * </pre>
 * 
 * @version 1.0
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface ServiceMethodBean {

	/**
	 * 所属的服务分组，默认为"DEFAULT"
	 * 
	 * @return
	 */
	String group() default ServiceMethodDefinition.DEFAULT_GROUP;

	/**
	 * 组中文名
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

	String value() default "";

	/**
	 * 该方法所对应的版本号，对应version请求参数的值，版本为空，表示不进行版本限定
	 * 
	 * @return
	 */
	String version() default "";
}
