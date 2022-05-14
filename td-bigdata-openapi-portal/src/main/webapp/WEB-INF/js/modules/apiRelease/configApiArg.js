var ApiDetail = function(){
	
	/*
	 * 初始化wizard
	 */
	var handleWizard = function() {
		$("#wizard").bwizard({
			backBtnText : "上一步",
			nextBtnText : "下一步",
			clickableSteps : false,	//Determines whether step tabs are clickable
			hideOption : {
				fade : false
			},
			showOption : {
				fade : false
			},
			validating: function (e, ui) { 
				var result = true;
				var curIndex = ui.index;
				var nextIndex = ui.nextIndex;
				var ifNextStep = (nextIndex > curIndex);
				switch (curIndex){
					case 0:
						$('.btn_submit').trigger('click');
						$('.portlet-source:not(.hide)').find('.form-horizontal').each(function(){
							if(!$(this).valid()) {
								result = false;
								return false;
							}
						})
						if(result == true && ifNextStep){
							handleMultiSelect();
						}
						break;
					case 1:
						if(ifNextStep && $('#multi_sel_field').val()== null){
							bootbox.alert("目标字段不能为空！");
							result = false;
						}
						if(result == true && ifNextStep){
							handleFieldConfig();
						}
						break;
					case 2:
						break;
					case 3:
						break;
					default:;
				}
				return result;
			},
			activeIndexChanged : function (e, ui) { 
				var curIndex = ui.index;
				switch (curIndex){
					case 0:
						break;
					case 1:
						break;
					case 2:
						break;
					case 3:
						break;
					default:;
				}
			}
		});
	}
	
	var handleValidation = function($form) {
		$form.validate({
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
            	sourceId: {
                    minlength: 1,
                    required: true
                },
                schemaName: {
                    required: true
                },
                tabName: {
                    required: true
                }
            },
            messages: {
            	sourceId:'数据源为必选项',
            	schemaName:'数据库名称为必选项',
            	tabName:'表名称为必选项'
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
            }
        });
        $('.select2').change(function(){  
            $(this).valid();  
        });  
	}
	
	var handleToolTip = function() {
		$('.toolTip').tooltip();
	}
	
	//获取form表单参数
	var getFormJson = function($form) {
		var o = {};
		//$sel_disabled = $form.find(' select[disabled = "disabled"]');
		//$sel_disabled.removeAttr("disabled","disabled"); 
		var a = $form.serializeArray();
		//$sel_disabled.attr("disabled","disabled"); 
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
	
	var handleHover = function() {
		$('#portlet_addDataSource').hover(function(){
			$(this).removeClass('white').addClass('green');
		},function(){
			$(this).removeClass('green').addClass('white');
		});
	}
	
	/*
	 * 初始化select2
	 */
	var handleSelect2 = function() {
		$("#sel_sourceId1, #sel_schemaName1").select2({
			width: null,
            language: 'zh-CN',
            minimumResultsForSearch: -1
        });
		$("#sel_schemaName1").select2({
			width: null,
            language: 'zh-CN'
        });
		$('#sel_tabName1').select2({
			width: null,
            language: 'zh-CN',
            ajax: {
                url: $.basePath+'/apiRelease/getTable.json',
                dataType: 'json',
                delay: 250,
                data: function(params) {
                    return {
                    	sourceId : $('#sel_sourceId1').val(),
                    	schemaName : $('#sel_schemaName1').val(),
                        q: params.term, // search term
                        page: params.page
                    };
                },
                processResults: function(data, page) {
                	var options = [];
	                for(var i= 0;i<data.length;i++){
	                    var option = {"id":data[i]["tabName"], "text":data[i]["tabName"]};
	                    options.push(option);
	                }
                    return {
	                    results: options,
	                    pagination: {
	                        //more:true
	                    	more:false
	                    }
                    };
                },
                cache: true
            },
            escapeMarkup: function(markup) {
                return markup;
            }, 
            minimumInputLength: 1
        });
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
		var sb = new StringBuffer();
		// 绑定Ajax的内容
		$.ajax({
			url : sel_url,
			type : "get",
			async:true,
			data : sel_param,
			dataType : "json",
			success : function(data) {
				if (!showError(data)) {
					$sel_obj.empty();// 清空下拉框
					sb.append("<option value=''>--请选择--</option>");
					$.each(data, function(i, item) {
						var opt_val = item[sel_field.valueField];
						var opt_text = item[sel_field.textField];
						if (sel_val) {
							if (opt_val == sel_val) {
								sb.append("<option value='" + opt_val + "' selected='selected'>&nbsp;" + opt_text + "</option>");
							} else {
								sb.append("<option value='" + opt_val + "'>&nbsp;" + opt_text + "</option>");
							}
						} else {
							sb.append("<option value='" + opt_val + "'>&nbsp;" + opt_text + "</option>");
						}
					});
					$sel_obj.append(sb.toStr());
					if(sel_val){
						//$sel_obj.trigger('change');
						var select_id = $sel_obj.attr('id');
						var selected_text = $sel_obj.find('option:selected').text();
			    		var $select2 = $sel_obj.next().find('.select2-selection__rendered');
			    		$select2.text(selected_text);
			    		$select2.attr('title', selected_text);
					}
				}
			},
			error : function(data, e) {
				bootbox.alert("系统错误,请稍候再试！");
			}
		});
	}
	
	/*
	 * 获取选择框
	 * sel_url:选择url
	 * sel_param:选择参数
	 * sel_field:要获取的字段
	 * sel_val:选择项
	 */
	var handleSelectData = function(sel_url, sel_param, sel_field, sel_val, async_val){
		var sb = new StringBuffer();
		// 绑定Ajax的内容
		$.ajax({
			url : sel_url,
			type : "post",
			async: async_val,
			data : sel_param,
			dataType : "json",
			success : function(data) {
				if (!showError(data)) {
					/*sb.append("<option value=''>--请选择--</option>");*/
					$.each(data, function(i, item) {
						var opt_val = item[sel_field.valueField];
						var opt_text = item[sel_field.textField];
						if (sel_val) {
							if (opt_val == sel_val) {
								sb.append("<option value='" + opt_val + "' selected='selected'>&nbsp;" + opt_text + "</option>");
							} else {
								sb.append("<option value='" + opt_val + "'>&nbsp;" + opt_text + "</option>");
							}
						} else {
							sb.append("<option value='" + opt_val + "'>&nbsp;" + opt_text + "</option>");
						}
					});
				}
			},
			error : function(data, e) {
				bootbox.alert("系统错误,请稍候再试！");
			}
		});
		return sb.toStr();
	}
	
	/*
	 * 清除下拉框信息
	 */
	var handleClearSelect = function($sel_obj){
		$sel_obj.empty();
		$sel_obj.append("<option value=''>--请选择--</option>");
	}
	
	/*
	 * 删除数据源
	 */
	var handleRemoveSource = function(){
		//重排序数据源order
		$('.sourceNum').each(function(index, obj){
			$(this).html(index+1);
		});
	} 
	
	/*
	 * 添加新的数据源
	 */
	var handleAddSource = function(){
		var $portlet_newSource = $('#tpl_portlet_source').clone(true, true).removeClass('hide');
		var sourcelength = $('.sourceNum').length;
		$portlet_newSource.find('.portlet-title .caption').html('数据源_<span class="sourceNum">'+(sourcelength+1)+'</span>');
		$portlet_newSource.find('.form-horizontal').append('<button class="btn_submit hide"></button>');
		$portlet_newSource.insertBefore($('#portlet_addDataSource'));
		handleSelect($portlet_newSource.find('.sel_sourceId'), $.basePath+'/apiRelease/getSelect.json', {method : 'getSource'}, {valueField : 'sourceId', textField : 'sourceDesc'}, '');
		$portlet_newSource.find('.sel_sourceId').select2({
			width: null,
            language: 'zh-CN',
            minimumResultsForSearch: -1
        });
		$portlet_newSource.find('.sel_schemaName').select2({
			width: null,
            language: 'zh-CN'
        });
		/*$portlet_newSource.find('.sel_tabName').select2({
			width: null,
            language: 'zh-CN'
		});*/
		$portlet_newSource.find('.sel_tabName').select2({
			width: null,
            language: 'zh-CN',
            ajax: {
                url: $.basePath+'/apiRelease/getTable.json',
                dataType: 'json',
                delay: 250,
                data: function(params) {
                    return {
                    	sourceId : $portlet_newSource.find('.sel_sourceId').val(),
                    	schemaName : $portlet_newSource.find('.sel_schemaName').val(),
                        q: params.term, // search term
                        page: params.page
                    };
                },
                processResults: function(data, page) {
                	var options = [];
	                for(var i= 0;i<data.length;i++){
	                    var option = {"id":data[i]["tabName"], "text":data[i]["tabName"]};
	                    options.push(option);
	                }
                    return {
	                    results: options,
	                    pagination: {
	                        more:false
	                    }
                    };
                },
                cache: true
            },
            escapeMarkup: function(markup) {
                return markup;
            }, 
            minimumInputLength: 1
        });
		handleValidation($portlet_newSource.find('.form-horizontal'));
	}
	
	/*
	 * step2
	 */
	var handleMultiSelect = function () {
		var multi_sel_field = 'multi_sel_field';
		
		//已经step1->step2初始化下拉框，再次进入无需初始化
		//if($('#'+multi_sel_field).val() != null) return;
		
		var portlet_field = 'portlet_field';
		var url = $.basePath + '/apiRelease/getSourceField.json';
		var param = new Array();
		$('#'+multi_sel_field).multiSelect({
            selectableOptgroup: true,
            selectableHeader : '<div class="ms-header text-center">源字段</div>',
            selectionHeader : '<div class="ms-header text-center">目标字段</div>'
        });
		
		App.blockUI({
            target: '#'+portlet_field,
            message: '加载中...'
        });
		
        $('.portlet-source:not(.hide)').find('.form-horizontal').each(function(){
        	var args = getFormJson($(this));
        	param.push(args);
		});
        
        $.ajax({
			url : url,
			type : "post",
			data : {
				jsonArray : JSON.stringify(param)
			},
			async : false,
			dataType : "json",
			success : function(data) {
				if (!showError(data)) {
					 $('#'+multi_sel_field).empty();
					 var sb = new StringBuffer();
					 sb.append('<optgroup label="全选:">');
					 $.each(data, function(i, item) {
						 var sourceNum = param.length;//选择数据源的个数
						 var fieldTypeList = item.fieldTypeList;
						 var sourceFieldList = item.sourceFieldList;
						 var fieldTypeLength = fieldTypeList.length;
						 var sourceFieldLength = sourceFieldList.length;
						 var disabled = "";
						 var disabledDesc = "";
						 var fieldTargtType = "";
						 if(fieldTypeLength == 1 && sourceFieldLength == sourceNum){//字段类型唯一，且选择的每个数据源都包含这个字段
							 fieldTargtType = ' fieldTargtType = "'+fieldTypeList[0].fieldTargtType+'"';
						 }else if(sourceFieldLength != sourceNum){
							 disabled = ' disabled="disabled"';
							 disabledDesc = '(某源数据不包含该字段)';
						 }else{
							 disabled = ' disabled="disabled"';
							 disabledDesc = '(源数据之间目标字段类型不统一)';
						 }
						 var fieldName = item.fieldName;
						 sb.append('<option value="'+fieldName+'"' + fieldTargtType + disabled + '>' + fieldName + disabledDesc+'</option>');
					 });
					 sb.append('</optgroup>');
					 $('#'+multi_sel_field).append(sb.toStr());
					 $('#'+multi_sel_field).multiSelect('refresh');
					 
					 if(updateFlag == true && updateChanged == false){
						 initStep2ForUpdate();
					 }
				}
				App.unblockUI('#'+portlet_field);
			},
			error : function(data, e) {
				bootbox.alert("系统错误,请稍候再试！");
				App.unblockUI('#'+portlet_field); 
			}
		});
    }
	
	/*
	 * step3获取下拉框date
	 */
	/*直接去源表中的目标字段类型,无需自己选择
	 * var sel_option_fieldTargtType = handleSelectData($.basePath+'/apiRelease/getSelect.json', {method : 'getDataTargtTypeCode'}, {valueField : 'fieldTargtType', textField : 'fieldTargtType'}, '', false);
	*/
	var sel_option_mustType = handleSelectData($.basePath+'/apiRelease/getSelect.json', {method : 'getArgMustCode'}, {valueField : 'codeId', textField : 'codeDesc'}, 2, false);
	
	/*
	 * step3初始化选择的字段信息
	 */
	var handleFieldConfig = function(){ 
		var selField = $('#multi_sel_field').val()+'';
		var arr_selField = selField.split(',');
		
		var portlet_fieldConfig = 'portlet_fieldConfig';
		var tb_fieldConfig = 'tb_fieldConfig';
		App.blockUI({
            target: '#'+portlet_fieldConfig,
            message: '加载中...'
        });
		$('#'+tb_fieldConfig +' tr:gt(1)').remove();    
		var sel_option_mustOneGrpId = '<option value="1">1</option><option value="2">2</option><option value="3">3</option><option value="4">4</option><option value="5">5</option>';
		$.each(arr_selField, function(index, obj){
			var sb = new StringBuffer();
			var fieldTargtType = $('#multi_sel_field').find('option[value='+obj+']').attr("fieldTargtType");
			sb.append('<tr>');
			sb.append('		<td>');
			sb.append('			<div class="td-label">'+obj+'</div> ');
			sb.append('			<input name="fieldName" type="text" class="fieldName hide" value="'+obj+'" /></td>');
			sb.append('		<td><input name="fieldFileDesc" type="text" class="fieldFileDesc form-control form-filter input-sm" placeholder="描述" />');
			sb.append('		</td>');
			/*直接取源表中的目标字段类型,无需自己选择
			sb.append('		<td><select name="fieldTargtType"');
			sb.append('			class="form-control form-filter input-sm">'+sel_option_fieldTargtType);
			sb.append('		</select></td>');*/
			sb.append('		<td>');
			sb.append('			<div class="td-label">'+fieldTargtType+'</div> ');
			sb.append('			<input name="fieldTargtType" type="text" class="fieldTargtType hide" value="'+fieldTargtType+'" /></td>');
			sb.append('		<td class="hide"><select name="syncMustType"');
			sb.append('			class="form-control form-filter input-sm mustType syncMustType" mustTypeDesc="sync" disabled="disabled">'+sel_option_mustType);
			sb.append('		</select></td>');
			sb.append('		<td class="hide"><select name="syncMustOneGrpId"');
			sb.append('			class="form-control form-filter input-sm syncMustOneGrpId" mustTypeDesc="sync" disabled="disabled">'+sel_option_mustOneGrpId);
			sb.append('		</select></td>');
			sb.append('		<td class="hide"><select name="asynMustType"');
			sb.append('			class="form-control form-filter input-sm mustType asynMustType" mustTypeDesc="asyn"  disabled="disabled">'+sel_option_mustType);
			sb.append('		</select></td>');
			sb.append('		<td class="hide"><select name="asynMustOneGrpId"');
			sb.append('			class="form-control form-filter input-sm asynMustOneGrpId" mustTypeDesc="asyn" disabled="disabled">'+sel_option_mustOneGrpId);
			sb.append('		</select></td>');
			sb.append('		<td class="hide"><select name="rssMustType"');
			sb.append('			class="form-control form-filter input-sm mustType rssMustType" mustTypeDesc="rss"  disabled="disabled">'+sel_option_mustType);
			sb.append('		</select></td>');
			sb.append('		<td class="hide"><select name="rssMustOneGrpId"');
			sb.append('			class="form-control form-filter input-sm rssMustOneGrpId" mustTypeDesc="rss" disabled="disabled">'+sel_option_mustOneGrpId);
			sb.append('		</select></td>');
			sb.append('		<td>');
			sb.append('			<div class="text-center td-label">');
			sb.append('				<input class="reqArgId" name="reqArgId" type="checkbox"');
			sb.append('					value="1" />');
			sb.append('			</div></td>');
			sb.append('		<td><input class="reqArgDefltVal" name="reqArgDefltVal" type="text"');
			sb.append('			class="form-control form-filter input-sm" placeholder="" disabled="disabled" />');
			sb.append('		</td>');
			sb.append('		<td>');
			sb.append('			<span class="td-label calc_princ_desc">不限制</span><i class="fa fa-edit toolTip calc_princ" title="配置取值范围"></i>');
			sb.append('			<input name="calcPrincId" type="text" class="calcPrincId hide" value="1000">');
			sb.append('			<input name="valueRange" type="text" class="valueRange hide" value=""/>');
			sb.append('		</td>');
			sb.append('		<td>');
			sb.append('			<div class="text-center td-label">');
			sb.append('				<input class="respnArgId" name="respnArgId" type="checkbox"');
			sb.append('					 value="1" />');
			sb.append('			</div></td>');
			sb.append('		<td><input class="respnArgSampVal" name="respnArgSampVal" type="text"');
			sb.append('			class="form-control form-filter input-sm" placeholder="" disabled="disabled" />');
			sb.append('		</td>');
			sb.append('</tr>');
			
			$('#'+tb_fieldConfig +' tbody').append(sb.toStr());
			$('#'+tb_fieldConfig +' tbody .calc_princ').tooltip();
		});
		
		var apiVisitMethdArray = $('#apiVisitMethd').val().split(',');
		$.each(apiVisitMethdArray, function(i, item){
			var mustType;
			switch(item){
				case '0':
					mustType = 'sync';break;
				case '1':
					mustType = 'asyn';break;
				case '2':
					mustType = 'rss';break;
			}
			$('#'+tb_fieldConfig +' tr th[mustTypeDesc="' + mustType + '"]').removeClass('hide');
			$('#'+tb_fieldConfig +' tr td').has('select[mustTypeDesc="' + mustType + '"]').removeClass('hide');
		});
		
		if(updateFlag == true && updateChanged == false){
			initStep3ForUpdate();
		}
		App.unblockUI('#'+portlet_fieldConfig); 
	}
	
	/*
	 * 初始化step3中的计算法则下拉框
	 */
	var handleCalcPrincId = function(){
		var sel_option_calcPrincId = handleSelectData($.basePath+'/apiRelease/getSelect.json', {method : 'getCalcPrincCode'}, {valueField : 'calcPrincId', textField : 'calcPrincDesc'}, '', false);
		$('#sel_calcPrincId_tmp').append(sel_option_calcPrincId);
	}
	
	/*
	 * 获取step3中的字段信息
	 */
	var getFieldJson = function($obj) {
		var o = {};
		var a = $obj.find('input,select').serializeArray();
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
	 * 配置step3中的字段取值范围
	 */
	var handleCalcPrinc = function($target){
		$('#modal_calcPrinc').modal('toggle');
		$('#form_calcPrinc')[0].reset();
		var $tr = $target.closest('tr');
		var fieldName = $tr.find('.fieldName').val();
		var calcPrincId = $tr.find('.calcPrincId').val();
		var valueRange = $tr.find('.valueRange').val();
		$('#txt_fieldName_tmp').text(fieldName);
		if(calcPrincId){
			$('#sel_calcPrincId_tmp').val(calcPrincId);
			$('#txtarea_valueRange_tmp').val(valueRange);
		}else{
			$('#sel_calcPrincId_tmp').prop("selectedIndex", 0);	
			$('#txtarea_valueRange_tmp').val('');
		}
		$('#sel_calcPrincId_tmp').trigger("change");
	}
	/*
	 * step3保存_配置取值范围
	 */
	var setCalcPrinc = function(){
		var txt_fieldName_tmp = $('#txt_fieldName_tmp').text();
		var sel_calcPrincId_tmp = $('#sel_calcPrincId_tmp').val();
		var sel_calcPrincId_tmp_desc = $("#sel_calcPrincId_tmp").find("option:selected").text();
		var txtarea_valueRange_tmp = $('#txtarea_valueRange_tmp').val();
		var $tr = $('#portlet_fieldConfig .fieldName[value="'+txt_fieldName_tmp+'"]').closest('tr');
		$tr.find('.calcPrincId').val(sel_calcPrincId_tmp);
		$tr.find('.valueRange').val(txtarea_valueRange_tmp);
		$tr.find('.calc_princ_desc').text(sel_calcPrincId_tmp_desc+'(...)');
		$('#btn_cancelCalcPrinc').trigger('click');
	}
	
	/*
	 * step4
	 * 保存api详情
	 */
	var handleSubmit = function(){
		var submitContainer = 'submitContainer';
		App.blockUI({
            target: '#'+submitContainer,
            message: '执行中...'
        });
		var url = $.basePath + '/apiRelease/saveApiArg.action';
		var param_1 = new Array();//数据源信息
		var param_2 = new Array();//字段配置信息
		var apiId = $('#apiId').val();
		var apiVersion = $('#apiVersion').val();
		
		$('.portlet-source:not(.hide)').find('.form-horizontal').each(function(){
        	var args = getFormJson($(this));
        	param_1.push(args);
		});
		
		$('#portlet_fieldConfig tr:gt(1)').each(function(index, obj){
			var args = getFieldJson($(this));
			param_2.push(args);
		});
		$.ajax({
			url : url,
			type : "post",
			data : {
				apiId : apiId,
				apiVersion : apiVersion,
				jsonArray1 : JSON.stringify(param_1),
				jsonArray2 : JSON.stringify(param_2)
			},
			async : true,
			dataType : "json",
			success : function(data) {
				if (!showError(data)) {
					App.unblockUI('#'+submitContainer); 
					bootbox.alert(data.msg, function(){
						closeWindow();
					});
				}
			},
			error : function(data, e) {
				App.unblockUI('#'+submitContainer); 
				bootbox.alert("系统错误,请稍候再试！");
			}
		});
	}
	
	var initStep1ForUpdate = function(){
		App.blockUI({
            target: '#portlet_container',
            message: '初始化...'
        });
		var apiArg = eval($('#apiArg').val()); 
		if(apiArg.length != 1){
			for(var i = 0; i < apiArg.length-1; i++){
				handleAddSource();//$('#portlet_addDataSource').trigger('click');
			}
		}
		$.each(apiArg, function(index, obj){
			var $portletSource = $('.portlet-source:not(.hide):eq('+index+')');
			var $sourceId = $portletSource.find('.sel_sourceId');
			var $schemaName = $portletSource.find('.sel_schemaName');
			var $tabName = $portletSource.find('.sel_tabName');
			var sourceId = obj.sourceId;
			var schemaName = obj.schemaName;
			var tabName = obj.tabName;
			handleSelect($sourceId, $.basePath+'/apiRelease/getSelect.json', {method : 'getSource'}, {valueField : 'sourceId', textField : 'sourceDesc'}, sourceId);
			handleSelect($schemaName, $.basePath+'/apiRelease/getSelect.json', {method : 'getSchema', sourceId : sourceId}, {valueField : 'schemaName', textField : 'schemaName'}, schemaName);
			handleSelect($tabName, $.basePath+'/apiRelease/getTable.json', {sourceId : sourceId, schemaName : schemaName, q : tabName}, {valueField : 'tabName', textField : 'tabName'}, tabName);
		})
		
		var apiField = apiArg[0].fieldList;
		var $multiSelFieldVal = $('#multi_sel_field');
		var multiSelFieldVal = new StringBuffer();
		$.each(apiField, function(index, obj){
			multiSelFieldVal.append(obj.fieldName+",");
		});
		
		//待选择框全部赋值后,取消遮罩
		var interval = setInterval(function(){
			if($('.portlet-source:not(.hide) select').find('option:selected[value=""]').length == 0){
				App.unblockUI('#portlet_container');
				clearInterval(interval);
			}
		},300);   
	}
	
	var initStep2ForUpdate = function(){
		var $multiSelField = $('#multi_sel_field');
		var fieldNameArray = new Array();
		
		var apiArg = eval($('#apiArg').val()); 
		var fieldList = apiArg[0].fieldList;
		$.each(fieldList, function(index, obj){
			var fieldName = obj.fieldName;
			fieldNameArray.push(fieldName);
		});
		$multiSelField.multiSelect('select', fieldNameArray);
	}
	
	var initStep3ForUpdate = function(){
		var tb_fieldConfig = 'tb_fieldConfig';
		var apiArg = eval($('#apiArg').val()); 
		var fieldList = apiArg[0].fieldList;
		$.each(fieldList, function(index, obj){
			var $tr = $('#'+tb_fieldConfig).find('tr:has(input[name="fieldName"][value="'+obj.fieldName+'"])');
			if($tr.length == 1){
				var fieldName = obj.fieldName;    
				var fieldFileDesc = obj.fieldFileDesc;   
				var fieldTargtType = obj.fieldTargtType; 
				var syncMustType = obj.syncMustType;        
				var syncMustOneGrpId = obj.syncMustOneGrpId;    
				var asynMustType = obj.asynMustType;        
				var asynMustOneGrpId = obj.asynMustOneGrpId;
				var rssMustType = obj.rssMustType;        
				var rssMustOneGrpId = obj.rssMustOneGrpId;
				var reqArgId = obj.reqArgId;        
				var reqArgDefltVal = obj.reqArgDefltVal; 
				var calcPrincId = obj.calcPrincId; 
				var valueRange = obj.valueRange;
				var respnArgId = obj.respnArgId;      
				var respnArgSampVal = obj.respnArgSampVal; 
				
				var $fieldName = $tr.find('.fieldName');
				var $fieldFileDesc = $tr.find('.fieldFileDesc');  
				var $fieldTargtType = $tr.find('.fieldTargtType'); 
				var $syncMustType = $tr.find('.syncMustType');       
				var $syncMustOneGrpId = $tr.find('.syncMustOneGrpId');   
				var $asynMustType = $tr.find('.asynMustType');       
				var $asynMustOneGrpId = $tr.find('.asynMustOneGrpId');  
				var $rssMustType = $tr.find('.rssMustType');       
				var $rssMustOneGrpId = $tr.find('.rssMustOneGrpId');  
				var $reqArgId = $tr.find('.reqArgId');       
				var $reqArgDefltVal = $tr.find('.reqArgDefltVal'); 
				var $calcPrincId = $tr.find('.calcPrincId');    
				var $valueRange = $tr.find('.valueRange'); 
				var $calcPrincDesc = $tr.find('.calc_princ_desc');
				var $respnArgId = $tr.find('.respnArgId');     
				var $respnArgSampVal = $tr.find('.respnArgSampVal');
				var $selCalcPrincIdTmp = $('#sel_calcPrincId_tmp');
				var calcPrincDesc = $selCalcPrincIdTmp.find('option[value="'+calcPrincId+'"]').text()+"(...)";
				
				$fieldName.val(fieldName);      
				$fieldFileDesc.val(fieldFileDesc);  
				$fieldTargtType.val(fieldTargtType); 
				
				$syncMustType.val(syncMustType).trigger('change');
				if(syncMustType != 2){
					$syncMustOneGrpId.find('option[value="'+syncMustType+'"]').attr('selected', true);   
				}
				$asynMustType.val(asynMustType).trigger('change'); 
				if(asynMustType != 2){
					$asynMustOneGrpId.find('option[value="'+asynMustType+'"]').attr('selected', true);   
				}
				$rssMustType.val(rssMustType).trigger('change'); 
				if(rssMustType != 2){
					$rssMustOneGrpId.find('option[value="'+rssMustType+'"]').attr('selected', true);   
				}
				
				$reqArgId.prop('checked', (reqArgId == 1)).trigger('change');       
				$reqArgDefltVal.val(reqArgDefltVal); 
				$calcPrincId.val(calcPrincId);    
				$valueRange.val(valueRange); 
				$calcPrincDesc.text(calcPrincDesc);
				$respnArgId.prop('checked', (respnArgId == 1)).trigger('change'); 
				$respnArgSampVal.val(respnArgSampVal);
			}
		});
	}
	
	var updateFlag = false;
	var updateChanged = false;
	var initUpdateFlag = function(){
		updateFlag = ($('#updateFlag').val() == 'true');
	}
	return {
		init : function(){
			initUpdateFlag();
			handleValidation($('#form_source1'));
			handleWizard();
			handleToolTip();
			handleHover();
			handleCalcPrincId();
			handleSelect2();
			//handleSelect($('#sel_sourceId1'), $.basePath+'/apiRelease/getSelect.json', {method : 'getSource'}, {valueField : 'sourceId', textField : 'sourceDesc'}, '');
			
			/*
			 * 级联下拉框：数据源->数据库名称
			 */
			$(".sel_sourceId").on("change", function(e) {
				var $sel_schemaName = $(this).closest('.form-body').find('.sel_schemaName');
				var sourceId = $(this).val();
				$sel_schemaName.select2("val", "");
				if (sourceId != '') {
					handleSelect($sel_schemaName, $.basePath+'/apiRelease/getSelect.json', {method : 'getSchema', sourceId : sourceId}, {valueField : 'schemaName', textField : 'schemaName'}, '');
				}else{
					handleClearSelect($sel_schemaName);
				}
				updateChanged = true;
			});
			/*
			 * 级联下拉框：数据库名称->表名称
			 */
			$(".sel_schemaName").on("change", function(e) {
				var sourceId = $(this).closest('.form-body').find('.sel_sourceId').val();
				var schemaName = $(this).val();
				var $sel_tabName = $(this).closest('.form-body').find('.sel_tabName');
				$sel_tabName.select2("val", "");
				if (schemaName == '') {
					handleClearSelect($sel_tabName);
				}
				updateChanged = true;
			});
			
			$(".sel_tabName").on("change", function(e) {
				updateChanged = true;
			});
			
			$(document).on('click', '.remove', function(){
				handleRemoveSource();
				
				updateChanged = true;
			});
			
			$(document).on('click', '#portlet_addDataSource', function(){
				handleAddSource();
				
				updateChanged = true;
			});
			
			/*
			 * #portlet_field ul.ms-optgroup li:not(".disabled")配置可选的字段
			 */
			$(document).on('click', '#portlet_field ul.ms-optgroup li:not(".disabled")', function(){
				updateChanged = true;
			});
			
			/*step3参数只读设置_begin*/
			$(document).on('change', '#portlet_fieldConfig .reqArgId', function(){
				var checked = $(this).is(':checked');
				var $reqArgDefltVal = $(this).closest('tr').find('.reqArgDefltVal');
				var $mustType = $(this).closest('tr').find('.mustType');
				if(checked == true){
					$reqArgDefltVal.removeAttr("disabled");
					$mustType.removeAttr("disabled");
				}else{
					$reqArgDefltVal.val('');
					$reqArgDefltVal.attr("disabled","disabled");
					$mustType.val(2).trigger('change');
					$mustType.attr("disabled","disabled");
				}
            });
			$(document).on('change', '#portlet_fieldConfig .respnArgId', function(){
				var checked = $(this).is(':checked');
				var $respnArgSampVal = $(this).closest('tr').find('.respnArgSampVal');
				if(checked == true){
					$respnArgSampVal.removeAttr("disabled");
					
				}else{
					$respnArgSampVal.val('');
					$respnArgSampVal.attr("disabled","disabled");
				}
            });
			$(document).on('change', '#portlet_fieldConfig .mustType', function(){
				var mustTypeVal = $(this).val();
				var $tr = $(this).closest('tr');
				var mustTypeDesc = $(this).attr('mustTypeDesc');
				var $mustOneGrpId = $tr.find('.'+mustTypeDesc+'MustOneGrpId');
				if(mustTypeVal == 1){
					$mustOneGrpId.removeAttr("disabled");
				}else{
					$mustOneGrpId.prop("selectedIndex",0);
					$mustOneGrpId.attr("disabled","disabled");
					
					if(mustTypeVal == 3){//周期字段(每个API只能有一个)
						//将之前选择为“周期字段(每个API只能有一个)”的选择框设置为“可选”
						$('#portlet_fieldConfig .'+mustTypeDesc+'MustType').has('option:selected[value = 3]').not($(this)).prop("selectedIndex",2);
					}
				}
            });
			/*step3参数只读设置_end*/
			
			//step3弹出modal_配置取值范围
			$(document).on('click', "#portlet_fieldConfig .calc_princ", function(e){
				handleCalcPrinc($(this));
			});
			
			//step3配置取值范围_选择option为"不限制"时，取值范围为空且只读
			$(document).on('change', "#sel_calcPrincId_tmp", function(e){
				var $txtarea_valueRange_tmp = $('#txtarea_valueRange_tmp');
				var $row =$txtarea_valueRange_tmp.closest('.row');
				if($(this).prop('selectedIndex') == 0){
					$txtarea_valueRange_tmp.val('');
					$txtarea_valueRange_tmp.attr("disabled","disabled");
					$row.hide();
				}else{
					$txtarea_valueRange_tmp.removeAttr("disabled");
					$txtarea_valueRange_tmp.blur();
					$row.show();
				}
			});
			
			//step3保存_配置取值范围
			$(document).on('click', "#btn_saveCalcPrinc", function(){
				setCalcPrinc();
			});
			
			//step3全选请求参数标识
			$(document).on('click', '#check_all_reqArg', function(){
				var isChecked = $(this).is(':checked');
				$('.reqArgId').prop('checked', isChecked).trigger('change');
			});
			//step3全选响应参数标识
			$(document).on('click', '#check_all_respnArg', function(){
				var isChecked = $(this).is(':checked');
				$('.respnArgId').prop('checked', isChecked).trigger('change');
			});
			
			//step4_保存参数信息
			$(document).on('click', "#btn_apiArgSubmit", function(){
				handleSubmit();
			});
			if(updateFlag == true){
				initStep1ForUpdate();
			}else{
				handleSelect($('#sel_sourceId1'), $.basePath+'/apiRelease/getSelect.json', {method : 'getSource'}, {valueField : 'sourceId', textField : 'sourceDesc'}, '');
			}
		}
	}
}();

jQuery(document).ready(function() { 
	//解决动态添加表单，多个name相同的元素只验证第一个的问题
	/*if ($.validator) {
	    //fix: when several input elements shares the same name, but has different id-ies....
	    $.validator.prototype.elements = function () {

	        var validator = this,
	            rulesCache = {};

	        // select all valid inputs inside the form (no submit or reset buttons)
	        // workaround $Query([]).add until http://dev.jquery.com/ticket/2114 is solved
	        return $([]).add(this.currentForm.elements)
	        .filter(":input")
	        .not(":submit, :reset, :image, [disabled]")
	        .not(this.settings.ignore)
	        .filter(function () {
	            var elementIdentification = this.id || this.name;

	            !elementIdentification && validator.settings.debug && window.console && console.error("%o has no id nor name assigned", this);

	            // select only the first element for each name, and only those with rules specified
	            if (elementIdentification in rulesCache || !validator.objectLength($(this).rules()))
	                return false;

	            rulesCache[elementIdentification] = true;
	            return true;
	        });
	    };
	}*/
	ApiDetail.init(); 
});

function closeWindow(){
	window.opener.closeModalApiItem();
	window.opener=null;
	window.open('','_self');
	window.close();
}