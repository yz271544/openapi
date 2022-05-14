package com.teradata.portal.admin.document.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teradata.portal.admin.document.dao.DocumentPublishMapper;
import com.teradata.portal.admin.document.service.DocumentPublishService;
import com.teradata.portal.web.document.pojo.Document;
import com.teradata.portal.web.document.pojo.Node;

@Service
public class DocumentPublishServiceImpl implements DocumentPublishService {
	
	@Autowired
	DocumentPublishMapper documentPublishMapper;

	@Override
	public void modifyDirStruct(List<Node> addList, List<Node> updList, List<Node> delList) {
		if(addList != null && addList.size() > 0) {
			documentPublishMapper.addOpenApiPlatformDir(addList);
		}
		if(updList != null && updList.size() > 0) {
			documentPublishMapper.updOpenAptPlatformDir(updList);
		}
		if(delList != null && delList.size() > 0) {
			documentPublishMapper.delOpenAptPlatformDir(delList);
		}
	}
	
	@Override
	public void modifySortDirStruct(List<Node> addList, List<Node> updList, List<Node> delList) {
		if(addList != null && addList.size() > 0) {
			documentPublishMapper.addSortDir(addList);
		}
		if(updList != null && updList.size() > 0) {
			documentPublishMapper.updSortDir(updList);
		}
		if(delList != null && delList.size() > 0) {
			documentPublishMapper.delSortDir(delList);
		}
	}

	@Override
	public void addDoc(Document doc) {
		documentPublishMapper.addDoc(doc);
	}

	@Override
	public Node queryDocTree() {
		List<Node> list = documentPublishMapper.queryDocTree();
		Map<Integer, Node> map = list.stream().collect(Collectors.toMap(Node::getId, (p) -> p));
		Collections.reverse(list);
		for(Node n : list) {
			Node parent = map.get(n.getParentId());
			if(parent == null)
				continue;
			List<Node> children = parent.getChildren();
			if(children == null) {
				children = new ArrayList<Node>();
			}
			children.add(n);
			parent.setExpandable(true);
			parent.setExpanded(true);
			map.remove(n.getId());
		}
		Node root = new Node();
		root.setId(-1);
		root.setRoot(true);
		root.setText(".");
		root.getChildren().addAll(map.values());
		return root;
	}

	@Override
	public Node querySortTree() {
		List<Node> list = documentPublishMapper.querySortTree();
		Map<Integer, Node> map = list.stream().collect(Collectors.toMap(Node::getId, (p) -> p));
		Collections.reverse(list);
		for(Node n : list) {
			Node parent = map.get(n.getParentId());
			if(parent == null)
				continue;
			List<Node> children = parent.getChildren();
			if(children == null) {
				children = new ArrayList<Node>();
			}
			children.add(n);
			parent.setExpandable(true);
			parent.setExpanded(true);
			map.remove(n.getId());
		}
		Node root = new Node();
		root.setId(0);
		root.setRoot(true);
		root.setText(".");
		root.getChildren().addAll(map.values());
		return root;
	}

}
