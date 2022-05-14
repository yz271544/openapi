<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE HTML>
<html>
<!-- BEGIN HEAD -->
<head>
<%@include file="/WEB-INF/views/include/apiRelease/common.jsp"%>
<title>API参数配置</title>
<script src="${ctx}/js/modules/apiRelease/configApiArg.js"></script>
<style>
/*page-content_begin*/
#portlet_addDataSource {
	border: 1px dashed #32C5D2;
}

#portlet_addDataSource:hover {
	cursor: pointer;
}

#portlet_field,#portlet_fieldConfig {
	border: 1px solid #32C5D2;
}

.table-scrollable {
	margin: 0px !important;
}

#tb_fieldConfig {
	margin-bottom: 0px;
}

#tb_fieldConfig td {
	border: 1px solid #e7ecf1;
	white-space: nowrap;
	padding: 10px;
	overflow: hidden;
	text-overflow: ellipsis;
}

#tb_fieldConfig td .td-label {
	height: 30px;
	padding: 5px 10px;
	font-size: 12px;
	line-height: 1.5;
}

.calc_princ {
	font-size: 18px;
	padding: 5px 0px;
}

.calc_princ:hover {
	cursor: pointer;
}

.center {
  width: auto;
  display: table;
  margin-left: auto;
  margin-right: auto;
}
/*page-content_end*/

/*multi-select_begin*/
.ms-container {
	width: 100%;
}

.ms-container .ms-list {
	height: 400px;
}

.ms-container .ms-optgroup-label {
	color: #555;
	padding: 5px 0px 0px 2px;
}

.ms-container .ms-optgroup-label:hover {
	color: #ffffff;
	background-color: #0088CC;
}

.ms-container .ms-selectable li.ms-elem-selectable,.ms-container .ms-selection li.ms-elem-selection
	{
	margin: 2px 30px;
	padding: 2px;
	border-bottom: 1px dashed #eee;
}

.ms-container .ms-selectable li.ms-elem-selectable:hover,.ms-container .ms-selection li.ms-elem-selection:hover
	{
	border-bottom: 1px solid #eee;
}

