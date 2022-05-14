package com.teradata.portal.admin.subscribe.common;

import java.io.IOException;
import java.net.SocketException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPSClient;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.teradata.portal.admin.subscribe.pojo.FtpUseInfo;

/**
 * 
 * ftp,sftp测试连通性 <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年7月27日 下午7:41:54
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
public class FtpCommon {

	private static Log log = LogFactory.getLog(FtpCommon.class);

	private FTPClient ftpClient;

	private ChannelSftp sftp;

	private FtpUseInfo ftpInfo;

	public FtpCommon(FtpUseInfo ftpInfo) {
		this.ftpInfo = ftpInfo;
		if ("ftp".equals(ftpInfo.getFtpType())) {
			if ("TLS".equals(ftpInfo.getFtpProtocol())) {
				this.ftpClient = new FTPClient();
			} else {
				this.ftpClient = new FTPSClient();
			}
		}
	}

	/**
	 * 测试ftp的连通性
	 * 
	 * @param ftpInfo
	 * @return
	 * @author houbl
	 */
	public boolean isFtpConnection() {
		boolean isOpen = false;
		if (null == this.ftpClient) {
			return false;
		}
		try {
			if (!this.ftpClient.isConnected() || !this.ftpClient.isAvailable()) {
				isOpen = this.ftpLogin();
			}
		}
		catch (Exception e) {
			log.error("login ftp error!" + e.getMessage(), e);
			isOpen = false;
		}
		// 如果连接不上，关掉ftpClient,防止socket 出现close_wait
		if (!isOpen) {
			try {
				this.ftpClient.disconnect();
			}
			catch (IOException e) {
				log.error("disconnect ftp error!" + e.getMessage(), e);
			}
		}
		return isOpen;
	}

	private boolean ftpLogin() {
		boolean isLogin = false;

		try {
			if (ftpInfo.getFtpPort() != null && ftpInfo.getFtpPort() > 0) {
				this.ftpClient.connect(ftpInfo.getFtpHost(), ftpInfo.getFtpPort());
			} else {
				this.ftpClient.connect(ftpInfo.getFtpHost());
			}

			this.ftpClient.login(ftpInfo.getUserName(), ftpInfo.getPassword());
			this.ftpClient.changeWorkingDirectory(ftpInfo.getFtpPath());

			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

			if ("passive".equals(ftpInfo.getFtpMode())) {
				this.ftpClient.enterLocalPassiveMode();
			} else {
				this.ftpClient.enterLocalActiveMode();
			}

			isLogin = true;
			log.debug("connect FTP  OK!");
		}
		catch (SocketException e) {
			log.error("connect FTP  fail!" + e.getMessage(), e);
		}
		catch (IOException e) {
			log.error("read FTP IO fail!" + e.getMessage(), e);
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return isLogin;
	}

	/**
	 * 测试sftp的连通性
	 * 
	 * @param ftpInfo
	 * @return
	 * @author houbl
	 */
	public Boolean isSftpConnection() {
		boolean isLogin = false;
		Session sshSession = null;
		Channel channel = null;
		JSch jsch = new JSch();
		try {
			if (ftpInfo.getFtpPort() != null && ftpInfo.getFtpPort() > 0) {
				sshSession = jsch.getSession(ftpInfo.getUserName(), ftpInfo.getFtpHost(), ftpInfo.getFtpPort());
			} else {
				sshSession = jsch.getSession(ftpInfo.getUserName(), ftpInfo.getFtpHost());
			}
			sshSession.setPassword(ftpInfo.getPassword());
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setConfig(sshConfig);
			sshSession.connect();
			// 创建sftp通信通道
			channel = sshSession.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;

			// 进入服务器指定的文件夹
			sftp.cd(ftpInfo.getFtpPath());
			isLogin = true;
			log.debug("connect SFTP  OK!");
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		finally {
			if (sshSession != null) {
				sshSession.disconnect();
			}
			if (channel != null) {
				channel.disconnect();
			}
		}
		return isLogin;
	}

	public static void main(String[] args) {
		FtpUseInfo ftpInfo = new FtpUseInfo();
		ftpInfo.setFtpType("sftp");
		ftpInfo.setFtpHost("192.168.20.110");
		ftpInfo.setUserName("oapi");
		ftpInfo.setFtpPath("/data/open_api/");
		ftpInfo.setPassword("oapi");
		FtpCommon ftp = new FtpCommon(ftpInfo);
		System.out.println(ftp.isSftpConnection());
	}

}
