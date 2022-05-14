package com.teradata.portal.admin.document.service;

import java.util.List;

import com.teradata.portal.web.document.pojo.Document;
import com.teradata.portal.web.document.pojo.Node;

public interface DocumentPublishService {
	
	public void modifyDirStruct(List<Node> addList, List<Node> updList, List<Node> delList);
	
	public void modifySortDirStruct(List<Node> addList, List<Node> updList, List<Node> delList);
	
	public void addDoc(Document doc);
	
	public Node queryDocTree();
	
	public Node querySortTree();

}
