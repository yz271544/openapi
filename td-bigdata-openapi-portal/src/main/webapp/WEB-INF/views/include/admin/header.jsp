<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


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
			<div class="top-menu">
				<ul class="nav navbar-nav pull-right">
					<!-- BEGIN USER LOGIN DROPDOWN -->
					<li class="dropdown dropdown-user dropdown-dark">
						<a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-close-others="true"> 
						   <img alt="" class="img-circle" src="${ctx }/img/avatar.png"/> <span class="username username-hide-mobile" id="showUserName"></span>
						</a>
						<ul class="dropdown-menu dropdown-menu-default">
							<%--<li><a href="page_user_lock_1.html"><i class="icon-lock"></i> Lock Screen </a></li>
							<li><a href="page_user_login_1.html"><i class="icon-key"></i> Log Out </a></li>--%>
							<li><a href="/openapi/logout"><i class="icon-key"></i> 注销 </a></li>
						</ul>
					</li>
					<!-- END USER LOGIN DROPDOWN -->
				</ul>
			</div>
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
				<%--<ul class="nav navbar-nav">
					<li class="menu-dropdown classic-menu-dropdown">
						<a href="javascript:;">注册管理 <span class="arrow"></span></a>
						<ul class="dropdown-menu">
							<li><a href="ui_colors.html">注册审批</a></li>
							<li><a href="ui_general.html">权限申请</a></li>
							<li><a href="ui_general.html">权限审批</a></li>
						</ul>
					</li>
					<li class="menu-dropdown classic-menu-dropdown ">
						<a href="javascript:;"> API发布管理 <span class="arrow"></span></a>
						<ul class="dropdown-menu">
							<li><a href="ui_colors.html">API发布</a></li>
							<li><a href="ui_general.html">审批管理</a></li>
						</ul>
					</li>
					<li class="menu-dropdown classic-menu-dropdown ">
						<a href="javascript:;"> 文档中心 <span class="arrow"></span></a>
						<ul class="dropdown-menu">
							<li><a href="ui_colors.html">API目录管理</a></li>
							<li><a href="ui_general.html">文档目录管理</a></li>
						</ul>
					</li>
					<li class="menu-dropdown classic-menu-dropdown ">
						<a href="javascript:;"> 支持中心 <span class="arrow"></span></a>
						<ul class="dropdown-menu">
							<li><a href="ui_colors.html">APP视图</a></li>
							<li><a href="ui_general.html">开发视图</a></li>
							<li><a href="ui_general.html">运维视图</a></li>
							<li><a href="ui_general.html">订阅管理</a></li>
						</ul>
					</li>
					<li class="menu-dropdown mega-menu-dropdown"><a href="javascript:;"> 资源管理 <span class="arrow"></span></a></li>
					<li class="menu-dropdown classic-menu-dropdown ">
						<a href="javascript:;"> 日志管理 <span class="arrow"></span></a>
						<ul class="dropdown-menu">
							<li><a href="ui_colors.html">统计分析</a></li>
							<li><a href="ui_colors.html">日志监控</a></li>
						</ul>
					</li>
					<li class="menu-dropdown classic-menu-dropdown ">
						<a href="javascript:;"> 系统管理 <span class="arrow"></span></a>
						<ul class="dropdown-menu">
							<li><a href="${ctx }/admin/auth/userManage.htm">用户管理</a></li>
							<li><a href="${ctx }/admin/auth/grpManage.htm">组管理</a></li>
							&lt;%&ndash;<li><a href="ui_general.html">角色管理</a></li>&ndash;%&gt;
							<li><a href="ui_general.html">菜单管理</a></li>
							<li><a href="ui_general.html">权限管理</a></li>
						</ul>
					</li>
				</ul>--%>
			</div>
			<!-- END MEGA MENU -->
		</div>
	</div>
	<!-- END HEADER MENU -->
</div>
<!-- END HEADER -->

<script>
	$(function () {

		var showAllTree = function (treeJsonStr) {

			var output = '';

			for(var node = 0;node <treeJsonStr.length;node++){
				if(treeJsonStr[node].childrens.length > 0){
					var childrenHtml = showAllTree(treeJsonStr[node].childrens);
					output += '<li class="menu-dropdown classic-menu-dropdown" ' +
							'nodeid="'+treeJsonStr[node].id+'" ' +
							'nodeName="'+treeJsonStr[node].name+'"><a href="javascript:;">'+ treeJsonStr[node].name +' <span class="arrow"></span></a>' +
							'' + '<ul class="dropdown-menu">' + childrenHtml + '</ul></li>\n';
				}else{
					output += '<li nodeid="'+treeJsonStr[node].id+'" nodeName="'+treeJsonStr[node].name+'"><a href="'+$.basePath + treeJsonStr[node].link+'">' + treeJsonStr[node].name +  '</a></li>\n';
				}
			}

			return output;
		};


		var resTreeJson=<%=session.getAttribute("resourceTree")%>;
		var userJson=<%=session.getAttribute("userJson")%>;
		var treeStr = eval(resTreeJson);
		var user = eval(userJson);

		$('#showUserName').empty().append(user.userName);

		$('.hor-menu').empty().append('<ul class="nav navbar-nav">' + showAllTree(treeStr) + '</ul>');







	});
</script>