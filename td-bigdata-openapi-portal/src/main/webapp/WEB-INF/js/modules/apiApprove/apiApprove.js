var ApiRelease = function(){
	var handleSelect2 = function(formId) {
		$('#'+formId+' select.select2').select2({
			width: null,
            language: 'zh-CN',
            minimumResultsForSearch: -1,
            dropdownCss:{
            	'z-index': 19999
            }
        });
	}
	var handleClearSelect2 = function(formId){
		/*替换$('.select2').select2('val', '');这样就不会触发select的change事件（这样导致validate表单信息）*/
		$('#'+formId+' select.select2').each(function(i, obj){
    		var select_id = $(this).attr('id');
    		var select2_id = 'select2-'+select_id+'-container';
    		$('#'+select2_id).text('--请选择--');
    		$('#'+select2_id).attr('title', '--请选择--');
    	});
	}
	var handleToolTip = function() {
		$('.toolTip').tooltip();
	}
	var handleValidation = function() {
            var form1 = $('#form_apiItem');
            form1.validate({
                errorElement: 'span', //default input error message container
                errorClass: 'help-block help-block-error', // default input error message class
                focusInvalid: false, // do not focus the last invalid input
                ignore: '',  // validate all fields including form hidden input
                messages: {
                    select_multi: {
                        maxlength: jQuery.validator.format("Max {0} items allowed for selection"),
                        minlength: jQuery.validator.format("At least {0} items must be selected")
                    }
                },
                rules: {
                    apiName: {
                        minlength: 1,
                        required: true
                    },
                    apiClsCode: {
                        required: true
                    },
                    apiSort: {
                        required: true
                    },
                    tabScaleType: {
                        required: true
                    },
                    dataCycleType: {
                        required: true
                    },
                    triggerMethd: {
                    	required: true
                    },
                    apiStatCode:  {
                    	required: true
                    }
                },
                messages: {
                	apiName:'API名称不能为空',
                	apiClsCode:'API大类为必选项',
                	apiSort:'API类型为必选项',
                	tabScaleType:'数据规模类型为必选项',
                	dataCycleType:'周期类型为必选项',
                	apiStatCode:'有效状态为必选项',
                	triggerMethd:'API触发类型为必选项'
                },
                invalidHandler: function (event, validator) { //display error alert on form submit              
                },
                highlight: function (element) { // hightlight error inputs
                    $(element)
                        .closest('.form-group').addClass('has-error'); // set error class to the control group
                },
                unhighlight: function (element) { // revert the change done by hightlight
                    $(element)
                        .closest('.form-group').removeClass('has-error'); // set error class to the control group
                },
                success: function (label) {
                    label.closest('.form-group').removeClass('has-error'); // set success class to the control group
                },
                submitHandler: function (form) {
                	if($('#btn_insertApiItem').is(':visible')){
                			handleSave();
                	}else if($('#btn_updateApiItem').is(':visible')){
                			handleUpdate();
                    }
                }
            });
            
            var form2 = $('#form_apiApprove');
            form2.validate({
                errorElement: 'span', //default input error message container
                errorClass: 'help-block help-block-error', // default input error message class
                focusInvalid: false, // do not focus the last invalid input
                ignore: '',  // validate all fields including form hidden input
                messages: {
                    select_multi: {
                        maxlength: jQuery.validator.format("Max {0} items allowed for selection"),
                        minlength: jQuery.validator.format("At least {0} items must be selected")
                    }
                },
                rules: {
                    auditAdvc: {
                        minlength: 1,
                        required: true
                    }
                },
                messages: {
                	auditAdvc:'审批意见不能为空'
                },
                invalidHandler: function (event, validator) { //display error alert on form submit              
                },
                highlight: function (element) { // hightlight error inputs
                    $(element)
                        .closest('.form-group').addClass('has-error'); // set error class to the control group
                },
                unhighlight: function (element) { // revert the change done by hightlight
                    $(element)
                        .closest('.form-group').removeClass('has-error'); // set error class to the control group
                },
                success: function (label) {
                    label.closest('.form-group').removeClass('has-error'); // set success class to the control group
                },
                submitHandler: function (form) {
                	alert();
                }
            });
            $('.select2').change(function(){  
                $(this).valid();  
            });  
    }
	
	var handleDatePickers = function(){
		$('.date-picker').datepicker({
			rtl : App.isRTL(),
			orientation : "left",
			autoclose : true,
			format : "yyyy-mm-dd",
			language : "zh-CN"
		});
	}
	
	//获取form表单参数
	var getFormJson = function(form_id) {
		var o = {};
		$sel_disabled = $('#'+form_id + ' select[disabled = "disabled"]');
		$sel_disabled.removeAttr("disabled","disabled"); 
		var a = $('#'+form_id).serializeArray();
		$sel_disabled.attr("disabled","disabled"); 
		$.each(a, function () {
			if (o[this.name] !== undefined) {
			if (!o[this.name].push) {
				o[this.name] = [o[this.name]];
			}
				o[this.name].push(this.value || '');
			} else {
				o[this.name] = this.value || '';
			}
		});
		return o;
	}
	
	/*
	 * 获取唯一id
	 */
	var generateId = function() {
		var url = $.basePath + '/apiRelease/generateId.json'
		var id;
		$.ajax({
			url : url,
			type : "post",
			//data : param,
			async : false,
			dataType : "json",
			success : function(data) {
				if (!showError(data)) {
					id = data.id;
				}
			},
			error : function(data, e) {
				bootbox.alert("系统错误,请稍候再试！");
			}
		});
		return id;
	}
	
	var pageIndex = 0; //页面索引初始值   
	var pageSize = 5; //每页显示条数初始化，修改显示条数，修改这里即可
	var handlePagination = function(){
		var form_id = 'form_apiSearch';
		var pg_detail = 'pg_detail';
		var pg_total = 'pg_total';
		var tb_id = 'tb_apiInfo';
		var data_url = $.basePath + '/apiRelease/getApiInfo.json';
		var data_param = {method : 'getApiInfo'};
		
		initTable(0); //Load事件，初始化表格数据，页面索引为0（第一页）
		
		function pageInit(totalCount, pageIndex){
			//分页，totalCount是总条目数，这是必选参数，其它参数都是可选
			$('#'+pg_detail).pagination(totalCount, {
				callback : pageCallback, //PageCallback() 为翻页调用次函数。
				prev_text : "« 上一页",
				next_text : "下一页 »",
				items_per_page : pageSize,
				num_edge_entries : 1, //两侧首尾分页条目数
				num_display_entries : 6, //连续分页主体部分分页条目数
				current_page : pageIndex, //当前页索引
			});
			
			var startIndex = (pageIndex * pageSize + 1);
			var endIndex = (pageIndex+1) * pageSize > totalCount ? totalCount : (pageIndex+1) * pageSize;
			$('#'+pg_total).html("当前第&nbsp;"+startIndex+'-'+endIndex+'&nbsp;条&nbsp;&nbsp;共计&nbsp;'+totalCount+'&nbsp;条');
		}
		//翻页调用   
		function pageCallback(index, jq) {
			initTable(index);
		}
		//请求数据   
		function initTable(pageIndex) {
			App.blockUI({
                target: '#portlet_body_api_info',
                message: '加载中...'
            });
			var totalCount;//总条目数
			var itemNum = pageIndex*pageSize+1;//当前页开始记录行数
			$.ajax({   
				 url: data_url,      //提交到一般处理程序请求数据    
				 type: "POST",  
				 //分页参数：pageIndex(页面索引)，pageSize(显示条数) 
			     data: $.extend({}, data_param, getFormJson(form_id), {'pageIndex' : pageIndex, 'pageSize' : pageSize},{'examStat' : 1}),
				 dataType: "json",
			     success: function(data) {
			    	 totalCount = data.totalCount;
			    	 pageInit(totalCount, pageIndex);
			    	 $('#'+tb_id +' tr:gt(0)').remove();        //移除Id为Result的表格里的行，从第二行开始（这里根据页面布局不同页变）   
			    	 $.each(data.items, function(i, item) {
			    		 var examStat = item.examStat;
			    		 var apiStatCode = item.apiStatCode;
			    		 //0未发布1待审核2已发布（审批通过）3审批驳回
			    		 var hide_btn_updateApiItem = (examStat == '1' ? ' hide ' : '');
			    		 var hide_btn_releaseApiItem = (examStat == '1' || examStat == '2' || apiStatCode == '0' ? ' hide ' : '');
			    		 var td_prefix = '<td><div class="td-label">';
			    		 var td_suffix = '</div></td>';
			    		 var sb = new StringBuffer();
			        	 sb.append('<tr>');
			        	 sb.append(td_prefix+(itemNum++)+td_suffix);
			        	 sb.append(td_prefix+item.apiClsDesc+td_suffix);
			        	 sb.append(td_prefix+item.apiSortName+td_suffix);
			        	 sb.append(td_prefix+item.apiId+td_suffix);
			        	 sb.append(td_prefix+item.apiName+td_suffix);
			        	 sb.append(td_prefix+item.apiDesc+td_suffix);
			        	 sb.append(td_prefix+item.triggerMethdDesc+td_suffix);
			        	 sb.append(td_prefix+item.dataCycleDesc+td_suffix);
			        	 sb.append(td_prefix+item.apiVersion+td_suffix);
			        	 sb.append(td_prefix+item.relseTime+td_suffix);
			        	 sb.append(td_prefix+item.examStatDesc+td_suffix);
			        	 sb.append(td_prefix+item.relsePersn+td_suffix);
			        	 sb.append(td_prefix+item.apiStatDesc+td_suffix);
			        	 sb.append('<td');
				        	 sb.append(' apiId ="'+item.apiId+'"');
				        	 sb.append(' apiVersion ="'+item.apiVersion+'"');
				        	 sb.append(' apiName ="'+item.apiName+'"');
				        	 sb.append(' apiDesc ="'+item.apiDesc+'"');
				        	 sb.append(' apiClsCode ="'+item.apiClsCode+'"');
				        	 sb.append(' apiSort ="'+item.apiSort+'"');
				        	 sb.append(' tabScaleType ="'+item.tabScaleType+'"');
				        	 sb.append(' dataCycleType ="'+item.dataCycleType+'"');
				        	 sb.append(' apiStatCode ="'+item.apiStatCode+'"');
				        	 sb.append(' triggerMethd ="'+item.triggerMethd+'"');
				        	 sb.append(' examStat ="'+item.examStat+'"');
				        	 sb.append(' apiVisitMethd ="'+item.apiVisitMethd+'"');
			        	 sb.append('>');
			        	 sb.append(	 '<a href="javascript:;" class="btn green btn-sm btn-outline sbold uppercase btn_seeApiItem toolTip" data-toggle="modal" data-target="#modal_apiItem" action="readApiItem" title="查看">');
			        	 sb.append(		'<i class="fa fa-search"></i> </a>');
			        	 sb.append(	 '<a href="javascript:;" class="btn green btn-sm btn-outline sbold uppercase btn_apiApprove toolTip" data-toggle="modal" data-target="#modal_apiApprove" title="审批">');
			        	 sb.append(		'<i class="fa fa-check"></i> </a>');
			        	 sb.append('</td>');
			        	 sb.append('</tr>');
			        	 
			        	 $('#'+tb_id +' tbody').append(sb.toStr());
			        	 handleToolTip();
			         });

					 App.unblockUI('#portlet_body_api_info');
			     },
				 error : function(data, e) {
					 bootbox.alert("系统错误,请稍候再试！");
				 }  
			});  
		}
	}
	
	/*
	 * 获取选择框
	 * sel_id:选择框id
	 * sel_url:选择url
	 * sel_param:选择参数
	 * sel_field:要获取的字段
	 * sel_val:选择项
	 */
	var handleSelect = function($sel_obj, sel_url, sel_param, sel_field, sel_val){
		// 绑定Ajax的内容
		$.ajax({
			url : sel_url,
			type : "post",
			data : sel_param,
			dataType : "json",
			success : function(data) {
				if (!showError(data)) {
					$sel_obj.empty();// 清空下拉框
					$sel_obj.append("<option value=''>--请选择--</option>");
					$.each(data, function(i, item) {
						var opt_val = item[sel_field.valueField];
						var opt_text = item[sel_field.textField];
						if (sel_val) {
							if (opt_val == sel_val) {
								$sel_obj.append("<option value='" + opt_val + "' selected='selected'>&nbsp;" + opt_text + "</option>");
								//$sel_obj.select2("val", opt_val);
								$sel_obj.val(opt_val).trigger("change");
							} else {
								$sel_obj.append("<option value='" + opt_val + "'>&nbsp;" + opt_text + "</option>");
							}
						} else {
							$sel_obj.append("<option value='" + opt_val + "'>&nbsp;" + opt_text + "</option>");
						}
					});
				}
			},
			error : function(data, e) {
				bootbox.alert("系统错误,请稍候再试！");
			}
		});
	}
	
	/*
	 * apiItem模态框
	 */
	var handleModalApiItem = function($target){
		var action = $target.attr('action');
		var modal_apiItemTitle = 'modal_apiItemTitle';
		var form_apiItem = 'form_apiItem';
		switch(action){
			case 'addApiItem':
				var apiId = generateId();
				$('#'+modal_apiItemTitle).text('新增API');
				$('#txt_apiId_tmp').val(apiId);
				$('#txt_apiVersion_tmp').val(1);
				$('#txt_apiName_tmp').removeAttr("readonly").css('borderWidth','1px'); 
				$('#form_apiItem').find('input, select').removeAttr("disabled","disabled"); 
				$('#sel_apiStateCode_tmp').attr("disabled","disabled"); 
				$('#btn_insertApiItem').show();
				$('#btn_showApiArg').hide();
				$('#btn_configApiArg').hide();
				$('#btn_updateApiItem').hide();
				$('input[name="apiVisitMethd"]').selAllCheckbox();
				
				handleSelect($('#sel_apiClsCode_tmp'), $.basePath+'/apiRelease/getSelect.json', {method:'getApiCls'},{valueField:'codeId', textField:'codeDesc'}, '');
				handleSelect($('#sel_apiSort_tmp'), $.basePath+'/apiRelease/getSelect.json', {method:'getApiSort'},{valueField:'apiSort', textField:'apiSortName'}, '');
				handleSelect($('#sel_tabScaleType_tmp'), $.basePath+'/apiRelease/getSelect.json', {method:'getApiTabScale'},{valueField:'codeId', textField:'codeDesc'}, '');
				handleSelect($('#sel_dataCycleType_tmp'), $.basePath+'/apiRelease/getSelect.json', {method:'getApiDataCycle'},{valueField:'codeId', textField:'codeDesc'}, '');
				handleSelect($('#sel_apiStateCode_tmp'), $.basePath+'/apiRelease/getSelect.json', {method:'getApiStat'},{valueField:'codeId', textField:'codeDesc'}, '1');
				handleSelect($('#sel_triggerMethd_tmp'), $.basePath+'/apiRelease/getSelect.json', {method:'getTriggerMethd'},{valueField:'codeId', textField:'codeDesc'}, '');
				
	    		handleSelect2('form_apiItem');
	    		handleClearSelect2('form_apiItem');
	    		
				break;
			case 'updateApiItem':
				$('#'+modal_apiItemTitle).text('修改API');
				$('#txt_apiName_tmp').attr("readonly", true).css('borderWidth','0px');
				//$('#sel_apiStateCode_tmp').removeAttr("disabled","disabled"); 
				$('#form_apiItem').find('input, select').removeAttr("disabled","disabled");
				$('#btn_insertApiItem').hide();
				$('#btn_showApiArg').hide();
				$('#btn_configApiArg').show();
				$('#btn_updateApiItem').show();
				
				var apiId = $target.parent().attr('apiId');
				var apiVersion = $target.parent().attr('apiVersion');
				var apiName = $target.parent().attr('apiName');
				var apiDesc = $target.parent().attr('apiDesc');
				var apiClsCode = $target.parent().attr('apiClsCode');
				var apiSort = $target.parent().attr('apiSort');
				var tabScaleType = $target.parent().attr('tabScaleType');
				var dataCycleType = $target.parent().attr('dataCycleType');
				var apiStatCode = $target.parent().attr('apiStatCode');
				var triggerMethd = $target.parent().attr('triggerMethd');
				var apiVisitMethd = $target.parent().attr('apiVisitMethd');
				
				$('#txt_apiId_tmp').val(apiId);
				$('#txt_apiVersion_tmp').val(apiVersion);
				$('#txt_apiName_tmp').val(apiName);
				$('#txt_apiDesc_tmp').val(apiDesc);
				$('input[name="apiVisitMethd"]').selCheckbox(apiVisitMethd.split(','));
				
				handleSelect($('#sel_apiClsCode_tmp'), $.basePath+'/apiRelease/getSelect.json', {method:'getApiCls'},{valueField:'codeId', textField:'codeDesc'}, apiClsCode);
				handleSelect($('#sel_apiSort_tmp'), $.basePath+'/apiRelease/getSelect.json', {method:'getApiSort'},{valueField:'apiSort', textField:'apiSortName'}, apiSort);
				handleSelect($('#sel_tabScaleType_tmp'), $.basePath+'/apiRelease/getSelect.json', {method:'getApiTabScale'},{valueField:'codeId', textField:'codeDesc'}, tabScaleType);
				handleSelect($('#sel_dataCycleType_tmp'), $.basePath+'/apiRelease/getSelect.json', {method:'getApiDataCycle'},{valueField:'codeId', textField:'codeDesc'}, dataCycleType);
				handleSelect($('#sel_apiStateCode_tmp'), $.basePath+'/apiRelease/getSelect.json', {method:'getApiStat'},{valueField:'codeId', textField:'codeDesc'}, apiStatCode);
				handleSelect($('#sel_triggerMethd_tmp'), $.basePath+'/apiRelease/getSelect.json', {method:'getTriggerMethd'},{valueField:'codeId', textField:'codeDesc'}, triggerMethd);
				
	    		handleSelect2('form_apiItem');
				break;
			case 'readApiItem':
				$('#'+modal_apiItemTitle).text('查看API');
				$('#txt_apiName_tmp').attr("readonly", true).css('borderWidth','0px');
				//$('#form_apiItem input, #form_apiItem select').attr("disabled","disabled"); 
				$('#form_apiItem').find('input, select').attr("disabled","disabled");
				$('#btn_insertApiItem').hide();
				$('#btn_showApiArg').show();
				$('#btn_configApiArg').hide();
				$('#btn_updateApiItem').hide();
				
				var apiId = $target.parent().attr('apiId');
				var apiVersion = $target.parent().attr('apiVersion');
				var apiName = $target.parent().attr('apiName');
				var apiDesc = $target.parent().attr('apiDesc');
				var apiClsCode = $target.parent().attr('apiClsCode');
				var apiSort = $target.parent().attr('apiSort');
				var tabScaleType = $target.parent().attr('tabScaleType');
				var dataCycleType = $target.parent().attr('dataCycleType');
				var apiStatCode = $target.parent().attr('apiStatCode');
				var triggerMethd = $target.parent().attr('triggerMethd');
				var apiVisitMethd = $target.parent().attr('apiVisitMethd');
				
				$('#txt_apiId_tmp').val(apiId);
				$('#txt_apiVersion_tmp').val(apiVersion);
				$('#txt_apiName_tmp').val(apiName);
				$('#txt_apiDesc_tmp').val(apiDesc);
				$('input[name="apiVisitMethd"]').selCheckbox(apiVisitMethd.split(','));
				
				handleSelect($('#sel_apiClsCode_tmp'), $.basePath+'/apiRelease/getSelect.json', {method:'getApiCls'},{valueField:'codeId', textField:'codeDesc'}, apiClsCode);
				handleSelect($('#sel_apiSort_tmp'), $.basePath+'/apiRelease/getSelect.json', {method:'getApiSort'},{valueField:'apiSort', textField:'apiSortName'}, apiSort);
				handleSelect($('#sel_tabScaleType_tmp'), $.basePath+'/apiRelease/getSelect.json', {method:'getApiTabScale'},{valueField:'codeId', textField:'codeDesc'}, tabScaleType);
				handleSelect($('#sel_dataCycleType_tmp'), $.basePath+'/apiRelease/getSelect.json', {method:'getApiDataCycle'},{valueField:'codeId', textField:'codeDesc'}, dataCycleType);
				handleSelect($('#sel_apiStateCode_tmp'), $.basePath+'/apiRelease/getSelect.json', {method:'getApiStat'},{valueField:'codeId', textField:'codeDesc'}, apiStatCode);
				handleSelect($('#sel_triggerMethd_tmp'), $.basePath+'/apiRelease/getSelect.json', {method:'getTriggerMethd'},{valueField:'codeId', textField:'codeDesc'}, triggerMethd);
				
				handleSelect2('form_apiItem');
				break;
			default:;
		}
		
		//handleSelect2('form_apiItem');
	}
	
	/*
	 * 保存API信息
	 */
	var handleSave = function(){
		var modal_content = 'modal_content_apiItem';
		var form_id = 'form_apiItem';
		var form_url = $.basePath+'/apiRelease/saveApiItem.action';
		var form_param = getFormJson(form_id);
		var btn_cancel = 'btn_cancel';
		var btn_search = 'btn_search';
		var btn_reset = 'btn_reset';
		App.blockUI({
		    target: '#'+modal_content,
		    message: '执行中...'
		});
		// 绑定Ajax的内容
		$.ajax({
			url : form_url,
			type : "post",
			data : form_param,
			dataType : "json",
			success : function(data) {
				App.unblockUI('#'+modal_content);
				if (!showError(data)) {
					$('#btn_insertApiItem').hide();
					$('#btn_configApiArg').show();
					$('#btn_updateApiItem').show();
					$('#txt_apiName_tmp').attr("readonly", true).css('borderWidth','0px');
					//$('#'+btn_cancel).trigger('click');
					$('#'+btn_reset).trigger('click');
					$('#'+btn_search).trigger('click');
					bootbox.alert("操作成功");
				}
			},
			error : function(data, e) {
				App.unblockUI('#'+modal_content);
				bootbox.alert("系统错误,请稍候再试！");
			}
		});
	}
	
	/*
	 * 修改API信息
	 */
	var handleUpdate = function(){
		var modal_content = 'modal_content_apiItem';
		var url = $.basePath+'/apiRelease/modifyApiItem.action';
		var form_id = 'form_apiItem';
		var param = getFormJson(form_id);
		var btn_search = 'btn_search';
		App.blockUI({
            target: '#'+modal_content,
            message: '执行中...'
        });
		
		// 绑定Ajax的内容
		$.ajax({
			url : url,
			type : "post",
			data : param,
			dataType : "json",
			success : function(data) {
				App.unblockUI('#'+modal_content);
				if (!showError(data)) {
					$('#'+btn_search).trigger('click');
					bootbox.alert("操作成功");
				}
			},
			error : function(data, e) {
				App.unblockUI('#'+modal_content);
				bootbox.alert("系统错误,请稍候再试！");
			}
		});
	}
	
	/*
	 * 删除API信息
	 */
	var handleDelete = function($target){
		var portlet_body = 'portlet_body_api_info';
		var url = $.basePath+'/apiRelease/delApi.action';
		var param = {
			apiId : $target.parent().attr('apiId'),
			apiVersion : $target.parent().attr('apiVersion')
		}
		var btn_search = 'btn_search';
		
		App.blockUI({
            target: '#'+portlet_body,
            message: '执行中...'
        });
		// 绑定Ajax的内容
		$.ajax({
			url : url,
			type : "post",
			data : param,
			dataType : "json",
			success : function(data) {
				App.unblockUI('#'+portlet_body);
				if (!showError(data)) {
					$('#'+btn_search).trigger('click');
					bootbox.alert("操作成功");
				}
			},
			error : function(data, e) {
				App.unblockUI('#'+portlet_body);
				bootbox.alert("系统错误,请稍候再试！");
			}
		});
	}
	
	/*
	 * 发布API信息
	 */
	/*var handleRelease = function($target){
		var portlet_body = 'portlet_body_api_info';
		var url = $.basePath+'/apiRelease/releaseApiItem.action';
		var param = {
			apiId : $target.parent().attr('apiId'),
			apiVersion : $target.parent().attr('apiVersion'),
			examStat : 1
		}
		var btn_search = 'btn_search';
		
		App.blockUI({
            target: '#'+portlet_body,
            message: '执行中...'
        });
		// 绑定Ajax的内容
		$.ajax({
			url : url,
			type : "post",
			data : param,
			dataType : "json",
			success : function(data) {
				App.unblockUI('#'+portlet_body);
				if (!showError(data)) {
					$('#'+btn_search).trigger('click');
					bootbox.alert("操作成功");
				}
			},
			error : function(data, e) {
				App.unblockUI('#'+portlet_body);
				bootbox.alert("系统错误,请稍候再试！");
			}
		});
	}*/
	
	var handleReleaseChecker = function($target){
		var $td = $target.parent();
		var apiId = $td.attr('apiId');
		var apiVersion = $td.attr('apiVersion');
		var apiName = $td.attr('apiName');
		var sortId = $td.attr('apiSort');
		var url = new StringBuffer();
		
		url.append('url:'+$.basePath+'/apitools/apitools.htm?sourceFlag=2');
		url.append('&apiId='+apiId);
		url.append('&apiVersion='+apiVersion);
		url.append('&apiName='+apiName);
		url.append('&sortId='+sortId);
		$.dialog({
			content: url.toStr(),
			width: '993px',
			height:'600px'
		});
	}
	
	/*
	 * 克隆API信息
	 */
	var handleClone = function($target){
		var portlet_body = 'portlet_body_api_info';
		var url = $.basePath+'/apiRelease/cloneApi.action';
		var param = {
				apiId : $target.parent().attr('apiId'),
				apiVersion : $target.parent().attr('apiVersion')
		}
		var btn_search = 'btn_search';
		
		App.blockUI({
            target: '#'+portlet_body,
            message: '执行中...'
        });
		// 绑定Ajax的内容
		$.ajax({
			url : url,
			type : "post",
			data : param,
			dataType : "json",
			success : function(data) {
				App.unblockUI('#'+portlet_body);
				if (!showError(data)) {
					$('#'+btn_search).trigger('click');
					bootbox.alert("操作成功");
				}
			},
			error : function(data, e) {
				App.unblockUI('#'+portlet_body);
				bootbox.alert("系统错误,请稍候再试！");
			}
		});
	}
	
	var handleModalApiDetail = function(){
		$('#modal_apiDetailTitle').empty();
		$('#tb_apiDetail tr:gt(0)').remove();
		$('#container_apiArg').empty();
		
		var modal_content_apiDetail = 'modal_content_apiDetail';
		App.blockUI({
		    target: '#'+modal_content_apiDetail,
		    message: '执行中...'
		});
		
		var url = $.basePath+'/apiRelease/getApiDetail.json';
		var apiId = $('#txt_apiId_tmp').val();
		var apiVersion = $('#txt_apiVersion_tmp').val();
		var apiName = $('#txt_apiName_tmp').val();
		var param = {
			apiId :　apiId,
			apiVersion : apiVersion,
			apiName : apiName
		}
		// 绑定Ajax的内容
		$.ajax({
			url : url,
			type : "post",
			data : param,
			dataType : "json",
			success : function(data) {
				if (!showError(data)) {
					var apiName = data.apiName;
					var apiVersion = data.apiVersion;
					var apiArg = data.apiArg;
					var sb1 = new StringBuffer();
					var sb2 = new StringBuffer();
					
					$('#modal_apiDetailTitle').html('（名称:'+apiName+'&nbsp;&nbsp;版本号：'+apiVersion+'）');
					
					$.each(apiArg, function(index, apiInfo){
						var sourceDesc = apiInfo.sourceDesc;
						var schemaName = apiInfo.schemaName;
						var tabName = apiInfo.tabName;
						var fieldList = apiInfo.fieldList;
						
						sb1.append('<tr>');
						sb1.append('<td>'+(index+1)+'</td>');
						sb1.append('<td>'+sourceDesc+'</td>');
						sb1.append('<td>'+schemaName+'</td>');
						sb1.append('<td>'+tabName+'</td>');
						sb1.append('</tr>');
						
						$.each(fieldList, function(index, field){
							var fieldName = field.fieldName;
							var fieldFileDesc= (field.fieldFileDesc == null || field.fieldFileDesc == '')? '暂无' : field.fieldFileDesc;
							var fieldTargtType = field.fieldTargtType;
							
							var syncMustTypeDesc = field.reqArgId == 1 ? field.syncMustTypeDesc : '未选';
							var syncMustOneGrpId = field.syncMustOneGrpId == null ? '暂无' : field.syncMustOneGrpId;
							var asynMustTypeDesc = field.reqArgId == 1 ? field.asynMustTypeDesc : '未选';
							var asynMustOneGrpId = field.asynMustOneGrpId == null ? '暂无' : field.asynMustOneGrpId;
							var rssMustTypeDesc = field.reqArgId == 1 ? field.rssMustTypeDesc : '未选';
							var rssMustOneGrpId = field.rssMustOneGrpId == null ? '暂无' : field.rssMustOneGrpId;
							
							var reqArgId = field.reqArgId == 1 ? '已选' : '未选';
							var reqArgDefltVal = (field.reqArgDefltVal == null || field.reqArgDefltVal == '')? '暂无' : field.reqArgDefltVal;
							var calcPrincDesc = field.calcPrincDesc;
							var valueRange = field.valueRange;
							var respnArgId = field.respnArgId == 1 ? '已选' : '未选';
							var respnArgSampVal = (field.respnArgSampVal == null || field.respnArgSampVal == '') ? '暂无' : field.respnArgSampVal;
							
							sb2.append('<h4 class="form-section">'+fieldName+'</h4>');
							
							sb2.append('<div class="row">');
							sb2.append('	<div class="col-md-6">');
							sb2.append('		<div>');
							sb2.append('			<label class="control-label col-md-3">字段描述:</label>');
							sb2.append('			<div class="col-md-9">');
							sb2.append('				<p class="form-control-static">'+fieldFileDesc+'</p>');
							sb2.append('			</div>');
							sb2.append('		</div>');
							sb2.append('	</div>');
							sb2.append('	<div class="col-md-6">');
							sb2.append('		<div>');
							sb2.append('			<label class="control-label col-md-3">字段类型:</label>');
							sb2.append('			<div class="col-md-9">');
							sb2.append('				<p class="form-control-static" id="label_fieldName">'+fieldTargtType+'</p>');
							sb2.append('			</div>');
							sb2.append('		</div>');
							sb2.append('	</div>');
							sb2.append('</div>');
							
							/*sb2.append('<div class="row">');
							sb2.append('	<div class="col-md-6">');
							sb2.append('		<div>');
							sb2.append('			<label class="control-label col-md-3">必选类型:</label>');
							sb2.append('			<div class="col-md-9">');
							sb2.append('				<p class="form-control-static">'+mustTypeDesc+'</p>');
							sb2.append('			</div>');
							sb2.append('		</div>');
							sb2.append('	</div>');
							sb2.append('	<div class="col-md-6">');
							sb2.append('		<div>');
							sb2.append('			<label class="control-label col-md-3">组号:</label>');
							sb2.append('			<div class="col-md-9">');
							sb2.append('				<p class="form-control-static" id="label_fieldName">'+mustOneGrpId+'</p>');
							sb2.append('			</div>');
							sb2.append('		</div>');
							sb2.append('	</div>');
							sb2.append('</div>');*/
							
							sb2.append('<div class="row">');
							sb2.append('	<div class="col-md-6">');
							sb2.append('		<div>');
							sb2.append('			<label class="control-label col-md-3">同步必选类型:</label>');
							sb2.append('			<div class="col-md-9">');
							sb2.append('				<p class="form-control-static">'+syncMustTypeDesc+'</p>');
							sb2.append('			</div>');
							sb2.append('		</div>');
							sb2.append('	</div>');
							sb2.append('	<div class="col-md-6">');
							sb2.append('		<div>');
							sb2.append('			<label class="control-label col-md-3">同步组号:</label>');
							sb2.append('			<div class="col-md-9">');
							sb2.append('				<p class="form-control-static" id="label_fieldName">'+syncMustOneGrpId+'</p>');
							sb2.append('			</div>');
							sb2.append('		</div>');
							sb2.append('	</div>');
							sb2.append('</div>');
							sb2.append('<div class="row">');
							sb2.append('	<div class="col-md-6">');
							sb2.append('		<div>');
							sb2.append('			<label class="control-label col-md-3">异步必选类型:</label>');
							sb2.append('			<div class="col-md-9">');
							sb2.append('				<p class="form-control-static">'+asynMustTypeDesc+'</p>');
							sb2.append('			</div>');
							sb2.append('		</div>');
							sb2.append('	</div>');
							sb2.append('	<div class="col-md-6">');
							sb2.append('		<div>');
							sb2.append('			<label class="control-label col-md-3">异步组号:</label>');
							sb2.append('			<div class="col-md-9">');
							sb2.append('				<p class="form-control-static" id="label_fieldName">'+asynMustOneGrpId+'</p>');
							sb2.append('			</div>');
							sb2.append('		</div>');
							sb2.append('	</div>');
							sb2.append('</div>');
							sb2.append('<div class="row">');
							sb2.append('	<div class="col-md-6">');
							sb2.append('		<div>');
							sb2.append('			<label class="control-label col-md-3">订阅必选类型:</label>');
							sb2.append('			<div class="col-md-9">');
							sb2.append('				<p class="form-control-static">'+rssMustTypeDesc+'</p>');
							sb2.append('			</div>');
							sb2.append('		</div>');
							sb2.append('	</div>');
							sb2.append('	<div class="col-md-6">');
							sb2.append('		<div>');
							sb2.append('			<label class="control-label col-md-3">订阅组号:</label>');
							sb2.append('			<div class="col-md-9">');
							sb2.append('				<p class="form-control-static" id="label_fieldName">'+rssMustOneGrpId+'</p>');
							sb2.append('			</div>');
							sb2.append('		</div>');
							sb2.append('	</div>');
							sb2.append('</div>');
							
							sb2.append('<div class="row">');
							sb2.append('	<div class="col-md-6">');
							sb2.append('		<div>');
							sb2.append('			<label class="control-label col-md-3">请求参数标识:</label>');
							sb2.append('			<div class="col-md-9">');
							sb2.append('				<p class="form-control-static">'+reqArgId+'</p>');
							sb2.append('			</div>');
							sb2.append('		</div>');
							sb2.append('	</div>');
							sb2.append('	<div class="col-md-6">');
							sb2.append('		<div>');
							sb2.append('			<label class="control-label col-md-3">请求默认值:</label>');
							sb2.append('			<div class="col-md-9">');
							sb2.append('				<p class="form-control-static" id="label_fieldName">'+reqArgDefltVal+'</p>');
							sb2.append('			</div>');
							sb2.append('		</div>');
							sb2.append('	</div>');
							sb2.append('</div>');
							
							sb2.append('<div class="row">');
							sb2.append('	<div class="col-md-6">');
							sb2.append('		<div>');
							sb2.append('			<label class="control-label col-md-3">响应参数标识:</label>');
							sb2.append('			<div class="col-md-9">');
							sb2.append('				<p class="form-control-static" id="label_fieldName">'+respnArgId+'</p>');
							sb2.append('			</div>');
							sb2.append('		</div>');
							sb2.append('	</div>');
							sb2.append('	<div class="col-md-6">');
							sb2.append('		<div>');
							sb2.append('			<label class="control-label col-md-3">响应事例值:</label>');
							sb2.append('			<div class="col-md-9">');
							sb2.append('				<p class="form-control-static">'+respnArgSampVal+'</p>');
							sb2.append('			</div>');
							sb2.append('		</div>');
							sb2.append('	</div>');
							sb2.append('</div>');
							
							sb2.append('<div class="row">');
							sb2.append('	<div class="col-md-6">');
							sb2.append('		<div>');
							sb2.append('			<label class="control-label col-md-3">取值范围:</label>');
							sb2.append('			<div class="col-md-9">');
							sb2.append('				<p class="form-control-static">'+calcPrincDesc+'('+valueRange+')</p>');
							sb2.append('			</div>');
							sb2.append('		</div>');
							sb2.append('	</div>');
							sb2.append('</div>');
						})
					});
					
					$('#tb_apiDetail tbody').append(sb1.toStr());
					$('#container_apiArg').append(sb2.toStr());
					App.unblockUI('#'+modal_content_apiDetail);
				}
			},
			error : function(data, e) {
				bootbox.alert("系统错误,请稍候再试！");
			}
		});
	}
	
	/*
	 * 展示api测试沙箱信息
	 */
	var displayModalApiApprove = function($target){
		var apiId = $target.parent().attr('apiId');
		var apiVersion = $target.parent().attr('apiVersion');
		var tab_id = 'tb_apiApprove';
		var audit_id = 'txt_audit_advc';
		var modal_content_id = 'modal_content_apiApprove';
		var url = $.basePath + '/apiApprove/getApiTestBox.action';
		var param = {
			apiId : apiId,
			apiVersion : apiVersion
		};
		var sb =  new StringBuffer();
		
		App.blockUI({
            target: '#'+modal_content_id,
            message: '加载中...'
        });
		$.ajax({
			url : url,
			type : "post",
			data : param,
			dataType : "json",
			success : function(data) {
				if (!showError(data)) {
					$.each(data, function(index, item){
						var boxId = item.boxId;
						var reqArgs = item.reqArgs;
						var submitType = item.submitType;
						var returnType = item.returnType;
						var testResult = item.testResult;
						var isSuccess = item.isSuccess == 0 ? '<span class="label label-success">成功</span>' : '<span class="label label-danger">失败</span>';
						var styleCenter = 'line-height:128px;';
						var styleUpper = 'text-transform:uppercase;';
						sb.append('<tr boxId="'+boxId+'">');
						sb.append('<td class="text-center" style="'+styleCenter+'">'+(index+1)+'</td>');
						sb.append('<td><textarea class="form-control" rows="6" readOnly="true" style="background-color:#FFFFFF;">'+reqArgs+'</textarea></td>');
						sb.append('<td class="text-center" style="'+styleCenter+styleUpper+'">'+submitType+'</td>');
						sb.append('<td class="text-center"style="'+styleCenter+styleUpper+'">'+returnType+'</td>');
						sb.append('<td><textarea class="form-control" rows="6" readOnly="true" style="background-color:#FFFFFF;">'+testResult+'</textarea></td>');
						sb.append('</textarea></td>');
						sb.append('<td class="text-center" style="'+styleCenter+'">'+isSuccess+'</td>');
						sb.append('</tr>');
					});
					$('#'+tab_id+' tbody').append(sb.toStr());
				}
				App.unblockUI('#'+modal_content_id);
			},
			error : function(data, e) {
				bootbox.alert("系统错误,请稍候再试！");
			}
		});
	}
	
	/*
	 * 审批api（2：审批通过（已审批） 3：审批驳回）
	 */
	var doApiApprove = function(auditStat){
		var apiId = $('#txt_apiId_tmp_new').val();
		var apiVersion = $('#txt_apiVersion_tmp_new').val();
		var auditAdvc = $('#txt_audit_advc').val();
		var testSndboxIds = new Array();
		$.each($('#tb_apiApprove tr:gt(0)'), function(index, item){
			var boxId = $(item).attr('boxId');
			testSndboxIds.push(boxId);
		});
		var url = $.basePath + '/apiApprove/doApiApprove.action'
		var param = {
			apiId : apiId,
			apiVersion : apiVersion,
			auditStat : auditStat,
			auditAdvc : auditAdvc,
			testSndboxIds : testSndboxIds.join("、")
		}
		$.ajax({
			url : url,
			type : "post",
			data : param,
			dataType : "json",
			success : function(data) {
				if (!showError(data)) {
					bootbox.alert("操作成功", function(){
						$('#btn_search').trigger('click');
						$('#modal_apiApprove').modal('hide');
					});
				}
			},
			error : function(data, e) {
				bootbox.alert("系统错误,请稍候再试！");
			}
		});
	}
	
	return {
        init: function () {
        	handleSelect2('form_apiSearch');
            handleDatePickers();
            handlePagination();
            handleValidation();
            handleToolTip();
            handleSelect($('#sel_apiSort'), $.basePath+'/apiRelease/getSelect.json', {method:'getApiSort'},{valueField:'apiSort', textField:'apiSortName'}, '');
            handleSelect($('#sel_examStat'), $.basePath+'/apiRelease/getSelect.json', {method:'getApiExamStat'},{valueField:'codeId', textField:'codeDesc'}, '');
            
            
            $('body').on('click', '#btn_search', function(){
            	handlePagination();
            });
            $('body').on('click', '#btn_reset', function(){
            	$('#form_apiSearch')[0].reset();
            });
            $('body').on('change', 'input:radio[name="apiRange"]', function(){
            	$('#btn_search').trigger('click');
            });
            $('body').on('show.bs.modal', '#modal_apiItem', function (e) {
            	$('#form_apiItem')[0].reset();	//表单重置
            	$("#form_apiItem").validate().resetForm();//表单校验重置
            	handleModalApiItem($(e.relatedTarget));
        	});
            $('body').on('shown.bs.modal', '#modal_apiDetail', function (e) {
            	handleModalApiDetail($(e.relatedTarget));
        	});
            $('body').on('click', '#btn_insertApiItem, #btn_updateApiItem', function(){
            	$('#form_apiItem').submit();
            });
            $('body').on('click', '.btn_deleteApiItem', function(){
            	handleDelete($(this));
            });
            $('body').on('click', '.btn_releaseApiItem', function(){
            	handleReleaseChecker($(this));
            });
            $('body').on('click', '.btn_cloneApiItem', function(){
            	handleClone($(this));
            });
            $('body').on('click', '#btn_configApiArg', function(){
				var apiId = $('#txt_apiId_tmp').val();
				var apiVersion = $('#txt_apiVersion_tmp').val();
				var apiName = $('#txt_apiName_tmp').val();
            	window.open($.basePath+'/apiRelease/configApiArg.htm?apiId='+apiId+'&apiVersion='+apiVersion+'&apiName='+apiName);
            });
            
            /*
             * 展示api审批modal
             * modal展示前清空表单
             */
            $('body').on('show.bs.modal', '#modal_apiApprove', function (e) {
            	var $target = $(e.relatedTarget);
            	var apiId = $target.parent().attr('apiId');
            	var apiVersion = $target.parent().attr('apiVersion');
            	var api_id = 'txt_apiId_tmp_new';
            	var api_version = 'txt_apiVersion_tmp_new';
            	var tab_id = 'tb_apiApprove';
            	var audit_id = 'txt_audit_advc';
            	
            	$('#'+api_id).val(apiId);
            	$('#'+api_version).val(apiVersion);
            	$('#'+tab_id +' tr:gt(0)').remove();
            	$('#'+audit_id).val('');
        	});
            /*
             * 展示api审批modal
             * modal展示后加载表单数据 
             */
            $('body').on('shown.bs.modal', '#modal_apiApprove', function (e) {
            	displayModalApiApprove($(e.relatedTarget));
        	});
            $('body').on('click', '#btn_apiApproveYes', function(){
            	var ifValid = $('#form_apiApprove').valid();
            	if(ifValid){
            		doApiApprove('2');
            	}
            });
            $('body').on('click', '#btn_apiApproveNo', function(){
            	var ifValid = $('#form_apiApprove').valid();
            	if(ifValid){
            		doApiApprove('3');
            	}
            });
        }
    };
}();

jQuery(document).ready(function() {    
	ApiRelease.init(); 
});

function refreshPage(){
	$('#btn_search').trigger('click');
}

function closeModalApiItem(){
	$('#modal_apiItem').modal('hide');
}