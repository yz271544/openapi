<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE HTML>
<html>
<!-- BEGIN HEAD -->
<head>
    <%@include file="/WEB-INF/views/include/apiRelease/common.jsp"%>
    <title>注册审批</title>
    <script src="${ctx}/js/scripts/admin/right/registAppr.js"></script>
    <style>
        /*page-content_begin*/
        #container_apiArg .form-section {
            margin:10px 0px;
        }
        #tb_regInfo td {
            white-space: nowrap;
        }
        #tb_regInfo td .td-label {
            height: 30px;
            padding: 5px 0px;
            line-height: 1.5;
        }
        .text-white {
            color:#ffffff;
        }
        /*page-content_end*/
    </style>
</head>
<!-- END HEAD -->
<body class="page-container-bg-solid page-boxed">
<!-- BEGIN HEADER -->
<%@include file="/WEB-INF/views/include/admin/header.jsp"%>
<!-- END HEADER -->
<!-- BEGIN CONTENT -->
<div class="page-container container">
    <div class="tab-pane" id="tab_2">
        <div class="portlet box green">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-settings"></i>注册审批
                </div>
                <div class="tools">
                    <a href="javascript:;" class="collapse" title="折叠"> </a> <a
                        href="javascript:;" class="fullscreen" title="全屏"> </a>
                </div>
            </div>
            <div class="portlet-body form">
                <!-- BEGIN FORM-->
                <form action="#" class="form-horizontal" id="form_registSearch">
                    <div class="form-body">
                        <h4 class="form-section">查询区</h4>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="control-label col-md-3">用户名：</label>
                                    <div class="col-md-9">
                                        <input type="text" class="form-control" id="txt_userName"
                                               name="userName">
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="control-label col-md-3">审批状态：</label>
                                    <div class="col-md-9">
                                        <select class="form-control select2" id="sel_examStat"
                                                name="userStatCode">
                                            <option value='-1'>--请选择--</option>
                                        </select>
                                        <!--  <span class="help-block"> Select your gender. </span> -->
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!--/row-->
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="control-label col-md-3">注册开始日期:</label>
                                    <div class="col-md-9">
                                        <input class="form-control form-control-inline date-picker"
                                               id="date_beginDate" name="beginDate" />
                                    </div>
                                </div>
                            </div>
                            <!--/span-->
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="control-label col-md-3">注册结束日期:</label>
                                    <div class="col-md-9">
                                        <input class="form-control form-control-inline date-picker"
                                               id="date_endDate" name="endDate" />
                                    </div>
                                </div>
                            </div>
                            <!--/span-->
                        </div>
                        <!--/row-->
                        <div class="row">
                            <div class="col-md-12">
                                <div class="form-group">
                                    <div class="col-md-3">
                                        <div class="pull-right">
                                            <button type="button" class="btn green" id="btn_search">查询</button>
                                            <button type="button" class="btn default" id="btn_reset">重置</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12">
                                <!-- BEGIN SAMPLE TABLE PORTLET-->
                                <div class="portlet">
                                    <div class="portlet-title">
                                        <div class="caption">结果区</div>
                                    </div>
                                    <div class="portlet-body" id="portlet_body_reg_info">
                                        <div class="table-scrollable">
                                            <table
                                                    class="table table-striped table-bordered table-hover"
                                                    id="tb_regInfo">
                                                <thead>
                                                <tr>
                                                    <th>#</th>
                                                    <th>用户账号</th>
                                                    <th>用户昵称</th>
                                                    <th>手机号</th>
                                                    <th>注册人姓名</th>
                                                    <th>注册时间</th>
                                                    <th>注册类型</th>
                                                    <th>状态</th>
                                                    <th class="text-center">操作</th>
                                                </tr>
                                                </thead>
                                                <tbody>

                                                </tbody>
                                            </table>

                                        </div>
                                        <div id="pg_total" class="pull-left selfer"></div>
                                        <div id="pg_detail" class="pull-right selfer"></div>
                                    </div>
                                </div>
                                <!-- END SAMPLE TABLE PORTLET-->
                            </div>
                        </div>
                    </div>
                </form>
                <!-- END FORM-->
            </div>
        </div>
    </div>
</div>
<!-- /.modal -->


<div id="modal_regApprove" class="modal fade" aria-hidden="false">
    <div class="modal-dialog modal-full">
        <div class="modal-content" id="modal_content_apiApprove">
            <input type="hidden" id="txt_userId_tmp_new">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true"></button>
                <div class="row modal-title font-green" id="">
                    <h4>
                        <i class="icon-settings"></i>&nbsp;&nbsp;注册审批<span
                            id="modal_regApproveTitle"></span>
                    </h4>
                </div>
            </div>
            <div class="modal-body" style="min-height:410px;">
                <div class="portlet box green">
                    <div class="portlet-title">
                        <div class="caption">
                            <span class="caption-subject">审批</span>
                        </div>
                    </div>
                    <div class="portlet-body">
                        <form role="form" id="form_regApprove">
                            <div class="form-body">
                                <div class="form-group">
                                    <label>审批意见:<span
                                            class="required"> * </label>
                                    <textarea id="txt_audit_advc" name="auditAdvc" class="form-control" rows="4"></textarea>
                                </div>
                            </div>
                            <div class="row text-center">
                                <button id='btn_regApproveYes' type="button" class="btn green"
                                        title="审批通过">审批通过</button>
                                <button id='btn_regApproveNo' type="button"
                                        class="btn btn-danger" title="审批驳回">审批驳回</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- END CONTENT -->
<!-- BEGIN FOOTER -->
<%@include file="/WEB-INF/views/include/admin/footer.jsp"%>
<!-- END FOOTER -->
</body>
</html>