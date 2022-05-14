package com.teradata.portal.admin.subscribe.pojo;

public class FtpUseInfo {

	private String ftpHost;

	private Integer ftpPort;

	private String userName;

	private String password;

	private String ftpPath = "/";

	private String ftpMode;// passive active

	private String ftpProtocol;// TLS SSL

	private String ftpType;// ftp sftp

	private Boolean isConnect;

	private String dataFileName;

	private String checkFileName;

	public String getFtpHost() {
		return ftpHost;
	}

	public void setFtpHost(String ftpHost) {
		this.ftpHost = ftpHost;
	}

	public Integer getFtpPort() {
		return ftpPort;
	}

	public void setFtpPort(Integer ftpPort) {
		this.ftpPort = ftpPort;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFtpPath() {
		return ftpPath;
	}

	public void setFtpPath(String ftpPath) {
		this.ftpPath = ftpPath;
	}

	public String getFtpMode() {
		return ftpMode;
	}

	public void setFtpMode(String ftpMode) {
		this.ftpMode = ftpMode;
	}

	public String getFtpType() {
		return ftpType;
	}

	public void setFtpType(String ftpType) {
		this.ftpType = ftpType;
	}

	public Boolean getIsConnect() {
		return isConnect;
	}

	public void setIsConnect(Boolean isConnect) {
		this.isConnect = isConnect;
	}

	public String getFtpProtocol() {
		return ftpProtocol;
	}

	public void setFtpProtocol(String ftpProtocol) {
		this.ftpProtocol = ftpProtocol;
	}

	public String getDataFileName() {
		return dataFileName;
	}

	public void setDataFileName(String dataFileName) {
		this.dataFileName = dataFileName;
	}

	public String getCheckFileName() {
		return checkFileName;
	}

	public void setCheckFileName(String checkFileName) {
		this.checkFileName = checkFileName;
	}

}
