package com.teradata.portal.web.document.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.teradata.portal.web.document.pojo.Dirs;
import com.teradata.portal.web.document.pojo.Node;
import com.teradata.portal.web.document.service.DocumentService;

@Controller
@RequestMapping(value = "/document")
public class DocumentController {
	
	@Autowired
	DocumentService documentService;
	
	@RequestMapping(value = "/document.htm")
	public ModelAndView documentIndex() {
		Dirs dirs = documentService.getDirs();
		return new ModelAndView("portal/document/document", "dirs", dirs);
	}
	
	@RequestMapping(value = "/documentDisplay.htm")
	public ModelAndView documentDisplay(String nodeId) {
		return new ModelAndView("portal/document/documentDisplay", "nodeId", nodeId);
	}
	
	@RequestMapping(value = "/queryOpenApiPlatformDocs.json")
	@ResponseBody
	public Node queryOpenApiPlatformDocs() {
		return documentService.queryOpenApiPlatformDocs();
	}

}
