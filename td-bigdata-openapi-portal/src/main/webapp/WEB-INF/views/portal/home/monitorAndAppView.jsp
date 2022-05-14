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
/*page-content_end*/
</style>
<script src="${ctx}/js/modules/home/commonUtil.js"></script>
<script src="${ctx}/js/modules/home/monitorAndAppView.js"></script>
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
				<!-- BEGIN PAGE CONTENT INNER -->
				<div class="page-content-inner">
					<div class="row">
						<div class="col-md-12 col-sm-12">
							<div class="portlet light" id="portlet_1">
								<div class="portlet-title">
									<div class="caption font-red">
										<i class="icon-bell font-red bold"></i> <span
											class="caption-subject bold uppercase">待审批</span> <span
											class="caption-helper">(待审批注册工号、API访问权限、API发布信息)</span>
									</div>
								</div>
								<div class="portlet-body" id="portlet_body_1">
									<div class="row">
										<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12 margin-bottom-10">
											<div class="dashboard-stat blue">
												<div class="visual">
													<i class="fa fa-registered"></i>
												</div>
												<div class="details">
													<div class="number" id="register_num">&nbsp;</div>
													<div class="desc">注册工号</div>
												</div>
												<a class="more" href="javascript:;" moduleUrl="/registAppr/registAppr.htm"> 点击进入 <i
													class="m-icon-swapright m-icon-white"></i> </a>
											</div>
										</div>
										<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
											<div class="dashboard-stat red">
												<div class="visual">
													<i class="fa fa-unlock"></i>
												</div>
												<div class="details">
													<div class="number" id="auth_num">&nbsp;</div>
													<div class="desc">访问权限</div>
												</div>
												<a class="more" href="javascript:;" moduleUrl="/admin/right/authAppr.htm"> 点击进入 <i
													class="m-icon-swapright m-icon-white"></i> </a>
											</div>
										</div>
										<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
											<div class="dashboard-stat green">
												<div class="visual">
													<i class="fa fa-plus-square fa-icon-medium"></i>
												</div>
												<div class="details">
													<div class="number" id="approve_num">&nbsp;</div>
													<div class="desc">发布信息</div>
												</div>
												<a class="more" href="javascript:;" moduleUrl="/apiApprove/apiApprove.htm"> 点击进入 <i
													class="m-icon-swapright m-icon-white"></i> </a>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
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
											class="caption-helper">(API活跃数、非活跃数、状态异常数)</span>
									</div>
								</div>
								<div class="portlet-body" id="portlet_body_3">
									<div class="row" id="portlet_content_3_1">
										<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
											<div class="dashboard-stat2 ">
												<div class="display">
													<div class="number">
														<h3 class="font-green-sharp">
															<span data-counter="counterup" data-value="0" id="total_num"></span>
															<small class="font-green-sharp"></small>
														</h3>
														<small>API总量</small>
													</div>
													<div class="icon">
														<i class="fa fa-pie-chart"></i>
													</div>
												</div>
												<div class="progress-info">
													<div class="progress">
														<span style="width: 0%;"
															class="progress-bar progress-bar-success green-sharp" id="total_progress">
															 </span>
													</div>
													<div class="status">
														<div class="status-title"></div>
														<div class="status-number" id="total_status">0%</div>
													</div>
												</div>
											</div>
										</div>
										<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
											<div class="dashboard-stat2 ">
												<div class="display">
													<div class="number">
														<h3 class="font-blue">
															<span data-counter="counterup" data-value="0" id="active_num"></span>
														</h3>
														<small>活跃数</small>
													</div>
													<div class="icon">
														<i class="icon-like"></i>
													</div>
												</div>
												<div class="progress-info">
													<div class="progress">
														<span style="width: 0%;"
															class="progress-bar progress-bar-info blue-sharp" id="active_progress">
															</span>
													</div>
													<div class="status">
														<div class="status-title">占比</div>
														<div class="status-number" id="active_status">0%</div>
													</div>
												</div>
											</div>
										</div>
										<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
											<div class="dashboard-stat2 ">
												<div class="display">
													<div class="number">
														<h3 class="font-yellow">
															<span data-counter="counterup" data-value="0" id="unactive_num"></span>
														</h3>
														<small>非活跃数</small>
													</div>
													<div class="icon">
														<i class="icon-dislike"></i>
													</div>
												</div>
												<div class="progress-info">
													<div class="progress">
														<span style="width: 0%;"
															class="progress-bar progress-bar-warning" id="unactive_progress">  </span>
													</div>
													<div class="status">
														<div class="status-title">占比</div>
														<div class="status-number" id="unactive_status">0%</div>
													</div>
												</div>
											</div>
										</div>
										<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
											<div class="dashboard-stat2 ">
												<div class="display">
													<div class="number">
														<h3 class="font-red-haze">
															<span data-counter="counterup" data-value="0" id="error_num"></span>
														</h3>
														<small>状态异常数</small>
													</div>
													<div class="icon">
														<i class="fa fa-warning"></i>
													</div>
												</div>
												<div class="progress-info">
													<div class="progress">
														<span style="width: 0%;"
															class="progress-bar progress-bar-success red-haze" id="error_progress">
															</span>
													</div>
													<div class="status">
														<div class="status-title">占比</div>
														<div class="status-number" id="error_status">0%</div>
													</div>
												</div>
											</div>
										</div>
									</div>
									<div class="row" id="portlet_content_3_2">
										<div class="col-md-12">
											<div class="portlet light">
												<div class="portlet-title tabbable-line">
													<div class="caption fa fa-list font-green">详情</div>
												</div>
												<div class="portlet-body">
														<fieldset>
															<legend class="search-legend"><div class="fa fa-search"></div></legend>
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
																		<button type="button" class="btn default"
																			id="btn_reset">重置</button>
																	</div>
																</div>
															</form>
														</fieldset>
													<div class="table-scrollable">
														<table
															class="table table-striped table-bordered table-hover"
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
												</div>
											</div>
										</div>
									</div>
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