<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE HTML>
<html>
<!-- BEGIN HEAD -->
<head>
<%@include file="/WEB-INF/views/include/apiRelease/common.jsp"%>
<title>API发布</title>
<script src="${ctx}/js/modules/apiRelease/apiRelease.js"></script>
<script src="${ctx}/js/modules/home/commonUtil.js"></script>
<style>
/*page-content_begin*/
#tb_apiInfo td {
	white-space: nowrap;
}
#tb_apiInfo td .td-label {
	height: 30px;
	padding: 5px 0px;
	line-height: 1.5;
}
#container_apiArg .form-section{
	margin:10px 0px;
}
.text-white {
	color:#ffffff;
}
/*page-content_end*/
</style>
</head>
<!-- END HEAD -->
<body class="page-container-bg-solid page-boxed">
	<!-- BEGIN HEADER -->
	<%@include file="/WEB-INF/views/include/admin/header.jsp"%>
	<!-- END HEADER -->
	<!-- BEGIN CONTENT -->
	<div class="page-container container">
		<div class="tab-pane" id="tab_2">
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-search"></i>API发布
					</div>
					<div class="tools">
						<a href="javascript:;" class="collapse" title="折叠"> </a> <a
							href="javascript:;" class="fullscreen" title="全屏"> </a>
					</div>
				</div>
				<div class="portlet-body form">
					<!-- BEGIN FORM-->
					<form action="#" class="form-horizontal" id="form_apiSearch">
						<div class="form-body">
							<h4 class="form-section">查询区</h4>
							<div class="row">
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">分类：</label>
										<div class="col-md-9">
											<select class="form-control select2" id="sel_apiSort"
												name="apiSort">
												<option value=''>--请选择--</option>
											</select>
											<!--  <span class="help-block"> Select your gender. </span> -->
										</div>
									</div>
								</div>
								<!--/span-->
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">API_ID：</label>
										<div class="col-md-9">
											<input type="text" class="form-control" id="txt_apiId"
												name="apiId">
										</div>
									</div>
								</div>
								<!--/span-->
							</div>
							<!--/row-->
							<div class="row">
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">API名称：</label>
										<div class="col-md-9">
											<input type="text" class="form-control" id="txt_apiName"
												name="apiName">
										</div>
									</div>
								</div>
								<!--/span-->
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">状态：</label>
										<div class="col-md-9">
											<select class="form-control select2" id="sel_examStat"
												name="examStat">
												<option value=''>--请选择--</option>
											</select>
										</div>
									</div>
								</div>
							</div>
							<!--/row-->
							<div class="row">
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">创建开始日期:</label>
										<div class="col-md-9">
											<input class="form-control form-control-inline date-picker"
												id="date_beginDate" name="beginDate" />
										</div>
									</div>
								</div>
								<!--/span-->
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-3">创建结束日期:</label>
										<div class="col-md-9">
											<input class="form-control form-control-inline date-picker"
												id="date_endDate" name="endDate" />
										</div>
									</div>
								</div>
								<!--/span-->
							</div>
							<!--/row-->
							<div class="row">
								<div class="col-md-12">
									<div class="form-group">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">范围：</label>
												<div class="col-md-9">
													<div class="radio-list">
														<label class="radio-inline"> <input type="radio"
															name="apiRange" value="1" checked>我的创建</label> <label
															class="radio-inline"> <input type="radio"
															name="apiRange" value="2">所有创建</label>
													</div>
												</div>
											</div>
										</div>
										<div class="col-md-6">
											<div class="form-group">
												<button type="button" class="btn green" id="btn_search">查询</button>
												<button type="button" class="btn default" id="btn_reset">重置</button>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-md-12">
									<!-- BEGIN SAMPLE TABLE PORTLET-->
									<div class="portlet">
										<div class="portlet-title">
											<div class="caption">结果区</div>
											<div class="actions">
												<a href="javascript:;" class="btn blue btn-primary btn-sm"
													data-toggle="modal" data-target="#modal_apiItem"
													action="addApiItem" id="btn_addApiItem"> <i
													class="fa fa-plus"></i> 新增 </a>
											</div>
										</div>
										<div class="portlet-body" id="portlet_body_api_info">
											<div class="table-scrollable">
												<!-- <table class="table table-striped table-bordered table-hover">
                                                    <thead>
                                                         <tr>
                                                            <th scope="col" style="width:450px !important"> API_ID</th>
	                                                        <th scope="col"> API名称 </th>
	                                                        <th scope="col"> API描述 </th>
	                                                        <th scope="col"> 数据规模类型</th>
	                                                        <th scope="col"> 周期类型 </th>
	                                                        <th scope="col"> 商用版本号 </th>
	                                                        <th scope="col"> 开发版本号 </th>
	                                                        <th scope="col"> 开发版本创建时间 </th>
	                                                        <th scope="col"> 审批状态 </th>
	                                                        <th scope="col"> 当前版本创建人 </th>
	                                                        <th scope="col"> 有效状态 </th>
	                                                        <th scope="col"> 操作</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <tr>
                                                            <td> 客户指标类 </td>
	                                                        <td> 3141345 </td>
	                                                        <td> getUserNum </td>
	                                                        <td> 用户到达数 </td>
	                                                        <td> 指标 </td>
	                                                        <td> 日 </td>
	                                                        <td> 1 </td>
	                                                        <td> 2015-03-01 11:30:35 </td>
	                                                        <td> 已审批 </td>
	                                                        <td> 王XX </td>
	                                                        <td> 生效 </td>
	                                                        <td> <a href="javascript:;" class="btn dark btn-sm btn-outline sbold uppercase">
	                                                                    <i class="fa fa-share"></i> View </a>
	                                                             <a href="javascript:;" class="btn dark btn-sm btn-outline sbold uppercase">
	                                                                    <i class="fa fa-share"></i> View </a>
                                                            </td>
                                                        </tr>
                                                     </tbody>
                                              </table> -->
												<table
													class="table table-striped table-bordered table-hover"
													id="tb_apiInfo">
													<thead>
														<tr>
															<th>#</th>
															<th>API大类</th>
															<th>分类</th>
															<th>API_ID</th>
															<th>API名称</th>
															<th>API描述</th>
															<th>触发类型</th>
															<!-- <th>数据规模类型</th> -->
															<th>周期类型</th>
															<th>版本号</th>
															<!-- <th>商用版本号</th>
															<th>开发版本号</th> -->
															<th>开发版本创建时间</th>
															<th>审批状态</th>
															<th>当前版本创建人</th>
															<th>有效状态</th>
															<th class="text-center">操作</th>
														</tr>
													</thead>
													<tbody>
														<!-- <tr>
															<td>1</td>
															<td>客户指标类</td>
															<td>3141345</td>
															<td>getUserNum</td>
															<td>用户到达数</td>
															<td>指标</td>
															<td>日</td>
															<td>1</td>
															<td>1</td>
															<td>2015-03-01 11:30:35</td>
															<td>已审批</td>
															<td>王XX</td>
															<td>生效</td>
															<td class="text-centre"><a href="javascript:;"
																class="btn green btn-sm btn-outline sbold uppercase">
																	<i class="fa fa-edit"></i> 修改 </a> <a href="javascript:;"
																class="btn green btn-sm btn-outline sbold uppercase">
																	<i class="fa fa-plus"></i> 发布 </a> <a href="javascript:;"
																class="btn green btn-sm btn-outline sbold uppercase">
																	<i class="fa fa-times"></i> 删除 </a>
															</td>
														</tr>
														<tr>
															<td>2</td>
															<td>客户指标类</td>
															<td>4141345</td>
															<td>getUserNum</td>
															<td>用户到达数</td>
															<td>指标</td>
															<td>日</td>
															<td>1</td>
															<td>1</td>
															<td>2015-03-01 11:30:35</td>
															<td>已审批</td>
															<td>王XX</td>
															<td>生效</td>
															<td class="text-centre"><a href="javascript:;"
																class="btn green btn-sm btn-outline sbold uppercase">
																	<i class="fa fa-edit"></i> 修改 </a> <a href="javascript:;"
																class="btn green btn-sm btn-outline sbold uppercase">
																	<i class="fa fa-plus"></i> 发布 </a> <a href="javascript:;"
																class="btn green btn-sm btn-outline sbold uppercase">
																	<i class="fa fa-times"></i> 删除 </a>
															</td>
														</tr>
														<tr>
															<td>3</td>
															<td>客户指标类</td>
															<td>5141345</td>
															<td>getUserNum</td>
															<td>用户到达数</td>
															<td>指标</td>
															<td>日</td>
															<td>1</td>
															<td>1</td>
															<td>2015-03-01 11:30:35</td>
															<td>已审批</td>
															<td>王XX</td>
															<td>生效</td>
															<td class="text-centre"><a href="javascript:;"
																class="btn green btn-sm btn-outline sbold uppercase">
																	<i class="fa fa-edit"></i> 修改 </a> <a href="javascript:;"
																class="btn green btn-sm btn-outline sbold uppercase">
																	<i class="fa fa-plus"></i> 发布 </a> <a href="javascript:;"
																class="btn green btn-sm btn-outline sbold uppercase">
																	<i class="fa fa-times"></i> 删除 </a>
															</td>
															<td> <a href="javascript:;" class="btn dark btn-sm btn-outline sbold uppercase">
	                                                                    <i class="fa fa-share"></i> View </a>
	                                                             <a href="javascript:;" class="btn dark btn-sm btn-outline sbold uppercase">
	                                                                    <i class="fa fa-share"></i> View </a>
	                                                             <a href="javascript:;" class="btn dark btn-sm btn-outline sbold uppercase">
	                                                                    <i class="fa fa-share"></i> View </a>
                                                            </td>
														</tr>
														<tr>
															<td>4</td>
															<td>客户指标类</td>
															<td>4141345</td>
															<td>getUserNum</td>
															<td>用户到达数</td>
															<td>指标</td>
															<td>日</td>
															<td>1</td>
															<td>1</td>
															<td>2015-03-01 11:30:35</td>
															<td>已审批</td>
															<td>王XX</td>
															<td>生效</td>
															<td class="text-centre"><a href="javascript:;"
																class="btn green btn-sm btn-outline sbold uppercase">
																	<i class="fa fa-edit"></i> 修改 </a> <a href="javascript:;"
																class="btn green btn-sm btn-outline sbold uppercase">
																	<i class="fa fa-plus"></i> 发布 </a> <a href="javascript:;"
																class="btn green btn-sm btn-outline sbold uppercase">
																	<i class="fa fa-times"></i> 删除 </a>
															</td>
														</tr> -->
													</tbody>
												</table>
											</div>
											<div id="pg_total" class="pull-left selfer"></div>
											<div id="pg_detail" class="pull-right selfer"></div>
										</div>
									</div>
									<!-- END SAMPLE TABLE PORTLET-->
								</div>
							</div>
						</div>
					</form>
					<!-- END FORM-->
				</div>
			</div>
		</div>
	</div>
	<!-- /.modal -->
	<div id="modal_apiItem" class="modal fade" aria-hidden="false">
		<div class="modal-dialog">
			<div class="modal-content" id="modal_content_apiItem">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true"></button>
					<h4 class="modal-title" id="modal_apiItemTitle"></h4>
				</div>
				<div class="modal-body">
					<div class="scroller" style="height:395px" data-always-visible="1"
						data-rail-visible1="1">
						<form action="#" class="form-horizontal" id="form_apiItem">
							<div class="form-body">
								<div class="row col-md-12">
									<div class="form-group">
										<label class="control-label col-md-3">API_ID:<span
											class="required"> * </span> </label>
										<div class="col-md-3">
											<input id="txt_apiId_tmp" name="apiId" type="text"
												class="form-control" readonly
												style="border:0px;background-color:#ffffff;color:#000000;"
												value="92839713">
										</div>
										<label class="control-label col-md-3">API版本号:<span
											class="required"> * </span> </label>
										<div class="col-md-3">
											<!-- <input name="apiVersion" type="text" class="form-control" placeholder="Readonly" readonly style="border:0px;background-color:#ffffff;"> -->
											<input id="txt_apiVersion_tmp" name="apiVersion" type="text"
												class="form-control" readonly
												style="border:0px;background-color:#ffffff;color:#000000;">
										</div>
									</div>
								</div>
								<div class="row col-md-12">
									<div class="form-group">
										<label class="control-label col-md-3">API访问:<span
											class="required"> * </span> </label>
										<div class="col-md-9">
											 <div class="checkbox-list">
                                                 <label class="checkbox-inline">
                                                     <input type="checkbox" name="apiVisitMethd" value="0" checked> 同步 </label>
                                                 <label class="checkbox-inline">
                                                     <input type="checkbox" name="apiVisitMethd" value="1" checked> 异步 </label>
                                                 <label class="checkbox-inline">
                                                     <input type="checkbox" name="apiVisitMethd" value="2" checked> 订阅 </label>
                                             </div>
										</div>
									</div>
								</div>
								<div class="row col-md-12">
									<div class="form-group">
										<label class="control-label col-md-3">API名称:<span
											class="required"> * </span> </label>
										<div class="col-md-9">
											<div class="input-icon right">
												<i class="fa"></i> <input type="text" class="form-control"
													id="txt_apiName_tmp" name="apiName" readonly
													style="border-width:0px;background-color:#ffffff;color:#000000;">
											</div>
										</div>
									</div>
								</div>
								<div class="row col-md-12">
									<div class="form-group">
										<label class="control-label col-md-3">API描述:<span
											style="padding-right:11px;"></span> </label>
										<div class="col-md-9">
											<input type="text" class="form-control" id="txt_apiDesc_tmp"
												name="apiDesc">
										</div>
									</div>
								</div>
								<div class="row col-md-12">
									<div class="form-group">
										<label class="control-label col-md-3">API大类:<span
											class="required"> * </span> </label>
										<div class="col-md-9">
											<select class="form-control select2" id="sel_apiClsCode_tmp"
												name="apiClsCode">
												<option value=''>--请选择--</option>
											</select>
										</div>
									</div>
								</div>
								<div class="row col-md-12">
									<div class="form-group">
										<label class="control-label col-md-3">API分类:<span
											class="required"> * </span> </label>
										<div class="col-md-9">
											<select class="form-control select2" id="sel_apiSort_tmp"
												name="apiSort">
												<option value=''>--请选择--</option>
											</select>
										</div>
									</div>
								</div>
								<div class="row col-md-12">
									<div class="form-group">
										<label class="control-label col-md-3">API触发类型:<span
											class="required"> * </span> </label>
										<div class="col-md-9">
											<select class="form-control select2"
												id="sel_triggerMethd_tmp" name="triggerMethd">
												<option value=''>--请选择--</option>
											</select>
										</div>
									</div>
								</div>
								<div class="row col-md-12">
									<div class="form-group">
										<label class="control-label col-md-3">数据规模类型:<span
											class="required"> * </span> </label>
										<div class="col-md-9">
											<select class="form-control select2"
												id="sel_tabScaleType_tmp" name="tabScaleType">
												<option value=''>--请选择--</option>
												<option value="0">主要维度报表指标</option>
											</select>
										</div>
									</div>
								</div>
								<div class="row col-md-12">
									<div class="form-group">
										<label class="control-label col-md-3">周期类型:<span
											class="required"> * </span> </label>
										<div class="col-md-9">
											<select class="form-control select2"
												id="sel_dataCycleType_tmp" name="dataCycleType">
												<option value="">--请选择--</option>
												<option value="d">日</option>
												<option value="m">月</option>
											</select>
										</div>
									</div>
								</div>
								<div class="row col-md-12 ">
									<div class="form-group">
										<label class="control-label col-md-3">有效状态:<span
											class="required"> * </span> </label>
										<div class="col-md-9">
											<select class="form-control select2"
												id="sel_apiStateCode_tmp" name="apiStatCode">
												<option value=''>--请选择--</option>
											</select>
										</div>
									</div>
								</div>
							</div>
						</form>
					</div>
				</div>
				<div class="modal-footer" id="modal_footer_apiItem">
					<button id='btn_insertApiItem' type="button" class="btn green"
						title="新增API">新增</button>
					<button id='btn_updateApiItem' type="button" class="btn green"
						title="修改API">修改</button>
					<button id='btn_showApiArg' type="button" class="btn green"
						data-toggle="modal" data-target="#modal_apiDetail">查看详情</button>
					<button id='btn_configApiArg' type="button" class="btn green">配置详情</button>
					<button id='btn_cancel' type="button" class="btn default"
						data-dismiss="modal">关闭</button>
				</div>
			</div>
		</div>
	</div>

	<div id="modal_apiDetail" class="modal fade" aria-hidden="false">
		<div class="modal-dialog modal-full">
			<div class="modal-content" id="modal_content_apiDetail">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true"></button>
					<div class="row modal-title font-green" id="">
						<h4>
							<i class="fa fa-list-ul"></i>&nbsp;&nbsp;API参数详情<span
								id="modal_apiDetailTitle"></span>
						</h4>
					</div>
				</div>
				<div class="modal-body" style="min-height:410px;">
					<div class="portlet box green">
						<div class="portlet-title">
							<div class="caption">
								<i class="icon-settings"></i> <span class="caption-subject">数据源信息</span>
							</div>
							<div class="tools">
								<a href="javascript:;" class="collapse" title="折叠"> </a>
							</div>
						</div>
						<div class="portlet-body">
							<div class="table-scrollable table-scrollable-borderless">
								<table class="table table-hover" id="tb_apiDetail">
									<thead>
										<tr class="uppercase">
											<th>#</th>
											<th>数据源名称</th>
											<th>数据库名称</th>
											<th>表名称</th>
										</tr>
									</thead>
									<tbody>
										<!-- <tr>
											<td>1</td>
											<td>Mark</td>
											<td>Otto</td>
											<td>sanlim</td>
										</tr> -->
									</tbody>
								</table>
							</div>
						</div>
					</div>
					<div class="portlet box green">
						<div class="portlet-title">
							<div class="caption">
								<i class="icon-settings"></i> <span class="caption-subject">参数信息</span>
							</div>
							<div class="tools">
								<a href="javascript:;" class="collapse" title="折叠"> </a>
							</div>
						</div>
						<div class="portlet-body form">
							<!-- BEGIN FORM-->
							<form class="form-horizontal" role="form">
								<div class="form-body" id="container_apiArg"></div>
							</form>
							<!-- END FORM-->
						</div>
					</div>
				</div>
				<div class="modal-footer" id="modal_footer_apiArg">
					<button id='btn_cancel' type="button" class="btn default"
						data-dismiss="modal">关闭</button>
				</div>
			</div>
		</div>
	</div>
	<!-- END CONTENT -->
	<!-- BEGIN FOOTER -->
	<%@include file="/WEB-INF/views/include/admin/footer.jsp"%>
	<!-- END FOOTER -->
</body>
</html>