.ms-header {
	padding: 8px 0px;
	background-color: #32C5D2;
	color: #ffffff;
}
/*multi-select_end*/
</style>
</head>
<body class="page-container-bg-solid page-boxed">
	<div class="page-container container">
		<div class="portlet light portlet-fit ">
			<div class="portlet-title">
				<div class="caption">
					<i class=" icon-layers font-green"></i> <span
						class="caption-subject font-green bold uppercase">API参数配置（<small>名称：${apiName
							}&nbsp;&nbsp;版本号：${apiVersion }</small>）</span> <input type="hidden" id="apiId"
						value="${apiId }" /> <input type="hidden" id="apiVersion"
						value="${apiVersion }" /> <input type="hidden" id="apiArg"
						value="${apiArg}" /> <input type="hidden" id="updateFlag"
						value="${updateFlag}" /><input type="hidden" id="apiVisitMethd"
						value="${apiVisitMethd}" />
				</div>
			</div>
			<div class="portlet-body">
				<div id="wizard">
					<ol>
						<li>数据源定义</li>
						<li>字段设置</li>
						<li>参数设置</li>
						<li>完成</li>
					</ol>
					<div class="row" style="margin:-10px;padding:0px;">
						<div id="portlet_container">
							<div class="portlet box green portlet-source">
								<div class="portlet-title">
									<div class="caption">
										数据源_<span class="sourceNum">1</span>
									</div>
									<div class="tools">
										<a href="javascript:;" class="collapse"></a>
									</div>
								</div>
								<div class="portlet-body form">
									<form action="#" class="form-horizontal" id="form_source1">
										<button class="btn_submit hide"></button>
										<div class="form-body">
											<div class="row">
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">数据源：<span
															class="required"> * </span> </label>
														<div class="col-md-9">
															<!-- <input type="text" id="typeahead_example_1" name="typeahead_example_1" class="form-control" />  -->
															<select class="form-control select2 sel_sourceId"
																id="sel_sourceId1" name="sourceId">
																<option value=''>--请选择--</option>
															</select>
														</div>
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">数据库名称：<span
															class="required"> * </span> </label>
														<div class="col-md-9">
															<select class="form-control select2 sel_schemaName"
																id="sel_schemaName1" name="schemaName">
																<option value=''>--请选择--</option>
															</select>
														</div>
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">表名称：<span
															class="required"> * </span> </label>
														<div class="col-md-9">
															<select
																class="form-control select2 select2-show-search sel_tabName"
																id="sel_tabName1" name="tabName">
																<option value=''>--请选择--</option>
															</select>
														</div>
													</div>
												</div>
											</div>
										</div>
									</form>
								</div>
							</div>
							<div class="portlet box green portlet-source hide"
								id="tpl_portlet_source">
								<div class="portlet-title">
									<div class="caption">数据源_克隆</div>
									<div class="tools">
										<a href="javascript:;" class="collapse"></a> <a
											href="javascript:;" class="remove"></a>
									</div>
								</div>
								<div class="portlet-body form">
									<form action="#" class="form-horizontal">
										<div class="form-body">
											<div class="row">
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">数据源：<span
															class="required"> * </span> </label>
														<div class="col-md-9">
															<!-- <input type="text" id="typeahead_example_1" name="typeahead_example_1" class="form-control" />  -->
															<select class="form-control select2 sel_sourceId"
																name="sourceId">
																<option value=''>--请选择--</option>
															</select>
														</div>
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">数据库名称：<span
															class="required"> * </span> </label>
														<div class="col-md-9">
															<select class="form-control select2 sel_schemaName"
																name="schemaName">
																<option value=''>--请选择--</option>
															</select>
														</div>
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-3">表名称：<span
															class="required"> * </span> </label>
														<div class="col-md-9">
															<select
																class="form-control select2 select2-show-search sel_tabName"
																name="tabName">
																<option value=''>--请选择--</option>
															</select>
														</div>
													</div>
												</div>
											</div>
										</div>
									</form>
								</div>
							</div>
							<div class="row" style="margin:0px;padding:0px;">
								<div class="row" style="margin:0px;padding:0px;">
									<div class="portlet box white toolTip"
										id="portlet_addDataSource" title="添加新的数据源">
										<div class="portlet-title">
											<div class="caption">
												<i class="fa fa-plus"></i> 添加数据源
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="row" style="margin:-10px;padding:0px;">
						<div class="portlet box green portlet-column" id="portlet_field">
							<div class="portlet-body form">
								<!-- BEGIN FORM-->
								<form action="#" class="form-horizontal form-row-seperated">
									<div class="form-body">
										<div class="form-group last">
											<div class="col-md-12">
												<select multiple="multiple" class="multi-select"
													id="multi_sel_field" name="multi_sel_field">
													<option>Dallas Cowboys</option>
													<option>New York Giants</option>
													<option>Philadelphia Eagles</option>
													<option>Washington Redskins</option>
												</select>
											</div>
										</div>
									</div>
								</form>
								<!-- END FORM-->
							</div>
						</div>
					</div>
					<div class="row" style="margin:-10px;padding:0px;">
						<!-- BEGIN Portlet PORTLET-->
						<div class="portlet box green portlet-fieldConfig"
							id="portlet_fieldConfig">
							<div class="portlet-body form">
								<div class="table-container table-scrollable"
									style="min-height:400px">
									<table class="table table-striped table-bordered table-hover"
										id="tb_fieldConfig">
										<thead>
											<tr>
												<th class="text-center" colspan="1" rowspan="2">字段名称</th>
												<th style="min-width: 100px;" class="text-center" colspan="1" rowspan="2" >字段描述</th>
												<th style="min-width: 130px;" class="text-center" colspan="1" rowspan="2">字段类型</th>
												<th style="min-width: 200px;" class="text-center hide" mustTypeDesc="sync" colspan="2" rowspan="1">同步</th>
												<th style="min-width: 200px;" class="text-center hide" mustTypeDesc="asyn" colspan="2" rowspan="1">异步</th>
												<th style="min-width: 200px;" class="text-center hide" mustTypeDesc="rss" colspan="2" rowspan="1">订阅</th>
												<th class="text-center" rowspan="2">请求参数标识<br>（全选<input type="checkbox" id="check_all_reqArg">）</th>
												<th style="min-width: 130px;" class="text-center" colspan="1" rowspan="2">请求默认值</th>
												<th style="min-width: 100px; class="text-center" colspan="1" rowspan="2">取值范围</th>
												<th class="text-center" colspan="1" rowspan="2">响应参数标识<br>（全选<input type="checkbox" id="check_all_respnArg">）</th>
												<th style="min-width: 130px;" class="text-center" colspan="1" rowspan="2">响应事例值</th>
											</tr>
											<tr>
												<th style="min-width: 120px;" class="text-center hide" mustTypeDesc="sync" colspan="1" rowspan="1">必选类型</th>
												<th style="min-width: 80px;"class="text-center hide" mustTypeDesc="sync" colspan="1" rowspan="1">组号</th>
												<th style="min-width: 120px;" class="text-center hide" mustTypeDesc="asyn" colspan="1" rowspan="1">必选类型</th>
												<th style="min-width: 80px;"class="text-center hide" mustTypeDesc="asyn" colspan="1" rowspan="1">组号</th>
												<th style="min-width: 120px;" class="text-center hide" mustTypeDesc="rss" colspan="1" rowspan="1">必选类型</th>
												<th style="min-width: 80px;"class="text-center hide" mustTypeDesc="rss" colspan="1" rowspan="1">组号</th>
											</tr>
										</thead>
										<tbody>
											<!-- <tr>
													<form action="#" class="form-horizontal">
														<td>
															<div class="td-label">DEAL_DATE</div> <input
															name="fieldName" type="text" class="hide"
															value="DEAL_DATE" /></td>
														<td><input name="fieldFileDesc" type="text"
															class="form-control form-filter input-sm" placeholder="描述" />
														</td>
														<td><select name="fieldTargtType"
															class="form-control form-filter input-sm">
																<option value="">Int</option>
																<option value="pending">BigInt</option>
																<option value="closed">Float</option>
														</select></td>
														<td><select name="mustType"
															class="form-control form-filter input-sm">
																<option value="">必选参数（周期）</option>
																<option value="pending">组内必须其一</option>
																<option value="closed">可选参数</option>
														</select></td>
														<td><select name="mustOneGrpId"
															class="form-control form-filter input-sm">
																<option value="1">11</option>
																<option value="pending">2</option>
																<option value="closed">3</option>
														</select></td>
														<td>
															<div name="reqArgId" class="text-center td-label">
																<input type="checkbox" id="inlineCheckbox1"
																	value="option1" />
															</div></td>
														<td><input name="reqArgDefltVal" type="text"
															class="form-control form-filter input-sm" placeholder="" />
														</td>
														<td>
															<div class="td-label">img</div></td>
														<td>
															<div class="text-center td-label">
																<input name="respnArgId" type="checkbox"
																	id="inlineCheckbox1" value="option1" />
															</div></td>
														<td><input name="respnArgSampVal" type="text"
															class="form-control form-filter input-sm" placeholder="" />
														</td>
													</form>
												</tr> -->
										</tbody>
									</table>
								</div>
							</div>
						</div>
						<!-- END Portlet PORTLET-->
					</div>
					<div class="row" style="margin:-10px;padding:0px;background-color:#ffffff;border:1px solid #32C5D2;">
						<div id="submitContainer">
							<div class="center" style="min-height:200px;line-height:200px;">
									<button id='btn_apiArgSubmit' type="button" class="btn green" title="提交"><i class="fa fa-save"></i>&nbsp;提交</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="modal_calcPrinc" class="modal fade" aria-hidden="false">
		<div class="modal-dialog">
			<div class="modal-content" id="modal_content_calcPrinc">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true"></button>
					<h4 class="modal-title font-green">
						<i class="fa fa-edit"></i>&nbsp;配置取值范围
					</h4>
				</div>
				<div class="modal-body">
					<div class="scroller" style="height:200px" data-always-visible="1"
						data-rail-visible1="1">
						<form action="#" class="form-horizontal" id="form_calcPrinc">
							<div class="form-body">
								<div class="row col-md-12">
									<div class="form-group">
										<label class="control-label col-md-3">字段名:<span
											class="required"> * </span>
										</label>
										<div class="col-md-9">
											<p class="form-control-static" id="txt_fieldName_tmp">DEAL_DATE</p>
										</div>
									</div>
								</div>
								<div class="row col-md-12">
									<div class="form-group">
										<label class="control-label	col-md-3">计算法则:<span
											class="required"> * </span>
										</label>
										<div class="col-md-9">
											<select class="form-control select2" id="sel_calcPrincId_tmp"
												name="calcPrincId">
											</select>
										</div>
									</div>
								</div>
								<div class="row col-md-12">
									<div class="form-group">
										<label class="control-label col-md-3">值域:<span
											class="required"> &nbsp; </span>
										</label>
										<div class="col-md-9">
											<textarea class="form-control" id="txtarea_valueRange_tmp"
												rows="3" placeholder="请输入值域，多个值用逗号分割"
												disabled="disabled"></textarea>
										</div>
									</div>
								</div>
							</div>
						</form>
					</div>
				</div>
				<div class="modal-footer" id="modal_footer_apiItem">
					<button id='btn_saveCalcPrinc' type="button" class="btn green">设置</button>
					<button id='btn_cancelCalcPrinc' type="button" class="btn default"
						data-dismiss="modal">关闭</button>
				</div>
			</div>
		</div>
	</div>
	<!-- END CONTENT -->
	<!--[if lt IE 9]>
	<script src="${ctx}/js/plugins/respond.min.js"></script>
	<script src="${ctx}/js/plugins/excanvas.min.js"></script> 
	<![endif]-->
</body>
</html>