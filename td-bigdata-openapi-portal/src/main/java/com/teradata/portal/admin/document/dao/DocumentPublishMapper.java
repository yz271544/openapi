package com.teradata.portal.admin.document.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.teradata.portal.web.document.pojo.Document;
import com.teradata.portal.web.document.pojo.Node;

@Component
public interface DocumentPublishMapper {
	
	int addOpenApiPlatformDir(List<Node> dirs);
	
	int updOpenAptPlatformDir(List<Node> dirs);

	int delOpenAptPlatformDir(List<Node> dirs);
	
	int addSortDir(List<Node> dirs);
	
	int updSortDir(List<Node> dirs);

	int delSortDir(List<Node> dirs);
	
	int addDoc(Document doc);
	
	List<Node> queryDocTree();
	
	List<Node> querySortTree();
}
