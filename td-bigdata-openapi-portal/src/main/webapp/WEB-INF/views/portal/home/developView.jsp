<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE HTML>
<html>
<!-- BEGIN HEAD -->
<head>
<%@include file="/WEB-INF/views/include/apiRelease/common.jsp"%>
<title>${viewTitle}</title>
<style>
/*page-content_begin*/
.portlet-title.red {
	border-color: #e7505a !important;
}

.portlet-title.green {
	border-color: #32c5d2 !important;
}

.portlet-title.blue {
	border-color: #3598dc !important;
}

.chart-type-default {
	width:100%;
	height : 350px;
}
.search-legend{
	font-size : 16px !important;
	color:#A9A9A9;
	margin-bottom :0px !important;
}
#form_search{
	margin-bottom : 5px;
}
th, td {
	white-space: nowrap !important;
}
.font-25{
	font-size:25px;
}
.font-25-padding{
	padding-left:2px;
	padding-right:2px;
}
ul.sort-group li:hover{
	cursor:pointer;
}
/*page-content_end*/
</style>
<script src="${ctx}/js/modules/home/commonUtil.js"></script>
<script src="${ctx}/js/modules/home/developView.js"></script>
</head>
<!-- END HEAD -->
<body class="page-container-bg-solid page-boxed">
	<input type="hidden" id="viewType" value="${viewType}">
	<!-- BEGIN HEADER -->
	<%@include file="/WEB-INF/views/include/admin/header.jsp"%>
	<!-- END HEADER -->
	<!-- BEGIN CONTENT -->
	<div class="page-container">
		<div class="page-content">
			<div class="container">
				<ul class="page-breadcrumb breadcrumb sort-group">
                    <li class="font-green toolTip" title="开发者个人" ifGroup="0">
                        <i class="fa fa-circle"></i>
                        <span>个人</span>
                    </li>
                  	<li class="toolTip" title="开发者所属开发组" ifGroup="1">
                        <i class="fa fa-circle"></i>
                        <span>开发组</span>
                    </li>
                </ul>
				<!-- BEGIN PAGE CONTENT INNER -->
				<div class="page-content-inner">
					<div class="m-heading-1 border-green m-bordered" style="border-left-width:3px;" id="stateDesc">
						<h4>
							当前，累计开发API总数量为<span class="font-green font-25 font-25-padding" id="apiTotal">&nbsp;</span>个。此时间段被访问API<span
								class="font-green font-25 font-25-padding" id="visitedNum">&nbsp;</span>个，被访问<span
								class="font-green font-25 font-25-padding" id="visitedTime">&nbsp;</span>次，平均响应时长<span
								class="font-green font-25 font-25-padding" id="avgTime">&nbsp;</span>秒。
						</h4>
					</div>
					<div class="row">
						<div class="col-md-12 col-sm-12">
							<div class="portlet light ">
								<div class="portlet-title tabbable-line">
									<div class="caption font-green">
										<i class="icon-bar-chart font-green"></i><span
											class="caption-subject bold uppercase">API趋势分析</span> <span
											class="caption-helper">(API数量、访问次数、平均响应时长)</span>
									</div>
									<ul class="nav nav-tabs" id="nav_tabs_chart">
										<li><a href="#tab_chart_num" data-toggle="tab" chartId="chart_num" itemId="1001" chartUnit="个">
												API数量 </a></li>
										<li><a href="#tab_chart_visit" data-toggle="tab" chartId="chart_visit" itemId="2001" chartUnit="个"> 访问次数 </a></li>
										<li><a href="#tab_chart_time" data-toggle="tab" chartId="chart_time" itemId="3001" chartUnit="秒"> 平均时长 </a></li>
									</ul>
								</div>
								<div class="portlet-body" id="portlet_body_2">
									<div class="tab-content">
										<div class="tab-pane" id="tab_chart_num">
											<div id="chart_num" class="chart chart-type-default"></div>
										</div>
										<div class="tab-pane" id="tab_chart_visit">
											<div id="chart_visit" class="chart chart-type-default"></div>
										</div>
										<div class="tab-pane" id="tab_chart_time">
											<div id="chart_time" class="chart chart-type-default"></div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12 col-sm-12">
							<div class="portlet light">
								<div class="portlet-title">
									<div class="caption font-blue">
										<i class="fa fa-table font-blue"></i><span
											class="caption-subject bold uppercase">API状态分析</span> <span
											class="caption-helper">(API运行正常、异常分析)</span>
									</div>
								</div>
								<div class="portlet-body" id="portlet_body_3">
									<fieldset>
										<legend class="search-legend">
											<div class="fa fa-search"></div>
										</legend>
										<form class="search_form" id="form_search">
											<label class="radio-inline"> <input type="radio"
												name="apiStat" value="1" checked>正常</label> <label
												class="radio-inline"> <input type="radio"
												name="apiStat" value="0">异常</label>
											<div class="form-inline"
												style="margin-top:10px;margin-left:25px;">
												<div class="form-group" style="margin-right:10px;">
													<label class="control-label">分类：</label> <select
														class="form-control select2" id="sel_apiSort"
														name="apiSort">
														<option value='-1'>--请选择--</option>
													</select>
												</div>
												<div class="form-group" style="margin-right:10px;">
													<label class="control-label">调用方式：</label> <select
														class="form-control select2" id="sel_apiVisitMethd"
														name="apiVisitMethd">
														<option value='-1'>--请选择--</option>
														<option value='0'>同步</option>
														<option value='1'>异步</option>
														<option value='2'>订阅</option>
													</select>
												</div>
												<div class="form-group" style="margin-right:10px;">
													<label class="control-label">API名称：</label> <input
														type="text" class="form-control" id="txt_apiName"
														name="apiName">
												</div>
												<div class="form-group">
													<button type="button" class="btn green" id="btn_search">查询</button>
													<button type="button" class="btn default" id="btn_reset">重置</button>
												</div>
											</div>
										</form>
									</fieldset>
									<div class="table-scrollable">
										<table class="table table-striped table-bordered table-hover"
											id="tb_detail">
											<thead>
												<tr>
													<th>#</th>
													<th>分类</th>
													<th>API名称</th>
													<th>API描述</th>
													<th>调用方式</th>
													<th>访问次数</th>
													<th>响应时长(秒)</th>
													<th>当前状态</th>
													<th>最早使用时间</th>
													<th>最近使用时间</th>
												</tr>
											</thead>
											<tbody>
											</tbody>
										</table>
									</div>
									<div id="pg_total" class="pull-left selfer"></div>
									<div id="pg_detail" class="pull-right selfer"></div>
									<div class="clearfix"></div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- /.modal -->
	<!-- END CONTENT -->
	<!-- BEGIN FOOTER -->
	<%@include file="/WEB-INF/views/include/admin/footer.jsp"%>
	<!-- END FOOTER -->
</body>
</html>