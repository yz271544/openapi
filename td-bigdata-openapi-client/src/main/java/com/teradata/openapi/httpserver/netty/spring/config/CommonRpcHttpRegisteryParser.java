package com.teradata.openapi.httpserver.netty.spring.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.teradata.openapi.httpserver.netty.spring.config.support.CommonRpcHttpRegistery;

public class CommonRpcHttpRegisteryParser implements BeanDefinitionParser {

	/*
	 * (non-Javadoc)
	 * @see
	 * org.springframework.beans.factory.xml.BeanDefinitionParser#parse(org.
	 * w3c.dom.Element, org.springframework.beans.factory.xml.ParserContext)
	 */
	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		String id = element.getAttribute("id");
		int port = Integer.parseInt(element.getAttribute("port"));
		int timeout = Integer.parseInt(element.getAttribute("timeout"));

		RootBeanDefinition beanDefinition = new RootBeanDefinition();
		beanDefinition.setBeanClass(CommonRpcHttpRegistery.class);
		beanDefinition.getPropertyValues().addPropertyValue("port", port);
		beanDefinition.getPropertyValues().addPropertyValue("timeout", timeout);
		parserContext.getRegistry().registerBeanDefinition(id, beanDefinition);

		return beanDefinition;
	}

}
