<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml"> 
	<head> 
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="${ctx}/js/ext/extjs4/resources/example.css" />
		<link rel="stylesheet" type="text/css" href="${ctx}/js/ext/extjs4/resources/ext-theme-neptune/ext-theme-neptune-all-debug.css" />
		<link rel="stylesheet" type="text/css" href="${ctx}/js/ext/resources/css/public.css" />  
		<title>openapi平台 - 文档中心</title>
		<script>
			var projRoot = '${ctx}';
			var curPage = "oamenuapi";
			var apiSort = ${params.apiSort == null ? -100 : params.apiSort};
			var apiId = ${params.apiId == null ? -100 : params.apiId};
			var apiName = '${params.apiName == null ? "null" : params.apiName}';
			var apiVersion = '${params.apiVersion == null ? "null" : params.apiVersion}';
		</script>
		<!--引入自定义CSS--> 
		<script type="text/javascript" src="${ctx}/js/ext/extjs4/ext-all-debug.js"></script>
		<script type="text/javascript" src="${ctx}/js/ext/extjs4/locale/ext-lang-zh_CN.js"></script>
		<script type="text/javascript" src="${ctx}/js/ext/extjs4/extra.js"></script>
		<script type="text/javascript" src="${ctx}/js/ext/app/apiDis.js"></script>
		<style>
			.x-border-layout-ct {
				background-color : #0e2a47;
			}
			.x-panel-body-default {
				border-width : 0px;
			}
			.x-panel-default-framed {
			    border-radius: 0px;
			    padding: 0px 0px 0px 0px;
			    border-width: 0px;
			}
			.x-panel-default-framed {
			    border-color: #ffffff;
			}
			.x-column-header {
			    background-color: #ffffff;
			}
			.x-toolbar-default {
				border-width : 0px;
			}
			.x-html-editor-wrap textarea {
			  font: normal 13px "微软雅黑", arial, verdana, sans-serif;	
			  background-color: white;
			  resize: none;
			}
			.x-form-trigger-wrap {
			    border: 0px solid;
			}
			.x-border-layout-ct {
				background-color: #eff3f8;
			}
		</style>
	</head>
	<body> 
	</body> 
</html> 

