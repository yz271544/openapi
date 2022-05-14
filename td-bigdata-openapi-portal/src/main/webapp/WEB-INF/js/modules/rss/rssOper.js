var rssOper = (function() {
	var resargTable = null;
	var initResTable = function(){
		resargTable = $("#resarg_table").dataTable( {
			"bPaginate" : false,
			"bServerSide" : false,
			"bFilter" : false,
			"bInfo" : false,
			"bSort" : false,
			"bAutoWidth" : false,
			"bJQueryUI" : true,
			"aoColumns":[
                 {'sTitle':'字段名称',"mData":"fieldName","sWidth": "30%",'sClass':'center'}
                ,{'sTitle':'字段描述',"mData":"fieldTitle","sWidth": "25%",'sClass':'center'}
                ,{'sTitle':'字段类型',"mData":"fieldTargtType","sWidth": "15%",'sClass':'center'}
               
            ],
            "columnDefs": [
                   {   "aTargets":[3],
                	   "sTitle" : '全选<input type="checkbox" class="group-checkable" checked="checked" data-set="#resarg_table .checkboxes" />',
                	   "sWidth": "8%",
                	   "mRender":function(data,type,full){
                		   return '<input type="checkbox" class="checkboxes" checked="checked" value="1"/>'
                	    }
                   },
                   {   "aTargets":[4],
                	   "sTitle" : '操作',
                	   "sWidth": "22%",
                	   "mRender":function(data, type, full) {  
                		   var html = ' <a href="javascript:void(0);" class="top btn btn-default btn-xs" title="置顶"><i class="fa fa-angle-double-up"></i></a>'  
                		   html += ' <a href="javascript:void(0);" class="up btn btn-default btn-xs" title="上移"><i class="fa fa-arrow-up"></i></a>'  
                           html += ' <a href="javascript:void(0);" class="down btn btn-default btn-xs" title="下移"><i class="fa fa-arrow-down"></i></a>'  
                           html += ' <a href="javascript:void(0);" class="bottom btn btn-default btn-xs" title="置末"><i class="fa fa-angle-double-down"></i></a>'  
                           return html;  
                       }  
                   }]
		}); 
		
		resargTable.find('.group-checkable').change(function () {
            var set = jQuery(this).attr("data-set");
            var checked = jQuery(this).is(":checked");
            jQuery(set).each(function () {
                if (checked) {
                    $(this).prop("checked", true);
                    $(this).parents('tr').addClass("active");
                } else {
                    $(this).prop("checked", false);
                    $(this).parents('tr').removeClass("active");
                }
            });
            jQuery.uniform.update(set);
        });

		resargTable.on('change', 'tbody tr .checkboxes', function () {
			 $(this).parents('tr').toggleClass("active");
        });
		
		// 初始化置顶按钮  
	    $('#resarg_table tbody').on('click', 'a.top', function(e) {  
	        e.preventDefault();  
	        var table = $('#resarg_table').DataTable(); 
	        var select_row = table.row($(this).parents('tr')).data();  
	        var all_row = table.rows().data(); 
	        var new_data = [];  
	        new_data.push(select_row);  
	        for (var i = 0; i < all_row.length; i++) {  
	            if (select_row.fieldName == all_row[i].fieldName) {  
	                continue;  
	            }  
	            new_data.push(all_row[i]);  
	        } 
	        table.clear(); 
	        table.rows.add(new_data).draw();
            table.rows().nodes().to$().addClass('active'); 
	    });  
		// 初始化上升按钮  
	    $('#resarg_table tbody').on('click', 'a.up', function(e) {  
	        e.preventDefault();  
	        var table = $('#resarg_table').DataTable(); 
	        var index = table.row($(this).parents('tr')).index();  
	        if ((index - 1) >= 0) {  
	            var data = table.data();  
	            table.clear();  
	            data.splice((index - 1), 0, data.splice(index, 1)[0]);  
	            table.rows.add(data).draw();
	            table.rows().nodes().to$().addClass('active'); 
	        } 
	    });  
	  
	    // 初始化下降按钮  
	    $('#resarg_table tbody').on('click', 'a.down', function(e) {  
	        e.preventDefault();  
	        var table = $('#resarg_table').DataTable();  
	        var index = table.row($(this).parents('tr')).index();  
	        var max = table.rows().data().length;  
	        if ((index + 1) < max) {  
	            var data = table.data();  
	            table.clear();  
	            data.splice((index + 1), 0, data.splice(index, 1)[0]);  
	            table.rows.add(data).draw();  
	            table.rows().nodes().to$().addClass('active'); 
	        } 
	    }); 
	    
	    // 初始化置末按钮  
	    $('#resarg_table tbody').on('click', 'a.bottom', function(e) {  
	        e.preventDefault();  
	        var table = $('#resarg_table').DataTable(); 
	        var select_row = table.row($(this).parents('tr')).data();  
	        var all_row = table.rows().data(); 
	        var new_data = [];  
	        for (var i = 0; i < all_row.length; i++) {  
	            if (select_row.fieldName == all_row[i].fieldName) {  
	                continue;  
	            }  
	            new_data.push(all_row[i]);  
	        } 
	        new_data.push(select_row);  
	        table.clear(); 
	        table.rows.add(new_data).draw();
            table.rows().nodes().to$().addClass('active'); 
	    });  
	};
	
	var validFtpInfo = function(){
		var ftpForm = $('#ftpInfoForm');
        ftpForm.validate({
            errorElement: 'span',
            errorClass: 'help-block help-block-error', 
            focusInvalid: false, 
            ignore: "",  
            rules: {
            	ftpHost: {
                    required: true
                },
                ftpPort: {
                    required: true,
                    number: true
                },
                userName: {
                    required: true
                },
                password: {
                    required: true
                },
                ftpPath: {
                    required: true
                }
            },

            highlight: function (element) { 
                $(element).closest('.form-group').addClass('has-error');
            },

            unhighlight: function (element) { 
                $(element).closest('.form-group').removeClass('has-error'); 
            },

            success: function (label) {
            	label.closest('.form-group').removeClass('has-error'); 
            }
        });
	};
	
	var validRssInfo = function(){
		var rssForm = $('#rssInfoForm');
		rssForm.validate({
            errorElement: 'span',
            errorClass: 'help-block help-block-error', 
            focusInvalid: false, 
            ignore: "",  
            rules: {
            	apiSort: {
                    required: true
                },
                apiId: {
                    required: true
                },
                apiVersion: {
                    required: true
                },
                respnArgRender: {
                    required: true
                },
                reqArgRender: {
                    required: true
                }
            },

            highlight: function (element) { 
                $(element).closest('.form-group').addClass('has-error');
            },

            unhighlight: function (element) { 
                $(element).closest('.form-group').removeClass('has-error'); 
            },

            success: function (label) {
            	label.closest('.form-group').removeClass('has-error'); 
            }
        });
	};
	
	return {
		init : function() {
			var url = $.basePath + "/apitools/getSelectJson.json";
			var url2 = $.basePath + "/apitools/getSelectApiName.json";

			// 初始化api类目
			rssOper.bindSelect("api_sort_detail", url + "?selectName=apiSort", sort_id);
			
			if(method != 'add'){
				//初始化api名称
				//rssOper.bindSelect("api_name_detail", url + "?selectName=apiName&reqMethod=2&apiSort=" + sort_id, api_id);
				rssOper.triggerSelect2("api_name_detail", url2 + "?selectName=apiName&reqMethod=2&apiSort=" + sort_id, api_id);
				// 初始化api版本
				rssOper.bindSelect("api_version_detail", url + "?selectName=apiVersion&apiSort=" + sort_id + "&apiId=" + api_id, api_version);
				// 初始化周期、触发方式 
				rssOper.bindSelect("api_other", url + "?selectName=apiOther&apiId=" + api_id+"&apiVersion="+api_version);
				
				var ftpType = $("#ftp_type").val();
				if(ftpType == "ftp"){
					$("#ftpProtocol_div").show();
					$("#ftpMode_div").show();
				}else{
					$("#ftpProtocol_div").hide();
					$("#ftpMode_div").hide();
				}
				
				var dataFileArry = dataFileName.split("$");
				var dataPrefix = dataFileArry[0];
				var dataSuffix = dataFileArry[2].split(".")[1];
				
				var chkFileArry = checkFileName.split("$");
				var chkPrefix = chkFileArry[0];
				var chkSuffix = chkFileArry[1].split(".")[1];
				
				$("#data_prefix").val(dataPrefix);
				$("#data_suffix").val(dataSuffix);
				$("#datapre_date_switch").bootstrapSwitch('state',eval(dataFileArry[1])); 
				$("#datapre_num_switch").bootstrapSwitch('state', eval(dataFileArry[2].split(".")[0])); 
				$("#datasuf_date_switch").bootstrapSwitch('state',eval(dataFileArry[3])); 
				$("#datasuf_num_switch").bootstrapSwitch('state', eval(dataFileArry[4])); 
				
				$("#chk_prefix").val(chkPrefix);
				$("#chk_suffix").val(chkSuffix);
				$("#chkpre_date_switch").bootstrapSwitch('state', eval(chkFileArry[1])); 
				$("#chkpre_num_switch").bootstrapSwitch('state', eval(chkFileArry[2].split(".")[0])); 
				$("#chksuf_date_switch").bootstrapSwitch('state', eval(chkFileArry[3])); 
				$("#chksuf_num_switch").bootstrapSwitch('state', eval(chkFileArry[4])); 

				if(method == 'detail'){
					//详情 全部置灰
					var rssInfoForm = $("#rssInfoForm").serializeArray();
					$.each(rssInfoForm,function(i,v){
						$("#rssInfoForm").find("[name='"+v.name+"']").attr("disabled",true);
					});
					$("#rss_save_btn").attr("disabled",true);
					$("#rss_save_btn").hide();
					
					var ftpInfoForm = $("#ftpInfoForm").serializeArray();
					$.each(ftpInfoForm,function(i,v){
						$("#ftpInfoForm").find("[name='"+v.name+"']").attr("disabled",true);
					});
					$("#testBtn").attr("disabled",true);
					$("#testBtn").hide();
					
					$("#rep_detail_btn").attr("disabled",true);
					$("#rep_detail_btn").hide();
					
					$("#req_detail_btn").attr("disabled",true);
					$("#req_detail_btn").hide();
				}
			}else{
				$("#app_key").val(app_key);
			}
			
			if (method != 'detail') {
				$("#api_sort_detail").on("change", function(e) {
					var apiSort = $("#api_sort_detail").val();
					rssOper.clearSelect("api_version_detail");
					if (apiSort != "") {
						//rssOper.bindSelect("api_name_detail", url + "?selectName=apiName&reqMethod=2&apiSort=" + apiSort);
                        rssOper.triggerSelect2("api_name_detail", url2 + "?selectName=apiName&reqMethod=2&apiSort=" + apiSort);
					}
				});

				$("#api_name_detail").on("change", function(e) {
					var apiSort = $("#api_sort_detail").val();
					var apiId = $("#api_name_detail").val();
					rssOper.clearSelect("api_version_detail");
					if (apiId) {
						rssOper.bindSelect("api_version_detail", url + "?selectName=apiVersion&apiSort=" + apiSort + "&apiId=" + apiId);
					}
				});

				$("#api_version_detail").on("change", function(e) {
					var apiId = $("#api_name_detail").val();
					var apiVersion = $("#api_version_detail").val();
					rssOper.clearSelect("api_other");
					rssOper.bindSelect("api_other", url + "?selectName=apiOther&apiId=" + apiId+"&apiVersion="+apiVersion);
					rssOper.dynamicArgArea(apiId, apiVersion)
				});
			}
			
			//初始绑定事件
			$("#ftp_type").on("change",function(e){
				var ftpType = $(this).val();
				if(ftpType == "ftp"){
					$("#ftpProtocol_div").show();
					$("#ftpMode_div").show();
				}else{
					$("#ftpProtocol_div").hide();
					$("#ftpMode_div").hide();
				}
			});
			
			validRssInfo();
			initResTable();
			validFtpInfo();
		},
		bindSelect : function(ctrlName, url, checkedValue) {
			var control = $('#' + ctrlName);
			var async_flag = true;
			if (ctrlName != 'api_sort_detail') {
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
						control.append("<option value=''>--请选择--</option>");
						$.each(data, function(i, item) {
							if (checkedValue != null && checkedValue != '') {
								if (checkedValue == item.Value) {
									control.append("<option value='" + item.Value + "' selected='selected'>" + item.Text + "</option>");
								} else {
									control.append("<option value='" + item.Value + "'>" + item.Text + "</option>");
								}
							} else {
								if(ctrlName == 'api_other'){
									if(i==0){
										$("#dataCycleType").append("<option value='" + item.Value + "'>" + item.Text + "</option>");
									}else{
										$("#triggerMethod").append("<option value='" + item.Value + "'>" + item.Text + "</option>");
										if(item.Value == '2'){
											$("#trigger_div").show();
										}else{
											$("#trigger_div").hide();
										}
									}
								}else{
									control.append("<option value='" + item.Value + "'>" + item.Text + "</option>");
								}
							}

						});
					}
				},
				error : function(data, e) {
					bootbox.alert("系统错误,请稍候再试！");
				}
			});
			if (ctrlName == 'api_version_detail' && control.val() != '') {
				rssOper.dynamicArgArea($("#api_name_detail").val(), control.val());
			}
		},
		triggerSelect2 : function (ctrlName, url,checkedValue) {
			var control = $('#' + ctrlName);
            if (checkedValue != null && checkedValue != '') {
            	control.empty();// 清空下拉框
            	control.append("<option value='"+checkedValue+"'>"+api_name+"</option>");
            }
            control.select2({
	            ajax: {
	                url: url,
	                // type:'POST',
	                dataType: 'json',
	                delay: 250,
	                data: function (params) {
	                	return {
	                        term: params.term, // search term
	                        page: params.page ? params.page : 1
	                    };
	                },
	                processResults: function (data, params) {
	                    params.page = params.page || 1;
	                    var itemList = [];//当数据对象不是{id:0,text:'ANTS'}这种形式的时候，可以使用类似此方法创建新的数组对象
	                    var items = data.items;
	                    //console.log(items);
	                    $.each(items, function(i, item){
	                        itemList.push({id: item['Value'], text: item['Text']});
	                    });
	                    return {
	                        results: itemList,
	                        pagination: {
	                            more: (params.page * 30) < data.totalCount
	                        }
	                    };
	                },
	                cache: false
	            },
	            multiple: false,
	            placeholder: '请选择',
	            escapeMarkup: function (markup) { return markup; }, // let our custom formatter work
	            minimumInputLength: 1,
	            allowClear: true,
	            language: "zh-CN",
	            width: "100%"
	        });
        },
		clearSelect : function(ctrlName) {
			if(ctrlName != 'api_other'){
				var control = $('#' + ctrlName);
				control.empty();
				control.append("<option value=''>--请选择--</option>");
			}
			//清空数据
			$("#dataCycleType").empty();
			$("#triggerMethod").empty();
			$("#respnArg").html("");
			$("#reqArg").html("");
			// 清除请求参数动态区数据
			$("#reqDynamicArea").empty();
			//清除响应参数动态区数据
			resargTable.fnClearTable();
		},
		dynamicArgArea : function(api_id, api_version) {
			$.ajax({
				cache : false,
				url : $.basePath + "/apitools/getDynamicArgAreaJson.json",
				type : "post",
				data : {
					apiId : api_id,
					apiVersion : api_version,
					reqMethod : '3'
				},
				dataType : "json",
				beforeSend : function(XMLHttpRequest) {
					$("#reqDynamicArea").empty();
				},
				success : function(data) {
					if (!showError(data)) {
						//组装响应参数
						var responseArgList = data.responseArgList;
						rssOper.createTableHtml(responseArgList);
						// 组装请求动态区域参数数据
						var html = [];
						var requiredArgList = data.requiredArgList;
						for (var i = 0; i < requiredArgList.length; i++) {
							rssOper.createRowHtml(html, requiredArgList[i], "0");
						}
						var chooseOneArgMap = data.chooseOneArgMap;
						var chooseOneGroupList = data.chooseOneGroupList;
						for (var m = 0; m < chooseOneGroupList.length; m++) {
							html.push("<fieldset id='" + chooseOneGroupList[m] + "'>");
							html.push("<legend style='font-size:14px;'>" + chooseOneGroupList[m] + "</legend>");
							var groupInfo = chooseOneArgMap[chooseOneGroupList[m]];
							for (var n = 0; n < groupInfo.length; n++) {
								rssOper.createRowHtml(html, groupInfo[n], "1");
							}
							html.push("</fieldset>");
							html.push("<br/>");
						}

						var choosableArgList = data.choosableArgList;
						for (var j = 0; j < choosableArgList.length; j++) {
							rssOper.createRowHtml(html, choosableArgList[j], "2");
						}
						$("#reqDynamicArea").append(html.join(" "));
						
						$("[data-toggle='popover']").popover();
						
						if(method != 'add'){
							//请求参数配置区填充
							var reqArgVal = $("#reqArg").html();
							var reqArgArry = reqArgVal.split(";");
							for(var m=0;m < reqArgArry.length;m++){
								var temp = reqArgArry[m].split("=");
								$("#reqArgForm").find("input[name='"+temp[0]+"']").eq(0).val(temp[1]);
								if(method == 'detail'){
									$("#reqArgForm").find("input[name='"+temp[0]+"']").eq(0).attr("disabled",true);
								}
							}
						}
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
			html.push("    <label class='control-label col-md-5' data-trigger='hover' data-toggle='popover' data-content='"+source.fieldFileDesc+"'>" + source.fieldName + temp + "</label>");
			html.push("    <div class='col-md-7'>");
			if(fieldName.toLowerCase() == 'deal_date' || fieldName.toLowerCase() == 'dealdate'  ){
				html.push("<input type='text' vv='field_" + tempId + "_" + fieldName + "' class='form-control' name='" + fieldName + "' readonly value='周期字段，无需填写'></input>");
			}else{
				html.push("<input type='text' vv='field_" + tempId + "_" + fieldName + "' class='form-control' name='" + fieldName + "'></input>");
			}
			html.push("    </div>");
			html.push("  </div>");
			html.push(" </div>");
			html.push("</div>");
		},
		createTableHtml :function(data){
			resargTable.fnClearTable();
			resargTable.fnAddData(data);
			var repArgVal = $("#respnArg").html();
			var repArgArray = repArgVal.split(",");
			$("#resarg_table .checkboxes").each(function(i){
				if(method != 'add'){
					if(repArgVal != ''){
						//响应参数配置区填充
						var fieldName = $(this).parents("tr").children("td:eq(0)").html();
						if($.inArray(fieldName,repArgArray)!= -1){
							$(this).parents('tr').addClass("active");
						}else{
							$(this).parents('tr').removeClass("active");
							if($(this).is(":checked")){
								$(this).prop("checked", false);
							}
						}
					}else{
						$(this).parents('tr').addClass("active");
					}
				}else{
					$(this).parents('tr').addClass("active");
				} 
			});
		},
		createResponseHtml:function(){
			var table = $("#resarg_table").DataTable();
			var rows = table.rows('.active').data();
			var fields ="";
			for(var i=0; i < rows.length;i++){
				if(rows[i].fieldName){
					if(i == rows.length-1){
						fields += rows[i].fieldName;
					}else{
						fields += rows[i].fieldName+",";
					}
				}
			}
			$("#rep_detail_div").modal("hide");
			$("#respnArg").html(fields);
		},
		createRequestHtml:function(){
			var reqArg = $("#reqArgForm").serializeArray();
			var reqHtml ="";
			$.each(reqArg,function(i,v){
				if(v.name.toLowerCase() == 'deal_date' || v.name.toLowerCase() == 'dealdate'){
					v.value = "any";
				}
				if(v.value !=''){
					reqHtml += v.name +"="+v.value+";"
				}
			});
			$("#reqArg").html(reqHtml);
			$("#req_detail_div").modal("hide");
		},
		testIsConnect : function(){
			//先校验数据
			$.ajax({
				cache : false,
				url : $.basePath +"/admin/rss/testIsConnection.json",
				type : "post",
				data : $("#ftpInfoForm").serialize(),
				dataType : "json",
				beforeSend : function(XMLHttpRequest) {
					$("#recall_detail_div").modalmanager('loading');
				},
				success : function(data) {
					if (!showError(data)) {
						$("#recall_detail_div").modalmanager('removeLoading');
						if(data.isCon){
							 bootbox.dialog({
			                    message: "<h3>测试通过!</h3>",
			                    title: "FTP连通性测试",
			                    buttons: {
			                      success: {
			                        label: "确定",
			                        className: "green",
			                        callback: function() {
			                        	$("#ftp_isconnect").val("true");
			                        }
			                      }
			                    }
			                 });
						}else{
							 bootbox.dialog({
			                    message: "<h2>测试不通过!</h2>",
			                    title: "FTP连通性测试",
			                    buttons: {
			                      success: {
			                        label: "确定",
			                        className: "green",
			                        callback: function() {
			                        	$("#ftp_isconnect").val("false");
			                        }
			                      }
			                    }
			                 });
						}

					}
				},
				error : function(data, e) {
					$("#recall_detail_div").modalmanager('removeLoading');
					bootbox.alert("系统错误,请稍候再试！");
				}
			});
		},
		rssSaveEvent : function(){
			if(true){
				$.ajax({
					cache : false,
					url : $.basePath +"/admin/rss/saveRssInfo.json",
					type : "post",
					data : $("#rssInfoForm").serialize()+"&"+$("#ftpInfoForm").serialize()+"&method="+method,
					dataType : "json",
					beforeSend : function(XMLHttpRequest) {
						App.blockUI({
							boxed : true,
							message : '数据保存中,请稍候...'
						});
					},
					success : function(data) {
						if (!showError(data)) {
							App.unblockUI();
							if(data.retCode == '00'){
								bootbox.alert("保存成功！",function(){
									$("#rss_detail_div").modal("hide");
									 rss.queryForm();
								});
							}else{
								bootbox.alert("保存失败！");
							}
						}
					},
					error : function(data, e) {
						App.unblockUI();
						bootbox.alert("系统错误,请稍候再试！");
					}
				});
			}
		}
	}
})();

