var RegistAppr = function () {
    var handleSelect2 = function (formId) {
        $('#' + formId + ' select.select2').select2({
            width: null,
            language: 'zh-CN',
            minimumResultsForSearch: -1,
            dropdownCss: {
                'z-index': 19999
            }
        });
    };
    var handleClearSelect2 = function (formId) {
        /*替换$('.select2').select2('val', '');这样就不会触发select的change事件（这样导致validate表单信息）*/
        $('#' + formId + ' select.select2').each(function (i, obj) {
            var select_id = $(this).attr('id');
            var select2_id = 'select2-' + select_id + '-container';
            $('#' + select2_id).text('--请选择--');
            $('#' + select2_id).attr('title', '--请选择--');
        });
    };
    var handleToolTip = function () {
        $('.toolTip').tooltip();
    };
    var handlePopover = function(){
        $('.popovers').popover();
    };
    var handleDatePickers = function () {
        $('.date-picker').datepicker({
            rtl: App.isRTL(),
            orientation: "left",
            autoclose: true,
            format: "yyyy-mm-dd",
            language: "zh-CN"
        });
    };

    //获取form表单参数
    var getFormJson = function (form_id) {
        var o = {};
        $sel_disabled = $('#' + form_id + ' select[disabled = "disabled"]');
        $sel_disabled.removeAttr("disabled", "disabled");
        var a = $('#' + form_id).serializeArray();
        $sel_disabled.attr("disabled", "disabled");
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
    };

    var pageIndex = 0; //页面索引初始值
    var pageSize = 5; //每页显示条数初始化，修改显示条数，修改这里即可
    var handlePagination = function () {
        var form_id = 'form_registSearch';
        var pg_detail = 'pg_detail';
        var pg_total = 'pg_total';
        var tb_id = 'tb_regInfo';
        var data_url = $.basePath + '/registAppr/getRegInfo.json';
        var loginAcct = $('#txt_userName').val();
        var userStatCode = $("#sel_examStat").find("option:selected").val();
        var data_param = {loginAcct: loginAcct, userStatCode: userStatCode};

        // alert(" loginAcct:" + data_param.loginAcct + " userStatCode : " + data_param.userStatCode);
        initTable(0); //Load事件，初始化表格数据，页面索引为0（第一页）

        function pageInit(totalCount, pageIndex) {
            //分页，totalCount是总条目数，这是必选参数，其它参数都是可选
            $('#' + pg_detail).pagination(totalCount, {
                callback: pageCallback, //PageCallback() 为翻页调用次函数。
                prev_text: "« 上一页",
                next_text: "下一页 »",
                items_per_page: pageSize,
                num_edge_entries: 1, //两侧首尾分页条目数
                num_display_entries: 6, //连续分页主体部分分页条目数
                current_page: pageIndex, //当前页索引
            });

            var startIndex = (pageIndex * pageSize + 1);
            var endIndex = (pageIndex + 1) * pageSize > totalCount ? totalCount : (pageIndex + 1) * pageSize;
            $('#' + pg_total).html("当前第&nbsp;" + startIndex + '-' + endIndex + '&nbsp;条&nbsp;&nbsp;共计&nbsp;' + totalCount + '&nbsp;条');
        }

        //翻页调用
        function pageCallback(index, jq) {
            initTable(index);
        }

        //请求数据
        function initTable(pageIndex) {
            App.blockUI({
                target: '#portlet_body_reg_info',
                message: '加载中...'
            });
            var totalCount;//总条目数
            var itemNum = pageIndex * pageSize + 1;//当前页开始记录行数
            $.ajax({
                url: data_url,      //提交到一般处理程序请求数据
                type: "POST",
                //分页参数：pageIndex(页面索引)，pageSize(显示条数)
                data: $.extend({}, data_param, getFormJson(form_id), {'pageIndex': pageIndex, 'pageSize': pageSize}),
                dataType: "json",
                success: function (data) {
                    //alert("data_param.userStatCode:" + data_param.userStatCode);
                   //   alert(JSON.stringify(data));
                    totalCount = data.totalCount;
                    pageInit(totalCount, pageIndex);
                    $('#' + tb_id + ' tr:gt(0)').remove();        //移除Id为Result的表格里的行，从第二行开始（这里根据页面布局不同页变）
                    $.each(data.items, function (i, item) {
                        var login_acct = item.loginAcct;
                        var userStatCode = item.userStatCode;			//0未审批1通过2驳回
                        // alert("item.userStatCode:" + item.userStatCode + " item.userStatDesc:" + item.userStatDesc);
                        var td_prefix = '<td><div class="td-label">';
                        var td_suffix = '</div></td>';
                        var sb = new StringBuffer();
                        sb.append('<tr>');
                        sb.append(td_prefix + (itemNum++) + td_suffix);
                        sb.append(td_prefix + item.loginAcct + td_suffix);
                        sb.append(td_prefix + item.userName + td_suffix);
                        sb.append(td_prefix + item.phone + td_suffix);
                        sb.append(td_prefix + item.registPersn + td_suffix);
                        sb.append(td_prefix + item.registTime + td_suffix);
                        sb.append(td_prefix + item.userTypeDesc + td_suffix);
                        if(userStatCode == 1){
                            sb.append('<td> <div class="td-label text-white text-center popovers label-success" data-container="body" data-trigger="hover" data-placement="top" data-content="'+item.auditAdvc+'" data-original-title="审批意见">'+item.userStatDesc+'</div></td>');
                        }else if(userStatCode == 2){
                            sb.append('<td> <div class="td-label text-white text-center popovers label-danger" data-container="body" data-trigger="hover" data-placement="top" data-content="'+item.auditAdvc+'" data-original-title="审批意见">'+item.userStatDesc+'</div></td>');
                        }else{
                            sb.append(td_prefix+item.userStatDesc+td_suffix);
                        }
                        sb.append('<td');
                        sb.append(' user_id ="' + item.userId + '"');
                        sb.append(' login_acct ="' + item.loginAcct + '"');
                        sb.append(' userName ="' + item.userName + '"');
                        sb.append(' phone ="' + item.phone + '"');
                        sb.append(' regist_persn ="' + item.registPersn + '"');
                        sb.append(' regist_time ="' + item.registTime + '"');
                        sb.append(' user_type_code ="' + item.userTypeCode + '"');
                        sb.append(' user_stat_code ="' + item.userStatCode + '"');
                        sb.append('>');
                        if (userStatCode == 0) {
                            sb.append('<a href="javascript:;" class="btn green btn-sm btn-outline sbold uppercase btn_apiApprove toolTip" data-toggle="modal" data-target="#modal_regApprove" action="readApiItem" title="审批">');
                        }
                        sb.append('<i class="fa fa-check"></i> 审批 </a>');
                        sb.append('</td>');
                        sb.append('</tr>');
                        $('#' + tb_id + ' tbody').append(sb.toStr());
                        handleToolTip();
                        handlePopover();
                    });

                    App.unblockUI('#portlet_body_reg_info');
                },
                error: function (data, e) {
                    bootbox.alert("系统错误,请稍候再试！");
                }
            });
        }
    };

    /*
     * 获取选择框
     * sel_id:选择框id
     * sel_url:选择url
     * sel_param:选择参数
     * sel_field:要获取的字段
     * sel_val:选择项
     */
    var handleSelect = function ($sel_obj, sel_url, sel_param, sel_field, sel_val) {
        // 绑定Ajax的内容
        $.ajax({
            url: sel_url,
            type: "post",
            data: sel_param,
            dataType: "json",
            success: function (data) {
                if (!showError(data)) {
                    $sel_obj.empty();// 清空下拉框
                    $sel_obj.append("<option value='-1'>--请选择--</option>");
                    $.each(data, function (i, item) {
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
            error: function (data, e) {
                bootbox.alert("系统错误,请稍候再试！");
            }
        });
    };
    /*
     * 修改Reg审核信息
     */
    var handleUpdate = function () {
        var modal_content = 'modal_content_apiItem';
        var url = $.basePath + '/apiRelease/modifyApiItem.action';
        var form_id = 'form_apiItem';
        var param = getFormJson(form_id);
        var btn_search = 'btn_search';
        App.blockUI({
            target: '#' + modal_content,
            message: '执行中...'
        });

        // 绑定Ajax的内容
        $.ajax({
            url: url,
            type: "post",
            data: param,
            dataType: "json",
            success: function (data) {
                App.unblockUI('#' + modal_content);
                if (!showError(data)) {
                    $('#' + btn_search).trigger('click');
                    bootbox.alert("操作成功");
                }
            },
            error: function (data, e) {
                App.unblockUI('#' + modal_content);
                bootbox.alert("系统错误,请稍候再试！");
            }
        });
    };
    /*
     * 审批注册用户（0未审批 1：审批通过（已审批） 2：审批驳回）
     */
    var doRegApprove = function (auditUserStat) {
        var userId = $('#txt_userId_tmp_new').val();
        var auditAdvc = $('#txt_audit_advc').val();
        var url = $.basePath + '/registAppr/doRegApprove.action';
        var param = {
            userId : userId,
            userStat : auditUserStat,
            auditAdvc : auditAdvc
        };
        $.ajax({
            url: url,
            type: "post",
            data: param,
            dataType: "json",
            success: function (data) {
                if (!showError(data)) {
                    bootbox.alert("操作成功", function () {
                        $('#btn_search').trigger('click');
                        $('#modal_regApprove').modal('hide');
                    });
                }
            },
            error: function (data, e) {
                bootbox.alert("系统错误,请稍候再试！");
            }
        });
    };

    return {
        init: function () {
            handleSelect2('form_registSearch');
            handleDatePickers();
            handlePagination();
            handleToolTip();
            handleSelect($('#sel_examStat'), $.basePath + '/registAppr/getSelect.json', {method: 'getRegExamStat'}, {
                valueField: 'codeId',
                textField: 'codeDesc'
            }, '');


            $('body').on('click', '#btn_search', function () {
                handlePagination();
            });
            $('body').on('click', '#btn_reset', function () {
                $('#form_registSearch')[0].reset();
            });
            /*
             * 展示Reg审批modal
             * modal展示前清空表单
             */
            $('body').on('show.bs.modal', '#modal_regApprove', function (e) {
             var $target = $(e.relatedTarget);
             var userId = $target.parent().attr('user_id');
             var user_id = 'txt_userId_tmp_new';
             var tab_id = 'tb_regApprove';
             var audit_id = 'txt_audit_advc';

             $('#'+user_id).val(userId);
             $('#'+tab_id +' tr:gt(0)').remove();
             $('#'+audit_id).val('');
             });
            /*
             * 展示api审批modal
             * modal展示后加载表单数据 
             */
            $('body').on('click', '#btn_regApproveYes', function () {
                var ifValid = $('#form_regApprove').valid();
                if (ifValid) {
                    doRegApprove('1');
                }
            });
            $('body').on('click', '#btn_regApproveNo', function () {
                var ifValid = $('#form_regApprove').valid();
                if (ifValid) {
                    doRegApprove('2');
                }
            });
        }
    };
}();

jQuery(document).ready(function () {
    RegistAppr.init();
});
