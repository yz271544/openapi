package com.teradata.openapi.framework.message.request;

import java.io.Serializable;

public class SorcType implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1097794841133431154L;

	private Integer sorc_id;

	private String schemaName;

	private String tabName;

	private String tabAlias;

	private String sorc_field_type;

	private String sorc_format;

	private Integer sorc_max_len;

	private Integer sorc_total_digit;

	private Integer sorc_prec_digit;

	public Integer getSorc_id() {
		return sorc_id;
	}

	public void setSorc_id(Integer sorc_id) {
		this.sorc_id = sorc_id;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public String getTabName() {
		return tabName;
	}

	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	public String getTabAlias() {
		return tabAlias;
	}

	public void setTabAlias(String tabAlias) {
		this.tabAlias = tabAlias;
	}

	public String getSorc_field_type() {
		return sorc_field_type;
	}

	public void setSorc_field_type(String sorc_field_type) {
		this.sorc_field_type = sorc_field_type;
	}

	public String getSorc_format() {
		return sorc_format;
	}

	public void setSorc_format(String sorc_format) {
		this.sorc_format = sorc_format;
	}

	public Integer getSorc_max_len() {
		return sorc_max_len;
	}

	public void setSorc_max_len(Integer sorc_max_len) {
		this.sorc_max_len = sorc_max_len;
	}

	public Integer getSorc_total_digit() {
		return sorc_total_digit;
	}

	public void setSorc_total_digit(Integer sorc_total_digit) {
		this.sorc_total_digit = sorc_total_digit;
	}

	public Integer getSorc_prec_digit() {
		return sorc_prec_digit;
	}

	public void setSorc_prec_digit(Integer sorc_prec_digit) {
		this.sorc_prec_digit = sorc_prec_digit;
	}

}
