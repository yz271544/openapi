package com.teradata.openapi.sample.request;

import com.teradata.openapi.rop.AbstractRopRequest;

public class LogonRequest extends AbstractRopRequest {

	private String DEAL_DATE;

	private String fields;

	private String REGION_CODE;

	private String CITY_CODE;

	private String BARGAIN_NUM;

	private String SHOULD_PAY;

	private String SHOULD_PAY_AM;

	public String getDEAL_DATE() {
		return DEAL_DATE;
	}

	public void setDEAL_DATE(String dEAL_DATE) {
		DEAL_DATE = dEAL_DATE;
	}

	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}

	public String getREGION_CODE() {
		return REGION_CODE;
	}

	public void setREGION_CODE(String rEGION_CODE) {
		REGION_CODE = rEGION_CODE;
	}

	public String getCITY_CODE() {
		return CITY_CODE;
	}

	public void setCITY_CODE(String cITY_CODE) {
		CITY_CODE = cITY_CODE;
	}

	public String getBARGAIN_NUM() {
		return BARGAIN_NUM;
	}

	public void setBARGAIN_NUM(String bARGAIN_NUM) {
		BARGAIN_NUM = bARGAIN_NUM;
	}

	public String getSHOULD_PAY() {
		return SHOULD_PAY;
	}

	public void setSHOULD_PAY(String sHOULD_PAY) {
		SHOULD_PAY = sHOULD_PAY;
	}

	public String getSHOULD_PAY_AM() {
		return SHOULD_PAY_AM;
	}

	public void setSHOULD_PAY_AM(String sHOULD_PAY_AM) {
		SHOULD_PAY_AM = sHOULD_PAY_AM;
	}

}
