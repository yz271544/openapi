/**
 * 版权声明： 版权所有 违者必究 2012 日 期：12-8-1
 */
package com.teradata.openapi.rop.request;

import java.io.File;
import java.io.IOException;

import org.springframework.util.FileCopyUtils;

import com.teradata.openapi.rop.annotation.IgnoreSign;

/**
 * <pre>
 * 上传的文件
 * </pre>
 * 
 * @author 陈雄华
 * @version 1.0
 */
@IgnoreSign
public class UploadFile {

	private byte[] content;

	private String fileType;

	/**
	 * 根据文件构造
	 * 
	 * @param file
	 */
	public UploadFile(File file) {
		try {
			this.content = FileCopyUtils.copyToByteArray(file);
			this.fileType = file.getName().substring(file.getName().lastIndexOf('.') + 1);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 根据文件内容构造
	 * 
	 * @param content
	 */
	public UploadFile(String fileType, byte[] content) {
		this.content = content;
		this.fileType = fileType;
	}

	public byte[] getContent() {
		return content;
	}

	public String getFileType() {
		return fileType;
	}
}
