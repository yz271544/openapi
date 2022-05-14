package com.teradata.openapi.rop.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class RopNamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		registerBeanDefinitionParser("annotation-driven", new AnnotationDrivenBeanDefinitionParser());
		registerBeanDefinitionParser("interceptors", new InterceptorsBeanDefinitionParser());
		registerBeanDefinitionParser("listeners", new ListenersBeanDefinitionParser());
		registerBeanDefinitionParser("sysparams", new SystemParameterNamesBeanDefinitionParser());
	}
}
