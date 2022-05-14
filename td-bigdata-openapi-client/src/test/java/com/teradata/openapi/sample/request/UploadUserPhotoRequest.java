package com.teradata.openapi.sample.request;

import com.teradata.openapi.rop.AbstractRopRequest;
import com.teradata.openapi.rop.request.UploadFile;

/**
 * <pre>
 * 功能说明：
 * </pre>
 * 
 * @author 陈雄华
 * @version 1.0
 */
public class UploadUserPhotoRequest extends AbstractRopRequest {

	private String userId;

	private UploadFile photo;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public UploadFile getPhoto() {
		return photo;
	}

	public void setPhoto(UploadFile photo) {
		this.photo = photo;
	}
}
