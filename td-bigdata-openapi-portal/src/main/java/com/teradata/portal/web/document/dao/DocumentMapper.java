package com.teradata.portal.web.document.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.teradata.portal.web.document.pojo.ApiSort;
import com.teradata.portal.web.document.pojo.Document;
import com.teradata.portal.web.document.pojo.Node;

@Component
public interface DocumentMapper {
	
	public List<Document> queryOpenApiPlatformDirs();
	
	public List<ApiSort> queryApiSort();
	
	public List<Node> queryOpenApiPlatformDocs();

}
