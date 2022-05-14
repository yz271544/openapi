var sandbox = (function() {
	return {
		init : function() {
            //alert("sourceFlag=" + source_flag + " apiSort=" + sort_id + " api_id=" + api_id + " api_version=" + api_version) ;

            $("input[name='reqType']").eq(0).prop("checked",true);
			$("input[name='reqType']").eq(0).parent('span').addClass('checked');
			var url = $.basePath + "/apitools/getSelectJson.json?sourceFlag=" + source_flag;
			// 初始化api类目
			sandbox.bindSelect("api_sort", url + "&selectName=apiSort", sort_id);
			// 初始化api名称
			sandbox.bindSelect("api_name", url + "&selectName=apiName&apiSort=" + sort_id, api_id);
			// 初始化api版本
			sandbox.bindSelect("api_version", url + "&selectName=apiVersion&apiSort=" + sort_id + "&apiId=" + api_id, api_version);
			
			//同步 异步添加事件
			$("input[name='reqType']").on("click",function(){
				if($(this).val() == 'syn'){
					$("#pageInfoArea").show();
				}else{
					$("#pageInfoArea").hide();
				}
				$("#pageNum").val("");
				$("#pageSize").val("");
				var apiId = $("#api_name").val();
				var apiVersion = $("#api_version").val();
				if(apiId != -1 && apiVersion != -1){
					sandbox.createDynamicArgArea(apiId,apiVersion);
				}
				
			});

			if (source_flag != '2') {
				$("#api_sort").on("change", function(e) {
					var apiSort = $("#api_sort").val();
					sandbox.clearSelect("api_name,api_version");
					if (apiSort != '-1') {
						sandbox.bindSelect("api_name", url + "&selectName=apiName&apiSort=" + apiSort);
					}
				});

				$("#api_name").on("change", function(e) {
					var apiSort = $("#api_sort").val();
					var apiId = $("#api_name").val();
					sandbox.clearSelect("api_version");
					if (apiId != "-1") {
						sandbox.bindSelect("api_version", url + "&selectName=apiVersion&apiSort=" + apiSort + "&apiId=" + apiId);
					}
				});

				$("#api_version").on("change", function(e) {
					var apiSort = $("#api_sort").val();
					var apiId = $("#api_name").val();
					var apiVersion = $("#api_version").val();
					if(apiVersion != '-1'){
						sandbox.createDynamicArgArea(apiId,apiVersion);
						//拼装api详情链接
						var api_link = $.basePath +"/apiDisplay/apiDisplay.htm?apiSort="+apiSort+"&apiId="+apiId+"&apiVersion="+apiVersion;
						$("#api_link").attr("href",api_link).attr("target","_blank");
					}else{
						sandbox.clearSelect(null);
					}
				
				});
			}else{
				$("#publish_btn").hide();
				$("#app_key").val("系统分配(default)").attr("readonly","true");
				$("#app_secret").val("系统分配(default)").attr("readonly","true");
				
				//拼装api详情链接
				var api_link = $.basePath +"/apiDisplay/apiDisplay.htm?apiSort="+sort_id+"&apiId="+api_id+"&apiVersion="+api_version;
				$("#api_link").attr("href",api_link).attr("target","_blank");
				
				var api = frameElement.api, W = api.opener;
				$("#publish_btn").on("click",function(){
					var param = {
						apiId : api_id,
						apiVersion : api_version,
						examStat : 1
					};
					$.ajax({
						url :$.basePath+'/apiRelease/releaseApiItem.action',
						type : "post",
						data : param,
						dataType : "json",
						success : function(data) {
							if (!showError(data)) {
								bootbox.alert("发布成功",function(){
									api.close();
									W.refreshPage();
								});
							}
						},
						error : function(data, e) {
							bootbox.alert("系统错误,请稍候再试！");
						}
					});

				});
			}
			
			$("#commit_btn").on("click", function() {
				sandbox.commitForm();
			});
		},
		bindSelect : function(ctrlName, url, checkedValue) {
			var control = $('#' + ctrlName);
			// 针对api发布测试用 source_flag = 1 表示已经发布成功的测试 2表示未发布成功进行的测试
			if (source_flag == '2' && ctrlName != 'api_sort') {
				control.empty();
				if (ctrlName == 'api_name') {
					control.append("<option value='" + checkedValue + "'>" + api_name + "</option>");
				} else if (ctrlName == 'api_version') {
					control.append("<option value='" + checkedValue + "'>" + checkedValue + "</option>");
				}

			} else {
				var async_flag = true;
				if (ctrlName != 'api_sort') {
					async_flag = false;
				}
				// 绑定Ajax的内容
				$.ajax({
					cache : false,
					url : url,
					type : "post",
					async : async_flag,
					dataType : "json",
					success : function(data) {
						if (!showError(data)) {
							control.empty();// 清空下拉框

							if (source_flag == '2') {
								$.each(data, function(i, item) {
									if (checkedValue != null && checkedValue != '') {
										if (checkedValue == item.Value) {
											control.append("<option value='" + item.Value + "' selected='selected'>" + item.Text + "</option>");
										}
									}
								});
							} else {
								control.append("<option value='-1'>--请选择--</option>");
								$.each(data, function(i, item) {
									if (checkedValue != null && checkedValue != '') {
										if (checkedValue == item.Value) {
											control.append("<option value='" + item.Value + "' selected='selected'>" + item.Text + "</option>");
										} else {
											control.append("<option value='" + item.Value + "'>" + item.Text + "</option>");
										}
									} else {
										control.append("<option value='" + item.Value + "'>" + item.Text + "</option>");
									}

								});
							}

						}
					},
					error : function(data, e) {
						bootbox.alert("系统错误,请稍候再试！");
					}
				});
			}
			if (ctrlName == 'api_version' && control.val() != '-1') {
				//拼装api详情链接
				var api_link = $.basePath +"/apiDisplay/apiDisplay.htm?apiSort="+sort_id+"&apiId="+api_id+"&apiVersion="+api_version;
				$("#api_link").attr("href",api_link).attr("target","_blank");
				sandbox.createDynamicArgArea($("#api_name").val(), control.val());
			}
		},
		clearSelect : function(ctrlName) {
			if(ctrlName != null){
				var controls = ctrlName.split(",");
				for (var i = 0; i < controls.length; i++) {
					var control = $('#' + controls[i]);
					control.empty();
					control.append("<option value='-1'>--请选择--</option>");
				}
			}
			// 清除api详情链接
			$("#api_link").attr("href","#").attr("target","");
			// 清除fields值
			$("#fields").val("");
			// 清除动态区数据
			$("#dynamicArea").empty();
			//清除同步分页信息
			$("#pageNum").val("");
			$("#pageSize").val("");
		},
		reqMethodArea : function(api_id, api_version){
			$.ajax({
				cache : false,
				url : $.basePath + "/apitools/getApiReqMethodJson.json",
				type : "post",
				async : false,
				data : {
					apiId : api_id,
					apiVersion : api_version
				},
				dataType : "text",
				success : function(data) {
					if (!showError(data)) {
						var $reqType = $("input[name='reqType']");
						//$reqType.prop("checked",false);
						$reqType.parent('span').removeClass('checked');
						$reqType.parent('span').parent('div').removeClass("disabled");
						$reqType.prop("disabled",false);
						//同步
						if(data.indexOf("0") > -1 && data.indexOf("1") == -1){
							$reqType.eq(0).prop("checked",true);
							$reqType.eq(0).parent('span').addClass('checked');
							$reqType.eq(1).prop("disabled",true);
							$reqType.eq(1).parent('span').parent('div').addClass("disabled");
							$("#pageInfoArea").show();
						}
						//异步
						if(data.indexOf("0") == -1 && data.indexOf("1") > -1){
							$reqType.eq(1).prop("checked",true);
							$reqType.eq(1).parent('span').addClass('checked');
							$reqType.eq(0).prop("disabled",true);
							$reqType.eq(0).parent('span').parent('div').addClass("disabled");
							$("#pageInfoArea").hide();
						}
						
						if(data.indexOf("0") == -1 && data.indexOf("1") == -1){
							$reqType.prop("disabled",true);
							$reqType.prop("checked",false);
							$reqType.parent('span').removeClass('checked');
							$reqType.parent('span').parent('div').addClass("disabled");
						}
						
						if(data.indexOf("0") > -1 && data.indexOf("1") > -1){
							$("input[name='reqType']:checked").parent('span').addClass('checked');
						}
					}
				},
				error : function(data, e) {
					bootbox.alert("系统错误,请稍候再试！");
				}
			});
		},
		dynamicArgArea : function(api_id, api_version,req_method) {
			$.ajax({
				cache : false,
				url : $.basePath + "/apitools/getDynamicArgAreaJson.json",
				type : "post",
				data : {
					apiId : api_id,
					apiVersion : api_version,
					reqMethod :req_method
				},
				dataType : "json",
				beforeSend : function(XMLHttpRequest) {
					$("#dynamicArea").empty();
				},
				success : function(data) {
					if (!showError(data)) {
						$("#fields").val(data.fields);
						// 组装动态区域参数数据
						var html = [];
						var requiredArgList = data.requiredArgList;
						for (var i = 0; i < requiredArgList.length; i++) {
							sandbox.createRowHtml(html, requiredArgList[i], "0");
						}
						var chooseOneArgMap = data.chooseOneArgMap;
						var chooseOneGroupList = data.chooseOneGroupList;
						for (var m = 0; m < chooseOneGroupList.length; m++) {
							html.push("<fieldset id='" + chooseOneGroupList[m] + "'>");
							html.push("<legend style='font-size:14px;'>" + chooseOneGroupList[m] + "</legend>");
							var groupInfo = chooseOneArgMap[chooseOneGroupList[m]];
							for (var n = 0; n < groupInfo.length; n++) {
								sandbox.createRowHtml(html, groupInfo[n], "1");
							}
							html.push("</fieldset>");
							html.push("<br/>");
						}

						var choosableArgList = data.choosableArgList;
						for (var j = 0; j < choosableArgList.length; j++) {
							sandbox.createRowHtml(html, choosableArgList[j], "2");
						}
						$("#dynamicArea").append(html.join(" "));
					}
				},
				error : function(data, e) {
					bootbox.alert("系统错误,请稍候再试！");
				}
			});
		},
		createRowHtml : function(html, source, sourceType) {
			html.push("<div class='row'>");
			html.push("	<div class='col-md-12'>");
			html.push("	 <div class='form-group'>");
			var temp = "";
			var tempId = "";
			if (sourceType == '0') {
				temp = " <font color='red'>*</font>";
				tempId = "red";
			} else if (sourceType == '1') {
				temp = " <font color='blue'>*</font>";
				tempId = "blue";
			}
			var fieldName = source.fieldName;
			html.push("    <label class='control-label col-md-5'>" + fieldName + temp + "</label>");
			html.push("    <div class='col-md-7'>");
			html.push("		   <input type='text' vv='field_" + tempId + "_" + fieldName + "' class='form-control' name='" + fieldName + "'></input>");
			html.push("    </div>");
			html.push("  </div>");
			html.push(" </div>");
			html.push("</div>");
		},
		createDynamicArgArea : function(apiId,apiVersion){
			sandbox.reqMethodArea(apiId, apiVersion);
			var reqMethod = '';
			if($("input[name='reqType']:checked").val() == 'syn'){
				reqMethod = '0';
			}else{
				reqMethod = '1';
			}
			sandbox.dynamicArgArea(apiId, apiVersion,reqMethod);
		},
		validateArg : function() {
			var argsForm = $('#sandboxForm');
			// appKey appSecret 校验待定
			var app_key = $("#app_key").val();
			if ($.trim(app_key) == "") {
				bootbox.alert("appKey不能为空!");
				return false;
			}
			var app_secret = $("#app_secret").val();
			if ($.trim(app_secret) == "") {
				bootbox.alert("appSecret不能为空!");
				return false;
			}

			// 校验为空
			var api_sort = $("#api_sort").val();
			if ($.trim(api_sort) == "-1") {
				bootbox.alert("请选择API类目!");
				return false;
			}
			var api_name = $("#api_name").val();
			if ($.trim(api_name) == "-1") {
				bootbox.alert("请选择API名称!");
				return false;
			}
			var api_version = $("#api_version").val();
			if ($.trim(api_version) == "-1") {
				bootbox.alert("请选择API版本!");
				return false;
			}
			var fields = $("#fields").val();
			if ($.trim(fields) == "") {
				bootbox.alert("fields不能为空!");
				return false;
			}
			var requrieFlag = true;
			// 必选参数校验
			$("input[vv^='field_red']").each(function() {
				var argVal = $(this).val();
				if ($.trim(argVal) == "") {
					requrieFlag = false;
					bootbox.alert(this.name + "不能为空!");
					return false;//实现break功能
				}
			});
			if(!requrieFlag){
				return false;
			}
			
			var requrieOneFlag = true;
			// 必选其一参数校验
			$("fieldset").each(function(i) {
				var valArry = [];
				$(this).find("input[vv^='field_blue']").each(function() {
					var argVal = $(this).val();
					if ($.trim(argVal) != "") {
						valArry.push($.trim(argVal));
					}
				});
				if (valArry.length == 0) {
					requrieOneFlag = false;
					bootbox.alert(this.id + "里的参数中必填一个!");
					return false;
				}
			});
			if(!requrieOneFlag){
				return false;
			}
			
			if($("input[name='reqType']:checked").val() == 'syn'){
				var re = /^[1-9]+[0-9]*]*$/;
				var page_num = $("#pageNum").val();
				var page_size = $("#pageSize").val();
				if($.trim(page_num) != ""){
					if (!re.test($.trim(page_num))){
						bootbox.alert("pageNum只能为正整数值!");
						return false;
					}  
					
					if($.trim(page_size) == ""){
						bootbox.alert("pageSize不能为空!");
						return false;
					}
				}
				
				if($.trim(page_size) != ""){
					if (!re.test($.trim(page_size))){
						bootbox.alert("pageSize只能为正整数值!");
						return false;
					} 
					
					if($.trim(page_num) == ""){
						bootbox.alert("pageNum不能为空!");
						return false;
					}
				}
			}
		
			return true;

		},
		commitForm : function() {
			if (sandbox.validateArg()) {
				$.ajax({
					cache : false,
					url : $.basePath + "/apitools/getApiData.json",
					type : "post",
					data : $("#sandboxForm").serialize() + "&method=" + $("#api_name option:selected").text()+"&sourceFlag="+source_flag,
					dataType : "json",
					beforeSend : function(XMLHttpRequest) {
						$("#req_url").val("");
						$("#rep_val").val("");
						App.blockUI({
							boxed : true,
							message : '数据加载中,请稍候...'
						});
					},
					success : function(data) {
						if (!showError(data)) {
							App.unblockUI();
							$("#req_url").val(data.reqUrl);
							//针对下载特殊处理
							var downFlag = $("input[name='downLoadFLag']").eq(0).val();
							var formatType = $("select[name='format']").eq(0).val();
							if(downFlag == 'Y'){
								var retCode;
								if(formatType == 'json'){
									try {
										var jsonObj = $.parseJSON(data.result);
										retCode = jsonObj.retCode;
									} catch (e) {
										retCode = 'Down';
									}
								}else if(formatType == 'xml'){
									try {
										var xmlObj = $.parseXML(data.result);
										retCode = $(xmlObj).find('retCode').text();
									} catch (e) {
										retCode = 'Down';
									}
								}
								if(retCode == 'Down'){
									window.location.href = data.reqUrl;
								}else{
									$("#rep_val").val(data.result);
								}
							}else{
								$("#rep_val").val(data.result);
							}
							
							if(source_flag == '2'){
								var testCount = $("#test_count").val();
								if(data.result.indexOf("errorToken") == -1){
									var testCountInt = parseInt(testCount)+1;
									$("#test_count").val(testCountInt);
									$("#test_count_btn").val("测试成功"+testCountInt+"次");
								}
								var totalCount = $("#test_count").val();
								if(parseInt(totalCount) == 3){
									$("#commit_btn").hide();
									$("#publish_btn").show();
								}
							}
						}
					},
					error : function(data, e) {
						App.unblockUI();
						bootbox.alert("系统错误,请稍候再试！");
					}
				});
			}
		},
		validSourceArg : function() {
			if (source_flag == '2') {
				if (sort_id == null || sort_id == '') {
					bootbox.alert("传入参数API类目不能为空");
					return false;
				}

				if (api_name == null || api_name == '') {
					bootbox.alert("传入参数API名称不能为空");
					return false;
				}
				if (api_version == null || api_version == '') {
					bootbox.alert("传入参数API版本不能为空");
					return false;
				}
			}
			return true;
		}
	}
})();
