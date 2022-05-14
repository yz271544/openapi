<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/include/taglib.jsp"%>
<script type="text/javascript" src="${ctx}/js/plugins/select2/js/select2.js"></script>
<script type="text/javascript" src="${ctx}/js/plugins/select2/js/i18n/zh-CN.js"></script>
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
	<h4 class="modal-title">订阅明细</h4>
</div>
<div class="modal-body form">
	<!-- BEGIN FORM-->
	<form action="#" id="rssInfoForm" class="form-horizontal">
		<div class="form-body">
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label col-md-4">AppKey</label>
						<div class="col-md-7">
							<input type="text" class="form-control" id="app_key" name="appkey" readonly value="${rssInfo.appkey }">
						</div>
					</div>
				</div>
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label col-md-4">API分类<span class="required"> * </span></label>
						<div class="col-md-7">
							<select id="api_sort_detail" name="apiSort" class="form-control">
								<option value="">--请选择API分类--</option>
							</select>
						</div>
					</div>
				</div>
			</div>
			<!--/row-->
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label col-md-4">API名称<span class="required"> * </span></label>
						<div class="col-md-7">
							<select id="api_name_detail" name="apiId" class="form-control" data-placeholder="--请选择API--" tabindex="1">
								<option value="">--请选择API--</option>
							</select>
						</div>
					</div>
				</div>
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label col-md-4">API版本<span class="required"> * </span></label>
						<div class="col-md-7">
							<select id="api_version_detail" name="apiVersion" class="form-control" data-placeholder="--请选择API版本--">
								<option value="">--请选择API版本--</option>
							</select>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label col-md-4">周期</label>
						<div class="col-md-7">
							<select name="dataCycleType" id="dataCycleType" class="form-control" readonly>

							</select>
						</div>
					</div>
				</div>
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label col-md-4">触发方式</label>
						<div class="col-md-7">
							<select name="triggerMethd" id="triggerMethod" class="form-control" readonly>

							</select>
						</div>
					</div>
				</div>
			</div>
			<div class="row" id="trigger_div" style="display:none">
				<div class="col-md-12">
					<div class="form-group" id="trigger_div">
						<label class="control-label col-md-2">触发时间<span class="required"> * </span></label>
						<div class="col-md-8">
							<input type="text" class="form-control" id="triggerSorc" name="triggerSorc" value="${rssInfo.triggerSorc }" />
							(例如.0 0 12 * * ? 每天12点触发 )
							<span class="help-block"></span>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label col-md-4">订阅格式</label>
						<div class="col-md-7">
							<select name="formCode" class="form-control">
								<c:choose>
									<c:when test="${rssInfo.formCode eq 'xml'}">
										<option value="json">JSON</option>
										<option value="xml" selected>XML</option>
										<option value="txt">TXT</option>
									</c:when>
									<c:when test="${rssInfo.formCode eq 'txt'}">
										<option value="json">JSON</option>
										<option value="xml">XML</option>
										<option value="txt" selected>TXT</option>
									</c:when>
									<c:otherwise>
										<option value="json" selected>JSON</option>
										<option value="xml">XML</option>
										<option value="txt">TXT</option>
									</c:otherwise>
								</c:choose>
							</select>
						</div>
					</div>
				</div>
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label col-md-4">订阅编码格式</label>
						<div class="col-md-8">
							<div class="radio-list">
								<c:if var="flag" test="${rssInfo.encode eq 'GBK'}">
									<label class="radio-inline">
									<input type="radio" name="encode" value="UTF-8" /> UTF8
									</label>
									<label class="radio-inline">
										<input type="radio" name="encode" value="GBK" checked/>GBK
									</label>
								</c:if>
							    <c:if test="${!flag }">
							   		<label class="radio-inline">
									<input type="radio" name="encode" value="UTF-8" checked/> UTF8
									</label>
									<label class="radio-inline">
										<input type="radio" name="encode" value="GBK" />GBK
									</label>
							    </c:if>

							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<div class="form-group">
						<label class="control-label col-md-2">响应参数<span class="required"> * </span></label>
						<div class="col-md-9">
							<div class="input-group">
								<textarea class="form-control" id="respnArg" rows="3" name="respnArgRender" readonly>${rssInfo.respnArgRender }</textarea>
								<span class="input-group-btn">
									<a data-toggle="modal" href="#rep_detail_div"><img src="${ctx }/img/u377.png" alt="配置" /></a>
								</span>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<div class="form-group">
						<label class="control-label col-md-2">请求参数<span class="required"> * </span></label>
						<div class="col-md-9">
							<div class="input-group">
								<textarea class="form-control" id="reqArg" rows="3" name="reqArgRender" readonly>${rssInfo.reqArgRender }</textarea>
								<span class="input-group-btn">
									<a data-toggle="modal" href="#req_detail_div"><img src="${ctx }/img/u377.png" alt="配置" /></a>
								</span>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label col-md-4">回吐方式<span class="required"> * </span> </label>
						<div class="col-md-7">
							<div class="input-group">
								<select name="format" class="form-control">
									<option value="1">FTP/SFTP</option>
									<!-- <option value="2">HTTP/HTTPS</option> -->
								</select>
								<span class="input-group-btn">
									<a data-toggle="modal" href="#recall_detail_div"><img src="${ctx }/img/u7.png" alt="配置" /></a>
								</span>
							</div>
						</div>
					</div>
				</div>
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label col-md-4">优先级 </label>
						<div class="col-md-8">
							<input id="priorityText" type="text" value="${rssInfo.priority }" name="priority">
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<div class="form-group" id="rss_data_div">
						<label class="control-label col-md-2">数据文件名<span class="required"> * </span></label>
						<div class="col-md-10">
							<div class="input-group">
								<input id="data_prefix" style="width:105px;height:32px" type="text" name="dataPrefix" placeholder="前缀">
								日期<input type="checkbox" id="datapre_date_switch" class="make-switch" checked  data-size="mini">
								_编号<input type="checkbox" id="datapre_num_switch" class="make-switch" checked  data-size="mini">&nbsp;.
								<input id="data_suffix" style="width:105px;height:32px" type="text" name="dataSuffix" placeholder="后缀">
								日期<input type="checkbox" id="datasuf_date_switch" class="make-switch" data-size="mini">
								_编号<input type="checkbox" id="datasuf_num_switch" class="make-switch"  data-size="mini">
							</div>
							<span class="help-block"></span>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<div class="form-group" id="rss_chk_div">
						<label class="control-label col-md-2">校验文件名<span class="required"> * </span></label>
						<div class="col-md-10">
							<div class="input-group">
								<input id="chk_prefix" style="width:105px;height:32px" type="text" name="chkPrefix" placeholder="前缀">
								日期<input type="checkbox" id="chkpre_date_switch" class="make-switch" checked  data-size="mini">&nbsp;.
								<input id="chk_suffix" style="width:105px;height:32px" type="text" name="chkSuffix" placeholder="后缀">
								日期<input type="checkbox" id="chksuf_date_switch" class="make-switch" data-size="mini">
							</div>
							<span class="help-block"></span>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group" id="rss_starttime_div">
						<label class="control-label col-md-4">订阅开始时间 <span class="required"> * </span></label>
						<div class="col-md-8">
							<div class="input-group date date-picker" id="start_date">
								<input type="text" id="rss_starttime" name="startTime" class="form-control" readonly
									value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>">
								 <span class="input-group-btn">
									<button class="btn default" type="button">
										<i class="fa fa-calendar"></i>
									</button>
								</span>
							</div>
							<span class="help-block"></span>
						</div>
					</div>
				</div>
				<div class="col-md-6">
					<div class="form-group" id="rss_endtime_div">
						<label class="control-label col-md-4">订阅结束时间<span class="required"> * </span></label>
						<div class="col-md-8">
							<div class="input-group date date-picker" id="end_date">
								<input type="text" id="rss_endtime" name="endTime" class="form-control" readonly
									value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>">
								<span class="input-group-btn">
									<button class="btn default" type="button">
										<i class="fa fa-calendar"></i>
									</button>
								</span>
							</div>
							<span class="help-block"></span>
						</div>
					</div>
				</div>
			</div>
		</div>
		<input type="hidden" name="rssId" value="${rssInfo.rssId }" />
	</form>
	<!-- END FORM-->
	<div class="modal-footer">
		<button type="button" class="btn default" data-dismiss="modal">关闭</button>
		<button type="button" class="btn blue" id="rss_save_btn">保存</button>
	</div>

	<!-- 响应参数配置区 -->
	<div id="rep_detail_div" class="modal modalextend fade bs-modal-lg" tabindex="-1" data-width="700">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
			<h4 class="modal-title">响应参数</h4>
		</div>
		<div class="modal-body form">
			<form action="#" id="repForm" class="form-horizontal">
				<div class="form-body">
					 <table class="table table-striped table-bordered table-hover table-checkable order-column" id="resarg_table">
                     </table>
				</div>
			</form>
			<div>
			   <div class="col-md-12">
				   <span class="label label-sm label-danger">提示:&nbsp;</span>如有置顶、上升、下降、置末操作请先进行相应操作后再进行参数选择
			   </div>
		    </div>
		</div>
		<div class="modal-footer">
			<button type="button" data-dismiss="modal" class="btn dark btn-outline">关闭</button>
			<button type="button" class="btn red" id="rep_detail_btn">确定</button>
		</div>
	</div>

	<!-- 请求参数配置区 -->
	<div id="req_detail_div" class="modal modalextend fade bs-modal-lg" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
			<h4 class="modal-title">请求参数</h4>
		</div>
		<div class="modal-body form">
			<form action="#" id="reqArgForm" class="form-horizontal">
				<div class="form-body">
					<div class="row">
						<div class="col-md-12">
							<div class="form-group">
								<label class="control-label">
								     &nbsp;<font color="red">*</font> 表示必填，<font color="blue">*</font> 表示几个参数中必填一个;<a href="#">查看API详情</a>
								</label>
							</div>
						</div>
					</div>
					<div id="reqDynamicArea">

					</div>
				</div>
			</form>
		</div>
		<div class="modal-footer">
			<button type="button" data-dismiss="modal" class="btn dark btn-outline">关闭</button>
			<button type="button" class="btn red" id="req_detail_btn">确定</button>
		</div>
	</div>

	<!-- 回吐方式配置区 -->
	<div id="recall_detail_div" class="modal modalextend fade bs-modal-lg" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
			<h4 class="modal-title">回吐方式</h4>
		</div>
		<div class="modal-body form">
			<form id="ftpInfoForm" class="form-horizontal">
				<div class="form-body">
					<div class="row">
						<div class="col-md-12">
							<div class="form-group">
								<label class="control-label col-md-3">回吐方式 </label>
								<div class="col-md-7">
									<select name="ftpType" class="form-control" id="ftp_type">
										<c:if var="flag" test="${rssInfo.ftpUseInfo.ftpType eq 'sftp'}">
											<option value="ftp">FTP</option>
											<option value="sftp" selected>SFTP</option>
										</c:if>
									    <c:if test="${!flag }">
									    	<option value="ftp" selected>FTP</option>
											<option value="sftp">SFTP</option>
									    </c:if>
									</select>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<div class="form-group">
								<label class="control-label col-md-3">IP地址  <span class="required"> * </span></label>
								<div class="col-md-7">
									<input type="text" class="form-control" id="ftp_host" name="ftpHost" value="${rssInfo.ftpUseInfo.ftpHost}">
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<div class="form-group">
								<label class="control-label col-md-3">端口<span class="required"> * </span></label>
								<div class="col-md-7">
									<input type="text" class="form-control" id="ftp_port" name="ftpPort" value="${rssInfo.ftpUseInfo.ftpPort}">
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<div class="form-group">
								<label class="control-label col-md-3">帐号<span class="required"> * </span></label>
								<div class="col-md-7">
									<input type="text" class="form-control" id="ftp_user" name="userName" value="${rssInfo.ftpUseInfo.userName}">
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<div class="form-group">
								<label class="control-label col-md-3">密码<span class="required"> * </span></label>
								<div class="col-md-7">
									<input type="text" class="form-control" id="ftp_passwd" name="password" value="${rssInfo.ftpUseInfo.password}">
								</div>
							</div>
						</div>
					</div>
					<div class="row" id="ftpProtocol_div">
						<div class="col-md-12">
							<div class="form-group">
								<label class="control-label col-md-3">ftp协议</label>
								<div class="col-md-7">
									<select name="ftpProtocol" class="form-control" id="ftp_protocol">
									    <c:if var="flag" test="${rssInfo.ftpUseInfo.ftpProtocol eq 'SSL'}">
											<option value="TLS">TLS</option>
											<option value="SSL" selected>SSL</option>
										</c:if>
									    <c:if test="${!flag }">
									    	<option value="TLS" selected>TLS</option>
											<option value="SSL">SSL</option>
									    </c:if>

									</select>
								</div>
							</div>
						</div>
					</div>
					<div class="row" id="ftpMode_div">
						<div class="col-md-12">
							<div class="form-group">
								<label class="control-label col-md-3">主被动方式</label>
								<div class="col-md-7">
									<select name="ftpMode" class="form-control" id="ftp_mode">
										<c:if var="flag" test="${rssInfo.ftpUseInfo.ftpProtocol eq 'passive'}">
										    <option value="active">active</option>
										    <option value="passive" selected>passive</option>
										</c:if>
									    <c:if test="${!flag }">
									    	 <option value="active" selected>active</option>
										    <option value="passive">passive</option>
									    </c:if>
									</select>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<div class="form-group">
								<label class="control-label col-md-3">存放路径<span class="required"> * </span></label>
								<div class="col-md-7">
									<input type="text" class="form-control" id="ftp_path" name="ftpPath" value="${rssInfo.ftpUseInfo.ftpPath}">
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<div class="form-group">
								<label class="control-label col-md-3">测试连通性</label>
								<div class="col-md-7">
									<img src="${ctx }/img/u602.png" alt="测试" id="testBtn"/>
								</div>
							</div>
						</div>
					</div>
				</div>
				<input type="hidden" name="isConnect" id="ftp_isconnect" value="${rssInfo.ftpUseInfo.isConnect ne null ? rssInfo.ftpUseInfo.isConnect : false}"/>
				<input type="hidden" name="dataFileName" id="data_file_name" value="${rssInfo.ftpUseInfo.dataFileName }" />
				<input type="hidden" name="checkFileName" id="check_file_name" value="${rssInfo.ftpUseInfo.checkFileName }" />
			</form>
		</div>
		<div class="modal-footer">
			<button type="button" data-dismiss="modal" class="btn dark btn-outline">关闭</button>
			<button type="submit" class="btn red" id="ftp_commit_btn">确定</button>
		</div>
	</div>
	<script src="${ctx}/js/modules/rss/rssOper.js" type="text/javascript"></script>
	<script>
		var method ='${method}';
		var api_id = '${rssInfo.apiId}';
		var api_name = '${rssInfo.apiName}';
		var api_version = '${rssInfo.apiVersion}';
		var sort_id = '${rssInfo.apiSort}';
		var data_cycle = '${rssInfo.dataCycleType}';
		var app_key = '${appKey}';

		var dataFileName = '${rssInfo.ftpUseInfo.dataFileName}';
		var checkFileName = '${rssInfo.ftpUseInfo.checkFileName}';
		jQuery(document).ready(function() {
			//初始化
			$("#datapre_date_switch").bootstrapSwitch();
			$("#datapre_num_switch").bootstrapSwitch();
			$("#datasuf_date_switch").bootstrapSwitch();
			$("#datasuf_num_switch").bootstrapSwitch();

			$("#chkpre_date_switch").bootstrapSwitch();
			$("#chksuf_date_switch").bootstrapSwitch();

			$("#priorityText").TouchSpin({
				 initval: 1,
				 buttondown_class: "btn blue",
		         buttonup_class: "btn red"
		    });

			if (jQuery().datepicker) {
				$('.date-picker').datepicker({
					rtl : App.isRTL(),
					orientation : "left",
					todayBtn : "linked",
					autoclose : true,
					todayHighlight : true,
					format : "yyyy-mm-dd",
					language : "zh-CN"
				});
			}
		    //开始时间
			$('#start_date').datepicker().on('changeDate',function(e){
	            var startTime = e.date;
	            $('#end_date').datepicker('setStartDate',startTime);
	        });
	        //结束时间：
	        $('#end_date').datepicker().on('changeDate',function(e){
	            var endTime = e.date;
	            $('#start_date').datepicker('setEndDate',endTime);
	        });


			rssOper.init();
			$("#rep_detail_btn").on("click",function(){
				rssOper.createResponseHtml();
			});

			$("#req_detail_btn").on("click",function(){
				rssOper.createRequestHtml();
			});

			$("#testBtn").on("click",function(){
				if($("#ftpInfoForm").valid()){
					rssOper.testIsConnect();
				}

			});

			$("#ftp_commit_btn").on("click",function(){
				if($("#ftpInfoForm").valid()){
					$("#recall_detail_div").modal("hide");
				}
			});

			$("#rss_save_btn").on("click",function(){
				if($("#rssInfoForm").valid()){
					//触发时间
					if($("#triggerMethod").val()== '2'){
						var triggerSorc = $("#triggerSorc").val();
						if(triggerSorc == ''){
							$("#trigger_div").addClass("has-error");
							$("#trigger_div").find(".help-block").html("触发时间是必填字段");
							return;
						}else{
							$("#trigger_div").removeClass("has-error");
							$("#trigger_div").find(".help-block").html("");
						}
					}
					//订阅开始结束时间校验
					var startTime = $("#rss_starttime").val();
					var endTime = $("#rss_endtime").val();
					var startDiv = $("#rss_starttime_div");
					var endDiv = $("#rss_endtime_div");
					if(startTime == ''){
						startDiv.addClass("has-error");
						startDiv.find(".help-block").html("订阅开始时间是必填字段");
						return;
					}else{
						startDiv.removeClass("has-error");
						startDiv.find(".help-block").html("");
					}

					if(endTime == ''){
						endDiv.addClass("has-error");
						endDiv.find(".help-block").html("订阅结束时间是必填字段");
						return;
					}else{
						endDiv.removeClass("has-error");
						endDiv.find(".help-block").html("");
					}

					var dataPrefix = $("#data_prefix").val();
					var dataSuffix = $("#data_suffix").val();
					var rssDataDiv = $("#rss_data_div");
					var chkPrefix = $("#chk_prefix").val();
					var chkSuffix = $("#chk_suffix").val();
					var rssChkDiv = $("#rss_chk_div");

					if($.trim(dataPrefix) == '' || $.trim(dataSuffix) == ''){
						rssDataDiv.addClass("has-error");
						rssDataDiv.find(".help-block").html("数据文件名前缀or后缀是必填字段");
						return;
					}else{
						rssDataDiv.removeClass("has-error");
						rssDataDiv.find(".help-block").html("");
					}

					if($.trim(chkPrefix) == '' || $.trim(chkSuffix) == ''){
						rssChkDiv.addClass("has-error");
						rssChkDiv.find(".help-block").html("校验文件名前缀or后缀是必填字段");
						return;
					}else{
						rssChkDiv.removeClass("has-error");
						rssChkDiv.find(".help-block").html("");
					}

					if($("#ftpInfoForm").valid()){
						var dataPreDataState = $("#datapre_date_switch").bootstrapSwitch("state");
						var dataPreNumState = $("#datapre_num_switch").bootstrapSwitch("state");
						var dataSufDataState = $("#datasuf_date_switch").bootstrapSwitch("state");
						var dataSufNumState = $("#datasuf_num_switch").bootstrapSwitch("state");

						var chkPreDataState = $("#chkpre_date_switch").bootstrapSwitch("state");
						var chkSufDataState = $("#chksuf_date_switch").bootstrapSwitch("state");

						var dataFileName = dataPrefix+"$"+dataPreDataState+"$"+dataPreNumState+"."+dataSuffix+"$"+dataSufDataState+"$"+dataSufNumState;
						var checkFileName = chkPrefix+"$"+chkPreDataState+"."+chkSuffix+"$"+chkSufDataState;

						$("#data_file_name").val(dataFileName);
						$("#check_file_name").val(checkFileName);
						rssOper.rssSaveEvent();
						/* //二次校验
						if($("#ftp_isconnect").val() == 'true'){

						}else{
							bootbox.alert("回吐方式Ftp测试未通过!");
						} */
					}else{
						bootbox.alert("回吐方式字段校验未通过,请核查!",function(){
							$("#recall_detail_div").modal("show");
						});
					}
				}
			});

		});
	</script>