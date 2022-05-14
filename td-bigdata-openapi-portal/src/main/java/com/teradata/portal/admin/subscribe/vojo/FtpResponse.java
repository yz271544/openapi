package com.teradata.portal.admin.subscribe.vojo;

import java.io.Serializable;

/**
 * 调用ftp接口返回实体. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2018年2月7日 下午2:25:15
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
public class FtpResponse implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -2890456424467059469L;

	private String retCode;

	private String retMsg;

	private String reqID;

	private String reqStat;

	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

	public String getReqID() {
		return reqID;
	}

	public void setReqID(String reqID) {
		this.reqID = reqID;
	}

	public String getReqStat() {
		return reqStat;
	}

	public void setReqStat(String reqStat) {
		this.reqStat = reqStat;
	}

}
