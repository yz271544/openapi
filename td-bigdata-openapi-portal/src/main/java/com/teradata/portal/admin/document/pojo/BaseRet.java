package com.teradata.portal.admin.document.pojo;

import java.io.Serializable;
import java.util.List;

public class BaseRet implements Serializable {

	private static final long serialVersionUID = 535267947898351814L;
	private boolean retCode;
	private String retMsg;
	private Object addData;
	private Object addData1;
	private List<?> result;
	private int totalCount;
	public Object getAddData1() {
		return addData1;
	}
	public void setAddData1(Object addData1) {
		this.addData1 = addData1;
	}
	public Object getAddData() {
		return addData;
	}
	public void setAddData(Object addData) {
		this.addData = addData;
	}
	public List<?> getResult() {
		return result;
	}
	public void setResult(List<?> result) {
		this.result = result;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public boolean isRetCode() {
		return retCode;
	}
	public void setRetCode(boolean retCode) {
		this.retCode = retCode;
	}
	public String getRetMsg() {
		return retMsg;
	}
	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

}
