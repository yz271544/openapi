<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<!DOCTYPE html>
<html style="overflow-x:auto;overflow-y:auto;">
<head>
	<title>openapi平台 - <sitemesh:title/></title>
	<%@include file="/WEB-INF/views/include/common.jsp" %>		
	<sitemesh:head/>
</head>
 <body class="page-container-bg-solid page-header-menu-fixed page-boxed">
	<%@include file="/WEB-INF/views/include/header.jsp"%>
	<sitemesh:body/>
	<%@include file="/WEB-INF/views/include/footer.jsp"%>
</body>
</html>