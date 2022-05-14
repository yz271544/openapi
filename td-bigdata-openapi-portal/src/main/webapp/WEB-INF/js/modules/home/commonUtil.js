(function($) {
	$.fn.getSelect = function(sel_url, sel_param, sel_field, sel_val) {
		var $sel_obj = $(this);
		$.ajax({
			url : sel_url,
			type : "post",
			data : sel_param,
			dataType : "json",
			success : function(data) {
				if (!showError(data)) {
					$sel_obj.empty();// 清空下拉框
					$sel_obj.append("<option value='-1'>--请选择--</option>");
					$.each(data, function(i, item) {
						var opt_val = item[sel_field.valueField];
						var opt_text = item[sel_field.textField];
						if (sel_val) {
							if (opt_val == sel_val) {
								$sel_obj.append("<option value='" + opt_val
										+ "' selected='selected'>&nbsp;"
										+ opt_text + "</option>");
								//$sel_obj.select2("val", opt_val);
								$sel_obj.val(opt_val).trigger("change");
							} else {
								$sel_obj.append("<option value='" + opt_val
										+ "'>&nbsp;" + opt_text + "</option>");
							}
						} else {
							$sel_obj.append("<option value='" + opt_val
									+ "'>&nbsp;" + opt_text + "</option>");
						}
					});
				}
			},
			error : function(data, e) {
				bootbox.alert("系统错误,请稍候再试！");
			}
		});
	};
	
	$.fn.getFormJson = function() {
		var o = {};
		$sel_disabled = $(this).find('select[disabled = "disabled"]');//$('#'+form_id + ' select[disabled = "disabled"]');
		$sel_disabled.removeAttr("disabled","disabled"); 
		var a = $(this).serializeArray();
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
	
	$.isIE = function(){
		var ua = navigator.userAgent.toLowerCase();
		var s = ua.match(/msie ([\d.]+)/);
		return (s != null);
	};
	
	/*
	 * 说明：选中相同name匹配的多选框
	 * @param name：下拉框名称
	 * @param vals：下拉框值集合数组 [val1, val2...]
	 */
	$.fn.selCheckbox = function(vals){
		var $checkbox = $(this);
		
		$checkbox.prop('checked', false);
		$checkbox.closest('span').removeClass('checked');
		
		$.each(vals, function(i, item){
			$checkbox.filter('[value="'+item+'"]').prop('checked', true);
			$checkbox.filter('[value="'+item+'"]').closest('span').addClass('checked');
		});
	}
	
	/*
	 * 说明：选中相同name所有的多选框
	 * @param name：下拉框名称
	 * @param vals：下拉框值集合数组 [val1, val2...]
	 */
	$.fn.selAllCheckbox = function(){
		var $checkbox = $(this);
		
		$checkbox.prop('checked', true);
		$checkbox.closest('span').addClass('checked');
	}
})(jQuery);
