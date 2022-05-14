package com.teradata.portal.web.document.service;

import java.util.List;

import com.teradata.portal.web.document.pojo.ApiSort;
import com.teradata.portal.web.document.pojo.Dirs;
import com.teradata.portal.web.document.pojo.Document;
import com.teradata.portal.web.document.pojo.Node;

public interface DocumentService {
	
	public Dirs getDirs();
	
	public List<Document> getOpenPlatformDirs();
	
	public List<ApiSort> getOpenApiDirs();
	
	public Node queryOpenApiPlatformDocs();

}
