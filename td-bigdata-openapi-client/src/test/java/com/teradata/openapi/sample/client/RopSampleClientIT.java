package com.teradata.openapi.sample.client;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.teradata.openapi.sample.RopSampleClient;

// @ContextConfiguration("classpath:openapi-rop.xml")
@ContextConfiguration(locations = { "classpath*:/openapi-mybatis.xml", "classpath*:/openapi-rop.xml" })
public class RopSampleClientIT extends AbstractJUnit4SpringContextTests {

	public static final String APP_KEY = "houbl";

	public static final String APP_SECRET = "123456";

	RopSampleClient ropSampleClient = new RopSampleClient(APP_KEY, APP_SECRET);

	/*
	 * @Test public void testLogon() throws Exception { String sessionId = ropSampleClient.logon("tomson", "123456");
	 * assertNotNull(sessionId); }
	 * @Test public void testLogout() throws Exception { ropSampleClient.logout(); }
	 */

	@Test
	public void addUser() {

		ropSampleClient.logon();

	}
}
