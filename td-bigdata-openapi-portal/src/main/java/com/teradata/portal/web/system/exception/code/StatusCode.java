package com.teradata.portal.web.system.exception.code;

public interface StatusCode {

	/**
	 * 返回4位(0000~9999)错误状态编码
	 * 
	 * @return
	 */
	String getCode();

	/**
	 * 返回错误状态的描述信息，可包含占位符，该描述最终可被输出至前端页面展示 <br/>
	 * 占位符类似于 {0}, {1}，具体抛出异常时可以指定替换占位符的参数，但占位符中体现的信息不会进行语义的国际化处理，建议一般为数字，日期等
	 * 
	 * @return
	 */
	String getDescription();
}
