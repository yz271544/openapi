package com.teradata.openapi.framework.message.request;

import com.teradata.openapi.framework.util.FastJSONUtil;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ReqArg implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -7010445433791338776L;

	private String fieldName;

	private String fieldTargtType;

	private List<SorcType> field_sorc_type;

	private Integer calcPrincId;

	private List<String> fieldValue;

	private Integer mustType;

	private Map<String,List<SorcType>> expressionAtomSorcTypeMap;

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldTargtType() {
		return fieldTargtType;
	}

	public void setFieldTargtType(String fieldTargtType) {
		this.fieldTargtType = fieldTargtType;
	}

	public List<SorcType> getField_sorc_type() {
		return field_sorc_type;
	}

	public void setField_sorc_type(List<SorcType> field_sorc_type) {
		this.field_sorc_type = field_sorc_type;
	}

	public Integer getCalcPrincId() {
		return calcPrincId;
	}

	public void setCalcPrincId(Integer calcPrincId) {
		this.calcPrincId = calcPrincId;
	}

	public List<String> getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(List<String> fieldValue) {
		this.fieldValue = fieldValue;
	}

	public Integer getMustType() {
		return mustType;
	}

	public void setMustType(Integer mustType) {
		this.mustType = mustType;
	}

	public Map<String, List<SorcType>> getExpressionAtomSorcTypeMap() {
		return expressionAtomSorcTypeMap;
	}

	public void setExpressionAtomSorcTypeMap(Map<String, List<SorcType>> expressionAtomSorcTypeMap) {
		this.expressionAtomSorcTypeMap = expressionAtomSorcTypeMap;
	}
	@Override
	public String toString() {
		return FastJSONUtil.serialize(this);
	}
}
