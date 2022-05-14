<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!-- BEGIN HEADER -->
<div class="page-header">
	<!-- BEGIN HEADER TOP -->
	<div class="page-header-top">
		<div class="container">
			<!-- BEGIN LOGO -->
			<div class="page-logo">
				<a href="index.html"> <img src="${ctx }/img/logo-default.jpg" alt="logo" class="logo-default" /></a>
			</div>
			<!-- END LOGO -->
			<!-- BEGIN RESPONSIVE MENU TOGGLER -->
			<a href="javascript:;" class="menu-toggler"></a>
			<!-- END RESPONSIVE MENU TOGGLER -->
			<!-- BEGIN TOP NAVIGATION MENU -->
			<%--<div class="top-menu">
				<ul class="nav navbar-nav pull-right">
					<!-- BEGIN USER LOGIN DROPDOWN -->
					<li class="dropdown dropdown-user dropdown-dark">
						<a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-close-others="true"> 
						   <img id="temp" alt="" class="img-circle" style="display: none;" src="${ctx }/img/avatar.png"/> <span class="username username-hide-mobile">Nick</span>
						</a>
						<ul class="dropdown-menu dropdown-menu-default">
							&lt;%&ndash;<li><a href="page_user_lock_1.html"><i class="icon-lock"></i> Lock Screen </a></li>
							<li><a href="page_user_login_1.html"><i class="icon-key"></i> Log Out </a></li>&ndash;%&gt;
							<li><a id="userName" href="/openapi/logout"><i class="icon-key"></i> 注销 </a></li>
						</ul>
					</li>
					<!-- END USER LOGIN DROPDOWN -->
				</ul>
			</div>--%>
			<!-- END TOP NAVIGATION MENU -->
		</div>
	</div>
	<!-- END HEADER TOP -->
	<!-- BEGIN HEADER MENU -->
	<div class="page-header-menu">
		<div class="container">
			<!-- BEGIN HEADER SEARCH BOX -->
			<%--<form class="search-form" action="page_general_search.html" method="GET">
				<div class="input-group">
					<input type="text" class="form-control" placeholder="Search" name="query"/> 
					<span class="input-group-btn">
						<a href="javascript:;" class="btn submit"> <i class="icon-magnifier"></i></a>
					</span>
				</div>
			</form>--%>
			<!-- END HEADER SEARCH BOX -->
			<!-- BEGIN MEGA MENU -->
			<div class="hor-menu">
				<ul class="nav navbar-nav">
					<li class="menu-dropdown classic-menu-dropdown">
						<a href="${ctx}/dashboard/index.htm">首&nbsp;页 <span class="arrow"></span></a>
					</li>
					<li class="menu-dropdown classic-menu-dropdown ">
						<a href="${ctx}/document/document.htm"> 文档中心 <span class="arrow"></span></a>
					</li>
					<li class="menu-dropdown classic-menu-dropdown ">
						<a href="${ctx}/apiDisplay/apiDisplay.htm"> &nbsp;API&nbsp; <span class="arrow"></span></a>
					</li>
					<li class="menu-dropdown mega-menu-dropdown"><a href="javascript:;"> 支持中心 <span class="arrow"></span></a></li>
					<li class="menu-dropdown classic-menu-dropdown "><a href="${ctx}/main.htm"> 控制台 <span class="arrow"></span></a></li>
				</ul>
			</div>
			<!-- END MEGA MENU -->
		</div>
	</div>
	<!-- END HEADER MENU -->
</div>
<!-- END HEADER -->