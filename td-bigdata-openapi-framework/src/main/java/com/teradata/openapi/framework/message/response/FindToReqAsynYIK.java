package com.teradata.openapi.framework.message.response;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * 接收finder返回的响应数据 <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年5月17日 上午10:23:23
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "findToReqAsynYIK")
public class FindToReqAsynYIK implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -2372962995366482539L;

	// 返回值：【0 接收正常，有数据运行】 【1 接收正常，无数据等待】 【2 重复提交】 【-1 运行异常】 【-2 请求参数异常】
	@XmlElement
	private String retCode;

	// 返回描述信息:【Received,Fetching data】【Received, Waiting for data】【Request duplication】【Running exception】 【Request parameter exception】
	@XmlElement
	private String retMsg;

	// 请求ID
	@XmlElement
	private String reqID;

	// 请求状态:HIVEDATA有数据 NODATA无数据
	@XmlElement
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
