package com.teradata.portal.admin.document.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teradata.portal.admin.document.pojo.BaseRet;
import com.teradata.portal.admin.document.service.DocumentPublishService;
import com.teradata.portal.web.document.pojo.Document;
import com.teradata.portal.web.document.pojo.Node;
import com.teradata.portal.web.document.service.DocumentService;

@Controller
@RequestMapping(value = "/documentPublish")
public class DocumentPublishController {
	
	@Autowired
	DocumentService documentService;
	@Autowired
	DocumentPublishService documentPublishService;
	
	@RequestMapping(value = "/documentPublish.htm")
	public ModelAndView documentPublish(HttpSession httpSession, String arg) {
		Map<String, String> args = new HashMap<String, String>();
		String menuJson = (String) httpSession.getAttribute("resourceTree");
		args.put("menuJson", menuJson);
		args.put("arg", arg);
		return new ModelAndView("admin/document/documentPublish", "params", args);
	}
	
	@RequestMapping(value = "/getOpenPlatformDirs.json")
	@ResponseBody
	public Node getOpenPlatformDirs() {
		return documentPublishService.queryDocTree();
	}
	
	@RequestMapping(value = "/getOpenApiDirs.json")
	@ResponseBody
	public Node getOpenApiDirs() {
		return documentPublishService.querySortTree();
	}
	
	@RequestMapping(value = "/modifyDirStruct.json")
	@ResponseBody
	public BaseRet modifyDirStruct(String addJson, String updJson, String delJson) {
		BaseRet br = new BaseRet();
		Gson gson = new Gson();
		List<Node> add = gson.fromJson(addJson, new TypeToken<List<Node>>(){}.getType());
		List<Node> upd = gson.fromJson(updJson, new TypeToken<List<Node>>(){}.getType());
		List<Node> del = gson.fromJson(delJson, new TypeToken<List<Node>>(){}.getType());
		try {
			documentPublishService.modifyDirStruct(add, upd, del);
			br.setRetCode(true);
			br.setRetMsg("保存成功！");
			return br;
		} catch (Exception e) {
			e.printStackTrace();
			br.setRetCode(false);
			br.setRetMsg("系统错误！");
			return br;
		}
	}
	
	@RequestMapping(value = "/modifySortDirStruct.json")
	@ResponseBody
	public BaseRet modifySortDirStruct(String addJson, String updJson, String delJson) {
		BaseRet br = new BaseRet();
		Gson gson = new Gson();
		List<Node> add = gson.fromJson(addJson, new TypeToken<List<Node>>(){}.getType());
		List<Node> upd = gson.fromJson(updJson, new TypeToken<List<Node>>(){}.getType());
		List<Node> del = gson.fromJson(delJson, new TypeToken<List<Node>>(){}.getType());
		try {
			documentPublishService.modifySortDirStruct(add, upd, del);
			br.setRetCode(true);
			br.setRetMsg("保存成功！");
			return br;
		} catch (Exception e) {
			e.printStackTrace();
			br.setRetCode(false);
			br.setRetMsg("系统错误！");
			return br;
		}
	}
	
	@RequestMapping(value = "/addDoc.json")
	@ResponseBody
	public BaseRet addDoc(String docJson) {
		BaseRet br = new BaseRet();
		Gson gson = new Gson();
		Document doc = gson.fromJson(docJson, Document.class);
		try {
			documentPublishService.addDoc(doc);
			br.setRetCode(true);
			br.setRetMsg("发布成功！");
			return br;
		} catch (Exception e) {
			e.printStackTrace();
			br.setRetCode(false);
			br.setRetMsg("系统错误！");
			return br;
		}
	}

}
