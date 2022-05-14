package com.teradata.portal.web.document.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teradata.portal.web.document.dao.DocumentMapper;
import com.teradata.portal.web.document.pojo.ApiSort;
import com.teradata.portal.web.document.pojo.Dirs;
import com.teradata.portal.web.document.pojo.Document;
import com.teradata.portal.web.document.pojo.Node;
import com.teradata.portal.web.document.service.DocumentService;

@Service
public class DocumentServiceImpl implements DocumentService {
	
	@Autowired
	DocumentMapper documentMapper;

	@Override
	public Dirs getDirs() {
		Dirs dirs = new Dirs();
		List<Document> openPlatform = new ArrayList<Document>();
		List<ApiSort> api = new ArrayList<ApiSort>();
		openPlatform = documentMapper.queryOpenApiPlatformDirs();
		api = documentMapper.queryApiSort();
		dirs.setDoc(openPlatform);
		dirs.setSort(api);
		return dirs;
	}

	@Override
	public List<Document> getOpenPlatformDirs() {
		List<Document> openPlatform = new ArrayList<Document>();
		openPlatform = documentMapper.queryOpenApiPlatformDirs();
		return openPlatform;
	}

	@Override
	public List<ApiSort> getOpenApiDirs() {
		List<ApiSort> api = new ArrayList<ApiSort>();
		api = documentMapper.queryApiSort();
		return api;
	}

	@Override
	public Node queryOpenApiPlatformDocs() {
		List<Node> list = documentMapper.queryOpenApiPlatformDocs();
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

}
