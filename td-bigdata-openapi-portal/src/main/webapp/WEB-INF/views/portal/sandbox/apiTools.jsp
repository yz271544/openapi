<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE HTML>
<html>
<!-- BEGIN HEAD -->
<head>
<%@include file="/WEB-INF/views/include/common.jsp" %>	
<title>API测试工具</title>

</head>
<!-- END HEAD -->
<body>
	<%@include file="/WEB-INF/views/include/header.jsp"%>
	<!-- BEGIN CONTENT -->
	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container">
					<!-- BEGIN PAGE CONTENT INNER -->
					<div class="page-content-inner">
						<div class="row ">
							<div class="col-md-12">
								<!-- BEGIN SAMPLE FORM PORTLET-->
								<div class="portlet light bordered">
									<div class="portlet-title">
										<div class="caption">
											<i class="icon-equalizer font-red-sunglo"></i> 
											<span class="caption-subject font-red-sunglo bold uppercase">API测试工具</span>
										</div>
									</div>
									<div class="portlet-body form">
										<div class="row ">
											<div class="col-md-6">
												<!-- BEGIN FORM-->
												<form action="#" id="sandboxForm" class="form-horizontal">
													<div class="form-body">
														<div class="row">
															<div class="col-md-12">
																<div class="form-group">
																	<label class="control-label col-md-5">返回格式(Format)</label>
																	<div class="col-md-7">
																		<select name="format" class="form-control">
																			<option value="json">JSON</option>
																			<option value="xml">XML</option>
																			<option value="txt">TXT</option>
																		</select>
																	</div>
																</div>
															</div>
														</div>
														<!--/row-->
														<div class="row">
															<div class="col-md-12">
																<div class="form-group">
																	<label class="control-label col-md-5">API类目(API Category)</label>
																	<div class="col-md-7">
																		<select id="api_sort" name="apiSort" class="form-control" >
																			
																		</select>
																	</div>
																</div>
															</div>
														</div>
														<!--/row-->
														<div class="row">
															<div class="col-md-12">
																<div class="form-group">
																	<label class="control-label col-md-5">API名称(API Name)</label>
																	<div class="col-md-7">
																		<select id="api_name" name="apiId" class="form-control" data-placeholder="--请选择API--" tabindex="1">
																			<option value="-1">--请选择API--</option>
																		</select>
																	</div>
																</div>
															</div>
														</div>
														<div class="row">
															<div class="col-md-12">
																<div class="form-group">
																	<label class="control-label col-md-5">API版本(API Version)</label>
																	<div class="col-md-7">
																		<select id="api_version" name="version" class="form-control" data-placeholder="--请选择API版本--">
																			<option value="-1">--请选择API版本--</option>
																		</select>
																	</div>
																</div>
															</div>
														</div>
														<div class="row">
															<div class="col-md-12">
																<div class="form-group">
																	<label class="control-label col-md-5">调用方式</label>
																	<div class="col-md-7">
																		<div class="radio-list">
																			<label class="radio-inline"> 
																				<input type="radio" name="reqType" value="syn"/> 同步
																			</label> 
																			<label class="radio-inline">
																				<input type="radio" name="reqType" value="asyn"/>异步
																			</label>
																		</div>
																	</div>
																</div>
															</div>
														</div>
														<div class="row">
															<div class="col-md-12">
																<div class="form-group">
																	<label class="control-label col-md-5">提交方式(Method)</label>
																	<div class="col-md-7">
																		<div class="radio-list">
																			<label class="radio-inline"> 
																				<input type="radio" name="invokeMethod" value="POST" checked />POST
																			</label> 
																			<label class="radio-inline"> 
																				<input type="radio" name="invokeMethod" value="GET" />GET
																			</label>
																		</div>
																	</div>
																</div>
															</div>
														</div>
														<div class="row">
															<div class="col-md-12">
																<div class="form-group">
																	<label class="control-label col-md-5">appKey <font color="red">*</font></label>
																	<div class="col-md-7">
																		<input type="text" class="form-control" id="app_key" name="appKey">
																	</div>
																</div>
															</div>
														</div>
														<div class="row">
															<div class="col-md-12">
																<div class="form-group">
																	<label class="control-label col-md-5">appSecret <font color="red">*</font></label>
																	<div class="col-md-7">
																		<input type="text" class="form-control" id="app_secret" name="appSecret">
																	</div>
																</div>
															</div>
														</div>
														<div class="row">
															<div class="col-md-12">
																<div class="form-group">
																	<label class="control-label">
																	&nbsp;将鼠标移至说明上，查看参数介绍；<font color="red">*</font> 表示必填，<font color="blue">*</font> 表示几个参数中必填一个;
																	<a id="api_link" href="#">查看API详情</a>
																	</label>
																</div>
															</div>
														</div>
														<div class="row">
															<div class="col-md-12">
																<div class="form-group">
																	<label class="control-label col-md-5">fields <font color="red">*</font></label>
																	<div class="col-md-7">
																		<input type="text" id="fields" class="form-control" name="fields">
																	</div>
																</div>
															</div>
														</div>
														<div id="dynamicArea">
														
														</div>
														<div id="pageInfoArea">
															<div class="row">
																<div class="col-md-12">
																	<div class="form-group">
																		<label class="control-label col-md-5">pageNum</label>
																		<div class="col-md-7">
																			<input type="text" id="pageNum" class="form-control" name="pageNum" maxlength="3">
																		</div>
																	</div>
																</div>
															</div>
															<div class="row">
																<div class="col-md-12">
																	<div class="form-group">
																		<label class="control-label col-md-5">pageSize</label>
																		<div class="col-md-7">
																			<input type="text" id="pageSize" class="form-control" name="pageSize">
																		</div>
																	</div>
																</div>
															</div>
														</div>
														<div class="form-actions">
															<div class="row">
																<div class="col-md-6">
																	<div class="row">
																		<div class="col-md-offset-3 col-md-9">
																			<input type="button" id="commit_btn" class="btn green" value="提交测试（Execute）"/>
																		</div>
																	</div>
																</div>
															</div>
														</div>
													</div>
												</form>
												<!-- END FORM-->
											</div>
											<div class="col-md-1"></div>
											<div class="col-md-5">
												<!-- BEGIN FORM-->
												<form action="#" class="form-horizontal">
													<div class="form-body">
														<div class="row">
															<div class="col-md-11">
																<div class="form-group">
																	<label>API请求参数(API Request)</label>
																	<textarea style="resize:vertical" class="form-control" id="req_url" rows="11"></textarea>
																</div>
															</div>
														</div>
														<!--/row-->
														<div class="row">
															<div class="col-md-11">
																<div class="form-group">
																	<label>API返回结果(API Response)</label>
																	<textarea style="resize:vertical" class="form-control" id="rep_val" rows="17"></textarea>
																</div>
															</div>
														</div>
													</div>
												</form>
												<!-- END FORM-->
											</div>
										</div>
									</div>
									<!-- END SAMPLE FORM PORTLET-->
								</div>
							</div>
						</div>
						<!-- END PAGE CONTENT INNER -->
					</div>
				</div>
				<!-- END PAGE CONTENT BODY -->
				<!-- END CONTENT BODY -->
			</div>
			<!-- END CONTENT -->
		</div>
	</div>
	<!-- END CONTENT -->
	<%@include file="/WEB-INF/views/include/footer.jsp"%>
	<script src="${ctx}/js/modules/sandbox/sandbox.js" type="text/javascript"></script>
	<script>
		var api_id = '${apiId}';
		var api_name = '${apiName}';
		var api_version = '${apiVersion}';
		var sort_id = '${sortId}';
		var source_flag = '${sourceFlag}';
		jQuery(document).ready(function() {
			if(sandbox.validSourceArg()){
                //alert('bbbbbbbbbbbb');
				sandbox.init();
			}
		});
	</script>
</body>
</html>