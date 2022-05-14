var RegistAppr = function () {

    //加载修改和查看Apply的API权限树
    var loadApiAuthTree = function () {
        var applyId = $('#txt_applyId_tmp_new').val();
        var userId = $('#txt_userId_tmp_new').val();
        var param = {
            applyId: applyId,
            userId: userId
        };
        $.ajax({
            "type": "post",
            "url": $.basePath + "/admin/right/getUnioAPIAuthApplyTree",
            data: $.extend({}, param),
            "dataType": "json",
            "success": function (resp) {
                //alert(resp);
                var obj1 = eval(resp.ugtree);
                //使用jstree初始化tree
                //先销毁当前树对象
                $('#ApiAuthTree').jstree("destroy");
                $('#ApiAuthTree').empty().append("<ul>" + showResAuthTree(obj1) + "</ul>");
                $('#ApiAuthTree').jstree({
                    "core": {
                        "themes": {
                            "responsive": false
                        },
                        "multiple": true
                    },
                    "types": {
                        "default": {
                            "icon": "fa fa-folder icon-state-warning icon-lg"
                        },
                        "file": {
                            "icon": "fa fa-file icon-state-warning icon-lg"
                        }
                    },
                    //"plugins": ["wholerow", "checkbox", "types","themes", "html_data", "ui","state"],
                    "plugins": ["checkbox"],
                    "checkbox": {
                        "undetermined": false,
                        "three_state": true
                    }
                });

            }
        });
    };

    var showResAuthTree = function (treeJsonStr) {
        var output = '';
        var editDisable = $('#txt_treeDisabled_tmp_new').val();
        for (var node = 0; node < treeJsonStr.length; node++) {
            if (treeJsonStr[node].childrens.length > 0 || treeJsonStr[node].apiVersion < 0) {
                var childrenHtml = showResAuthTree(treeJsonStr[node].childrens);
                output += '<li data-jstree=\'{"opened\" : true}\' nodeid="' + treeJsonStr[node].id + '" nodeName="' + treeJsonStr[node].name + '" nodeBelong="' + treeJsonStr[node].rightMain + '">' + treeJsonStr[node].name + '<ul>' + childrenHtml + '</ul></li>\n';
            } else {
                if (node == 0) {
                    if (treeJsonStr[node].rightFieldValue != null && treeJsonStr[node].rightFieldValue == treeJsonStr[node].id && (treeJsonStr[node].rightMain == 'group' || treeJsonStr[node].rightMain == 'user')) {
                        output += '<li data-jstree=\'{"type\" : \"file\" ,\"selected\" : true,\"disabled\": true}\' nodeid="' + treeJsonStr[node].id + '" nodeName="' + treeJsonStr[node].name + '" nodeBelong="' + treeJsonStr[node].rightMain + '"><a href="javascript:;">' + treeJsonStr[node].name + '('+treeJsonStr[node].apiDesc+') Version:' + treeJsonStr[node].apiVersion + '</a></li>\n';
                    } else if (treeJsonStr[node].rightFieldValue != null && treeJsonStr[node].rightFieldValue == treeJsonStr[node].id && treeJsonStr[node].rightMain == 'apply') {
                        if (editDisable == 'true') {
                            output += '<li data-jstree=\'{"type\" : \"file\" ,\"selected\" : true,\"disabled\": true }\' nodeid="' + treeJsonStr[node].id + '" nodeName="' + treeJsonStr[node].name + '" nodeBelong="' + treeJsonStr[node].rightMain + '"><font color="blue"><a href="javascript:;">' + treeJsonStr[node].name + '('+treeJsonStr[node].apiDesc+') Version:' + treeJsonStr[node].apiVersion + '</a></font></li>\n';
                        } else {
                            output += '<li data-jstree=\'{"type\" : \"file\" ,\"selected\" : true,\"disabled\": false }\' nodeid="' + treeJsonStr[node].id + '" nodeName="' + treeJsonStr[node].name + '" nodeBelong="' + treeJsonStr[node].rightMain + '"><font color="blue"><a href="javascript:;">' + treeJsonStr[node].name + '('+treeJsonStr[node].apiDesc+') Version:' + treeJsonStr[node].apiVersion + '</a></font></li>\n';
                        }
                    } else {
                        if (editDisable == 'true') {
                            output += '<li data-jstree=\'{"type\" : \"file\" ,\"disabled\": true }\' nodeid="' + treeJsonStr[node].id + '" nodeName="' + treeJsonStr[node].name + '" nodeBelong="' + treeJsonStr[node].rightMain + '"><a href="javascript:;">' + treeJsonStr[node].name + '('+treeJsonStr[node].apiDesc+') Version:' + treeJsonStr[node].apiVersion + '</a></li>\n';
                        } else {
                            output += '<li data-jstree=\'{"type\" : \"file\" ,\"disabled\": false }\' nodeid="' + treeJsonStr[node].id + '" nodeName="' + treeJsonStr[node].name + '" nodeBelong="' + treeJsonStr[node].rightMain + '"><a href="javascript:;">' + treeJsonStr[node].name + '('+treeJsonStr[node].apiDesc+') Version:' + treeJsonStr[node].apiVersion + '</a></li>\n';
                        }
                    }
                } else {
                    if (treeJsonStr[node].rightFieldValue != null && treeJsonStr[node].rightFieldValue == treeJsonStr[node].id && (treeJsonStr[node].rightMain == 'group' || treeJsonStr[node].rightMain == 'user')) {
                        output += '<li data-jstree=\'{"type\" : \"file\",\"selected\" : true,\"disabled\": true}\' nodeid="' + treeJsonStr[node].id + '" nodeName="' + treeJsonStr[node].name + '" nodeBelong="' + treeJsonStr[node].rightMain + '"><a href="javascript:;">' + treeJsonStr[node].name + '('+treeJsonStr[node].apiDesc+') Version:' + treeJsonStr[node].apiVersion + '</a></li>\n';
                    } else if (treeJsonStr[node].rightFieldValue != null && treeJsonStr[node].rightFieldValue == treeJsonStr[node].id && treeJsonStr[node].rightMain == 'apply') {
                        if (editDisable == 'true'){
                            output += '<li data-jstree=\'{"type\" : \"file\",\"selected\" : true,\"disabled\": true }\' nodeid="' + treeJsonStr[node].id + '" nodeName="' + treeJsonStr[node].name + '" nodeBelong="' + treeJsonStr[node].rightMain + '"><font color="blue"><a href="javascript:;">' + treeJsonStr[node].name + '('+treeJsonStr[node].apiDesc+') Version:' + treeJsonStr[node].apiVersion + '</a></font></li>\n';
                        }else{
                            output += '<li data-jstree=\'{"type\" : \"file\",\"selected\" : true,\"disabled\": false }\' nodeid="' + treeJsonStr[node].id + '" nodeName="' + treeJsonStr[node].name + '" nodeBelong="' + treeJsonStr[node].rightMain + '"><font color="blue"><a href="javascript:;">' + treeJsonStr[node].name + '('+treeJsonStr[node].apiDesc+') Version:' + treeJsonStr[node].apiVersion + '</a></font></li>\n';
                        }
                    } else {
                        if (editDisable == 'true'){
                            output += '<li data-jstree=\'{"type\" : \"file\",\"disabled\": true }\' nodeid="' + treeJsonStr[node].id + '" nodeName="' + treeJsonStr[node].name + '" nodeBelong="' + treeJsonStr[node].rightMain + '"><a href="javascript:;">' + treeJsonStr[node].name + '('+treeJsonStr[node].apiDesc+') Version:' + treeJsonStr[node].apiVersion + '</a></li>\n';
                        }else{
                            output += '<li data-jstree=\'{"type\" : \"file\",\"disabled\": false }\' nodeid="' + treeJsonStr[node].id + '" nodeName="' + treeJsonStr[node].name + '" nodeBelong="' + treeJsonStr[node].rightMain + '"><a href="javascript:;">' + treeJsonStr[node].name + '('+treeJsonStr[node].apiDesc+') Version:' + treeJsonStr[node].apiVersion + '</a></li>\n';
                        }
                    }
                }
            }
        }
        return output;
    };


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
    var handlePopover = function () {
        $('.popovers').popover();
    };
    var handleConfirm = function () {
        $('.confirmation').confirmation({
         container : 'body',
         btnOkClass : 'btn btn-sm btn-success',
         btnCancelClass : 'btn btn-sm btn-danger'
         });
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
    /*
     * 获取唯一id
     */
    var generateId = function () {
        var url = $.basePath + '/admin/right/generateId.json';
        var id;
        $.ajax({
            url: url,
            type: "post",
            //data : param,
            async: false,
            dataType: "json",
            success: function (data) {
                if (!showError(data)) {
                    id = data.id;
                }
            },
            error: function (data, e) {
                bootbox.alert("系统错误,请稍候再试！");
            }
        });
        return id;
    };

    /*
     * 获取UserId
     */
    var getUserId = function () {
        var url = $.basePath + '/admin/right/getUserId.json';
        var userid;
        $.ajax({
            url: url,
            type: "post",
            //data : param,
            async: false,
            dataType: "json",
            success: function (data) {
                if (!showError(data)) {
                    userid = data.userId;
                }
            },
            error: function (data, e) {
                bootbox.alert("系统错误,请稍候再试！");
            }
        });
        return userid;
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


    var handleMaster = function () {
        var data_url = $.basePath + '/admin/right/getRightApplyMaster.json';
        //请求数据
        $.ajax({
            url: data_url,      //提交到一般处理程序请求数据
            type: "POST",
            dataType: "json",
            success: function (data) {
                //alert(JSON.stringify(data));
                $('#txt_loginAcct').val(data.loginAcct);        //移除Id为Result的表格里的行，从第二行开始（这里根据页面布局不同页变）
            },
            error: function (data, e) {
                bootbox.alert("系统错误,请稍候再试！");
            }
        });
    };

    var pageIndex = 0; //页面索引初始值
    var pageSize = 5; //每页显示条数初始化，修改显示条数，修改这里即可
    var handlePagination = function () {
        var form_id = 'form_authSearch';
        var pg_detail = 'pg_detail';
        var pg_total = 'pg_total';
        var tb_id = 'tb_authInfo';
        var data_url = $.basePath + '/admin/right/getRightApplyInfoInfo.json';
        var loginAcct = $('#txt_loginAcct').val();
        var auditStat = $("#sel_auditStat").find("option:selected").val();

        var data_param = {loginAcct: loginAcct, auditStat: auditStat};

        //alert("查询条件: loginAcct:" + data_param.loginAcct + " auditStat : " + data_param.auditStat);
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
                current_page: pageIndex //当前页索引
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
                target: '#portlet_body_auth_info',
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
                    //alert(JSON.stringify(data));
                    totalCount = data.totalCount;
                    pageInit(totalCount, pageIndex);
                    $('#' + tb_id + ' tr:gt(0)').remove();        //移除Id为Result的表格里的行，从第二行开始（这里根据页面布局不同页变）
                    $.each(data.items, function (i, item) {
                        var login_acct = item.loginAcct;
                        var auditStat = item.auditStat;			//0未审批1通过2驳回
                        //alert("fetch data:applyId:" + item.applyId + " login_acct: " + login_acct + " auditStat:" + auditStat);
                        var hide_btn_saveApplyItem = (auditStat == '-1' ? '' : ' hide '); //待发布状态才可修改
                        var hide_btn_releaseApplyItem = (auditStat == '-1' ? '' : ' hide '); //待发布状态才可发布
                        var hide_btn_deleteApplyItem = (auditStat == '-1' ? '' : ' hide '); //待发布状态才可删除
                        //alert("auditStat:" + auditStat + " hide_btn_saveApplyItem:" + hide_btn_saveApplyItem + " hide_btn_releaseApplyItem:" + hide_btn_releaseApplyItem);
                        var td_prefix = '<td><div class="td-label">';
                        var td_suffix = '</div></td>';
                        var sb = new StringBuffer();
                        sb.append('<tr>');
                        sb.append(td_prefix + (itemNum++) + td_suffix);
                        sb.append(td_prefix + item.applyId + td_suffix);
                        sb.append(td_prefix + item.loginAcct + td_suffix);
                        sb.append(td_prefix + item.applyTimestamp + td_suffix);
                        if (auditStat == 1) {
                            sb.append('<td><div class="td-label text-white text-center popovers label-success" data-container="body" data-trigger="hover" data-placement="top" data-content="' + item.auditAdvc + '" data-original-title="审批意见">' + item.auditStatDesc + '</div></td>');
                        } else if (auditStat == 2) {
                            sb.append('<td><div class="td-label text-white text-center popovers label-danger" data-container="body" data-trigger="hover" data-placement="top" data-content="' + item.auditAdvc + '" data-original-title="审批意见">' + item.auditStatDesc + '</div></td>');
                        } else {
                            sb.append(td_prefix + item.auditStatDesc + td_suffix);
                        }
                        sb.append('<td');
                        sb.append(' apply_id="' + item.applyId + '"');
                        sb.append(' user_id="' + item.userId + '"');
                        sb.append(' login_acct="' + item.loginAcct + '"');
                        sb.append(' audit_stat="' + item.auditStat + '"');
                        sb.append('>');
                        sb.append('<a href="javascript:;" class="btn green btn-sm btn-outline sbold uppercase btn_seeApplyItem toolTip" data-toggle="modal" data-target="#modal_applyItem" action="readApplyItem" title="查看">');
                        sb.append('<i class="fa fa-search"></i></a>');
                        sb.append('<a href="javascript:;" class="btn green btn-sm btn-outline sbold uppercase btn_updateApplyItem toolTip' + hide_btn_saveApplyItem + '" data-toggle="modal" data-target="#modal_applyItem" action="saveApplyItem" title="修改">');
                        sb.append('<i class="fa fa-edit"></i></a>');
                        // sb.append('<a href="javascript:;" class="btn green btn-sm btn-outline sbold uppercase btn_releaseApplyItem toolTip' + hide_btn_releaseApplyItem + '" data-toggle="modal" data-target="#modal_applyRelease" title="发布">');
                        sb.append('<a href="javascript:;" class="btn green green-sharp btn-large btn-sm btn-outline sbold uppercase btn_releaseApplyItem confirmation toolTip' + hide_btn_releaseApplyItem + '" data-toggle="confirmation" data-original-title="你确定要发布吗?" title="你确定要发布吗?">');
                        sb.append('<i class="fa fa-plus"></i></a>');
                        sb.append('<a href="javascript:;" class="btn green green-sharp btn-large btn-sm btn-outline sbold uppercase btn_deleteApplyItem confirmation toolTip' + hide_btn_deleteApplyItem + '" data-toggle="confirmation" data-original-title="你确定要删除吗?" title="你确定要删除吗?">');
                        sb.append('<i class="fa fa-remove"></i></a>');

                        sb.append('</td>');
                        sb.append('</tr>');
                        $('#' + tb_id + ' tbody').append(sb.toStr());
                        handleToolTip();
                        handlePopover();
                        handleConfirm();
                        /*$('[data-toggle=confirmation]').confirmation({
                            container : 'body',
                            btnOkClass : 'btn btn-sm btn-success',
                            btnCancelClass : 'btn btn-sm btn-danger'
                        });*/
                    });

                    App.unblockUI('#portlet_body_auth_info');
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
     * applyItem模态框
     */
    var handleModalApplyItem = function ($target) {
        var action = $target.attr('action');
        //alert("handleModalApplyItem:" + action);
        var modal_authApplyTitle = 'modal_authApplyTitle';
        var ApiAuthTree = 'ApiAuthTree';
        switch (action) {
            case 'addApplyItem':
                var applyId = generateId();
                $('#' + modal_authApplyTitle).text('新增申请');
                $('#txt_applyId_tmp').val(applyId);
                var userId = getUserId();
                $('#txt_userId_tmp_new').val(userId);
                var editDisableTmp = 'false';
                $('#txt_treeDisabled_tmp_new').val(editDisableTmp);
                $('#btn_saveApplyItem').show();
                $('#btn_cancel').show();


                break;
            case 'saveApplyItem':
                var applyId = $target.parent().attr('apply_id');
                $('#' + modal_authApplyTitle).text('修改申请');
                $('#txt_applyId_tmp').val(applyId);
                var editDisableTmp = 'false';
                $('#txt_treeDisabled_tmp_new').val(editDisableTmp);
                $('#btn_saveApplyItem').show();
                $('#btn_cancel').show();
                break;
            case 'readApplyItem':
                var applyId = $target.parent().attr('apply_id');
                $('#' + modal_authApplyTitle).text('查看申请');
                $('#txt_applyId_tmp').val(applyId);
                var editDisableTmp = 'true';
                $('#txt_treeDisabled_tmp_new').val(editDisableTmp);
                $('#btn_saveApplyItem').hide();
                $('#btn_cancel').show();
                break;
            default:
        }
        loadApiAuthTree();
    };

    /*
     * 删除API信息
     */
    var handleDelete = function($target){
        var portlet_body = 'portlet_body_auth_info';
        var url = $.basePath+'/admin/right/delApply.action';
        var applyId = $target.parent().attr('apply_id');
        var param = {
            applyId : applyId
        };
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
    };
    /*
     * 发布API信息
     */
    var doAuthRelease = function ($target) {
        var applyId = $target.parent().attr('apply_id');
        var auditStat = 0;
        //alert("doAuthRelease---->applyId:" + applyId + " auditStat:" + auditStat);
        var url = $.basePath + '/admin/right/doAuthRelease.action';
        var param = {
            applyId: applyId,
            auditStat: auditStat
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
                        // $('#modal_applyRelease').modal('hide');
                    });
                }
            },
            error: function (data, e) {
                bootbox.alert("dangqian系统错误,请稍候再试！");
            }
        });
    };

    return {
        init: function () {
            handleSelect2('form_authSearch');
            handleMaster();
            handleDatePickers();
            handlePagination();
            handleToolTip();
            handleSelect($('#sel_auditStat'), $.basePath + '/admin/right/getSelect.json', {method: 'getAuthAuditStat'}, {
                valueField: 'codeId',
                textField: 'codeDesc'
            }, '');

/*            $('body').on('show.bs.modal', '#modal_applyItem', function (e) {
                //$('#ApiAuthTree')[0].reset();	//表单重置
                //$("#ApiAuthTree").validate().resetForm();//表单校验重置

            });*/

            $('body').on('click', '.btn_releaseApplyItem', function(){
                doAuthRelease($(this));
            });

            $('body').on('click', '.btn_deleteApplyItem', function(){
                handleDelete($(this));
            });

            $('body').on('click', '#btn_search', function () {
                handlePagination();
            });
            $('body').on('click', '#btn_reset', function () {
                $('#form_authSearch')[0].reset();
            });
            /*
             * 展示Reg审批modal
             * modal展示前清空表单
             */
            $('body').on('show.bs.modal', '#modal_applyItem', function (e) {
                var $target = $(e.relatedTarget);
                var applyId = $target.parent().attr('apply_id');
                var userId = $target.parent().attr('user_id');
                var apply_id = 'txt_applyId_tmp_new';
                var user_id = 'txt_userId_tmp_new';
                var tab_id = 'tb_authApprove';
                var audit_id = 'txt_audit_advc';
                $('#' + apply_id).val(applyId);
                $('#' + user_id).val(userId);
                $('#' + tab_id + ' tr:gt(0)').remove();
                $('#' + audit_id).val('');
                handleModalApplyItem($target);
            });

            $('body').on('show.bs.modal', '#modal_applyRelease', function (e) {
                var $target = $(e.relatedTarget);
                var applyId = $target.parent().attr('apply_id');
                var apply_id = 'release_applyId_tmp';
                $('#' + apply_id).val(applyId);
            });

            $('body').on('click', '#btn_applyReleaseYes', function () {
                doAuthRelease('0');
            });
            $('body').on('click', '#btn_applyReleaseCancel', function () {
                $('#modal_applyRelease').modal('hide');
            });

            $('body').on('click', '#btn_saveApplyItem', function () {
                var applyId = $('#txt_applyId_tmp').val();
                var checkedApiTree = $('#ApiAuthTree').jstree().get_checked();//选中的API树节点id
                var allApiId = "";

                for (var i = 0; i < checkedApiTree.length; i++) {
                    var nodeBelong = $('#' + checkedApiTree[i]).attr("nodebelong");
                    //alert("------"+checkedApiTree[i] + " : " +  nodeBelong);

                    if (nodeBelong == 'undefined') {
                        var apiId = $('#' + checkedApiTree[i]).attr("nodeid");
                        if (i == checkedApiTree.length - 1) {
                            allApiId += apiId;
                        } else {
                            allApiId += apiId + ",";
                        }
                    }
                }
                var btn_search = 'btn_search';
                var portlet_body = 'modal_applyItem';
                App.blockUI({
                    target: '#'+portlet_body,
                    message: '执行中...'
                });

                $.ajax({
                    type: "post",
                    url: "/openapi/admin/right/registApiAuth",
                    data: {
                        applyId: applyId,
                        allApiId: allApiId
                    },
                    dataType: "text",
                    success: function (resp) {
                        //alert(resp);
                        if (!showError(resp)) {
                            // App.unblockUI('#'+portlet_body);
                            bootbox.alert("操作成功");
                            $('#'+btn_search).trigger('click');
                            $('#modal_applyItem').modal('hide');
                            App.unblockUI('#portlet_body_auth_info');
                        }
                    },
                    error : function(resp, e) {
                        App.unblockUI('#'+portlet_body);
                        bootbox.alert("系统错误,请稍候再试！");
                    }
                });
            });
        }
    };
}();

jQuery(document).ready(function () {
    RegistAppr.init();
});
