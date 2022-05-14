<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE HTML>
<html>
<!-- BEGIN HEAD -->
<head>
<%@include file="/WEB-INF/views/include/common.jsp"%>
<title>openapi平台 - API测试工具</title>
<script src="${ctx}/js/modules/document/document.js" type="text/javascript"></script>
<style>
	.doc_btn {
		background-color : #3fc9d5;
	}
	.doc_btn:hover {
		background-color : #3598DC;
	}
	
	.tabbable-line>.nav-tabs>li>a {
	   color: #000000;
	}
	
	.tabbable-line>.nav-tabs>li>a:hover {
	   color: #ffffff;
	}
	
	.tabbable-line>.nav-tabs>li.active>a {
	   color: #ffffff;
	}
	
	.col-md-6 {
		width: 33%;
	}
	
	hr, p {
		margin:10px 0;
	}
	
	h3 {
		margin-top: 30px;
		margin-bottom: 30px;
	}
	
	.simplelineicons-demo .item-box {
		margin: 0 -.22em 0 0;
	}
	
	.simplelineicons-demo .item-box .item {
		padding: 20px 0 0 0;
	}
</style>
</head>
<!-- END HEAD -->
<body class="page-container-bg-solid page-boxed">
	<!-- BEGIN HEADER -->
	<%@include file="/WEB-INF/views/include/header.jsp"%>
	<!-- END HEADER -->
	<div class="page-container">
		<div class="page-content-wrapper">
			<div class="page-head" style="background-color:#3fc9d5;">
				<div class="container">
					<div class="row">
						<div class="page-title" style="padding:50px 0px 50px 0px;">
							<h1 style="color:#ffffff;">
								文档 & API <br /> <small style="color:#ffffff;">"文档中心包含开放平台所有平台接入、业务说明及API接口文档"</small>
							</h1>
						</div>
						<form class="search-form" action="page_general_search.html"
							method="GET">
							<div class="input-group" style="width: 400px; float: right; padding:55px 0px 55px 0px;">
								<input type="text" class="form-control" placeholder="Search" name="query" style="border-color:#48525e;" /> 
								<span class="input-group-btn" style="background-color:#48525e;"> 
									<a href="javascript:;" class="btn submit"> 
										<i class="icon-magnifier"></i>
									</a>
								</span>
							</div>
						</form>
					</div>
					<div class="row">
						<div class="portlet-title tabbable-line">
						    <ul class="nav nav-tabs">
						        <li class="active">
						            <a href="#openplatform" data-toggle="tab">开放平台</a>
						        </li>
						        <li>
						            <a href="#apidir" data-toggle="tab">API目录</a>
						        </li>
						    </ul>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="page-content-wrapper" style="margin-bottom:50px;">
			<div class="container">
				<div class="row">
					<div class="portlet-body util-btn-margin-bottom-5">
						<div class="tab-content">
							<div class="tab-pane active" id="openplatform">
								<c:forEach items="${dirs.getDoc()}" var="dir">
								<div class="col-md-6" style="text-align:left;font-size:18px;font-weight:bolder;">
									<h3>
										${dir.getNodeDesc()}
									</h3>
									<c:forEach items="${dir.getChildren()}" var="dl">
									<p style="font-size:18px;font-weight:normal;">
										<a href="${ctx}/document/documentDisplay.htm?nodeId=${dl.getNodeId()}">${dl.getNodeDesc()}</a>
									</p>
									<hr style="width:80%;" />
									</c:forEach>
								</div>
								</c:forEach>
							</div>
							<div class="tab-pane" id="apidir">
								<div class="simplelineicons-demo">
									<c:forEach items="${dirs.getSort()}" var="sort">
									<span class="item-box">
										<span class="item"><a href="${ctx}/apiDisplay/apiDisplay.htm?apiSort=${sort.getApiSort()}">${sort.getApiSortName()}</a></span>
										<span class="item">${sort.getApiSortDesc()}</span>
										<hr style="width:80%;" />
									</span>
									</c:forEach>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- BEGIN FOOTER -->
	<%@include file="/WEB-INF/views/include/footer.jsp"%>
	<!-- END FOOTER -->
</body>
<script>
$(".hor-menu ul").find("li").filter(".menu-dropdown").eq(1).addClass("active");
$(".hor-menu ul").find("li").filter(".menu-dropdown").eq(1).find("li").eq(1).addClass("active");
</script>
</html>