<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/include/admin/taglib.jsp"%>
<html>
    <!-- BEGIN HEAD -->
    <head>
        <%@include file="/WEB-INF/views/include/admin/common.jsp"%>
        <script src="${ctx}/js/plugins/bootstrap-wizard/jquery.bootstrap.wizard.min.js" type="text/javascript"></script>
        <script src="${ctx}/js/scripts/form-wizard.min.js" type="text/javascript"></script>
        <script src="${ctx}/js/plugins/jstree/dist/jstree.min.js" type="text/javascript"></script>
        <script src="${ctx}/js/scripts/ui-tree.js" type="text/javascript"></script>
        <script src="${ctx}/js/scripts/admin/apiUser.js" type="text/javascript"></script>
        <title>API使用者注册页面</title>
    </head>
    <!-- END HEAD -->
    <body>
    <div class="page-header" style="height: 75px;">
        <div class="page-header-top">
            <div class="container">
                <!-- BEGIN LOGO -->
                <div class="page-logo">
                    <a href="index.html"> <img src="${ctx }/img/logo-default.jpg" alt="logo" class="logo-default" /></a>
                </div>
                <!-- END LOGO -->
                <!-- BEGIN RESPONSIVE MENU TOGGLER -->
                <a href="javascript:;" class="menu-toggler"></a>
                <!-- END RESPONSIVE MENU TOGGLER -->
                <!-- BEGIN TOP NAVIGATION MENU -->
                <div class="top-menu">
                </div>
                <!-- END TOP NAVIGATION MENU -->
            </div>
        </div>

    </div>

    <div class="page-container">
        <div class="page-content-wrapper">
            <div class="page-content">
                <div class="container">
                    <!-- BEGIN PAGE CONTENT INNER -->
                    <div class="page-content-inner">
                        <div class="row">
                            <div class="col-md-12">
                                <div class="portlet light " id="form_wizard_1">

                                    <div class="portlet-title">
                                        <div class="caption">
                                            <i class=" icon-layers font-red"></i>
                                                <span class="caption-subject font-red bold uppercase"> API使用者账号注册 -
                                                    <span class="step-title"> 第一步 </span>
                                                </span>
                                        </div>
                                    </div>
                                    <div class="portlet-body form">
                                        <form class="form-horizontal" action="#" id="submit_form" method="POST">
                                            <div class="form-wizard">
                                                <div class="form-body">
                                                    <ul class="nav nav-pills nav-justified steps">
                                                        <li>
                                                            <a href="#tab1" data-toggle="tab" class="step">
                                                                <span class="number"> 1 </span>
                                                                    <span class="desc">
                                                                        <i class="fa fa-check"></i> 设置用户名 </span>
                                                            </a>
                                                        </li>
                                                        <li>
                                                            <a href="#tab2" data-toggle="tab" class="step">
                                                                <span class="number"> 2 </span>
                                                                    <span class="desc">
                                                                        <i class="fa fa-check"></i> 填写应用信息 </span>
                                                            </a>
                                                        </li>
                                                        <li>
                                                            <a href="#tab3" data-toggle="tab" class="step active">
                                                                <span class="number"> 3 </span>
                                                                    <span class="desc">
                                                                        <i class="fa fa-check"></i> 设置权限信息 </span>
                                                            </a>
                                                        </li>
                                                        <li>
                                                            <a href="#tab4" data-toggle="tab" class="step">
                                                                <span class="number"> 4 </span>
                                                                    <span class="desc">
                                                                        <i class="fa fa-check"></i> 注册审核 </span>
                                                            </a>
                                                        </li>
                                                    </ul>
                                                    <div id="bar" class="progress progress-striped" role="progressbar">
                                                        <div class="progress-bar progress-bar-success"> </div>
                                                    </div>
                                                    <div class="tab-content">
                                                        <div class="alert alert-danger display-none">
                                                            <button class="close" data-dismiss="alert"></button> You have some form errors. Please check below. </div>
                                                        <div class="alert alert-success display-none">
                                                            <button class="close" data-dismiss="alert"></button> Your form validation is successful! </div>

                                                        <div class="tab-pane active" id="tab1">
                                                            <h3 class="block">输入账号详细信息</h3>
                                                            <div class="form-group">
                                                                <label class="control-label col-md-3">登录账号
                                                                    <span class="required"> * </span>
                                                                </label>
                                                                <div class="col-md-4">
                                                                    <input type="text" class="form-control" name="loginAcct" id="loginAcct"/>
                                                                    <span class="help-block"> 请输入登录账号名称 </span>
                                                                </div>
                                                            </div>
                                                            <%--<div class="form-group">
                                                                <label class="control-label col-md-3">输入密码
                                                                    <span class="required"> * </span>
                                                                </label>
                                                                <div class="col-md-4">
                                                                    <input type="password" class="form-control" name="password" id="submit_form_password" />
                                                                    <span class="help-block"> 请输入账号密码. </span>
                                                                </div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="control-label col-md-3">确认密码
                                                                    <span class="required"> * </span>
                                                                </label>
                                                                <div class="col-md-4">
                                                                    <input type="password" class="form-control" name="rpassword" />
                                                                    <span class="help-block"> 确认账号密码 </span>
                                                                </div>
                                                            </div>--%>
                                                            <div class="form-group">
                                                                <label class="control-label col-md-3">输入密码
                                                                    <span class="required"> * </span>
                                                                </label>
                                                                <div class="col-md-4">
                                                                    <input type="password" class="form-control" name="loginPwd" id="submit_form_password" />
                                                                    <span class="help-block"> 请输入账号密码. </span>
                                                                </div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="control-label col-md-3">确认密码
                                                                    <span class="required"> * </span>
                                                                </label>
                                                                <div class="col-md-4">
                                                                    <input type="password" class="form-control" name="rLoginPwd" id="r_submit_form_password" />
                                                                    <span class="help-block"> 确认账号密码 </span>
                                                                </div>
                                                            </div>
                                                        </div>

                                                        <div class="tab-pane" id="tab2">
                                                        <h3 class="block">输入账号详细信息</h3>
                                                        <div class="form-group">
                                                            <label class="control-label col-md-3">应用名称
                                                                <span class="required"> * </span>
                                                            </label>
                                                            <div class="col-md-4">
                                                                <input type="text" class="form-control" name="appName" id="appName"/>
                                                                <span class="help-block"> 请输入应用名称 </span>
                                                            </div>
                                                        </div>
                                                        <%--<div class="form-group">
                                                            <label class="control-label col-md-3">应用IP
                                                                <span class="required"> * </span>
                                                            </label>
                                                            <div class="col-md-4">
                                                                <input type="text" class="form-control" name="appIP" />
                                                                <span class="help-block"> 请输入应用IP </span>
                                                            </div>
                                                        </div>--%>
                                                        <div class="form-group">
                                                            <label class="control-label col-md-3">注册人
                                                                <span class="required"> * </span>
                                                            </label>
                                                            <div class="col-md-4">
                                                                <input type="text" class="form-control" name="registPsn" id="registPsn"/>
                                                                <span class="help-block"> 请输入登注册人名称 </span>
                                                            </div>
                                                        </div>
                                                    </div>

                                                        <div class="tab-pane" id="tab3">
                                                            <h3 class="block">输入账号详细信息</h3>

                                                            <div class="row">
                                                                <div class="col-xs-12">
                                                                    <div class="mt-element-ribbon bg-grey-steel">
                                                                        <div class="ribbon ribbon-border-hor ribbon-clip ribbon-color-danger uppercase">
                                                                            <div class="ribbon-sub ribbon-clip"></div>应用组选择 </div>
                                                                        <div class="form-group">
                                                                            <label class="control-label col-md-3">应用组名称</label>
                                                                            <div class="col-md-4">
                                                                                <select name="userGrpName" id="userGrpName" class="form-control">
                                                                                    <%--<option value=""></option>
                                                                                    <option value="AF">Teradata</option>
                                                                                    <option value="AL">思特奇</option>
                                                                                    <option value="DZ">创我</option>
                                                                                    <option value="AS">浪潮</option>--%>
                                                                                </select>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>

                                                                <div class="col-xs-12">
                                                                    <div class="mt-element-ribbon bg-grey-steel">
                                                                        <div class="ribbon ribbon-border-hor ribbon-clip ribbon-color-info uppercase">
                                                                            <div class="ribbon-sub ribbon-clip"></div> 允许访问的API信息 </div>
                                                                        <br>
                                                                        <br>
                                                                        <div class="form-group">
                                                                            <div class="portlet-body">
                                                                                <div id="ApiAuthTree" class="tree-demo"></div>
                                                                            <%--<div id="tree_1" class="tree-demo">
                                                                                <ul>
                                                                                    <li> Root node 1
                                                                                        <ul>
                                                                                            <li data-jstree='{ "selected" : true ,"type" : "file","icon":"fa fa-check icon-state-success"}'>
                                                                                                <a href="javascript:;"> Initially selected </a>
                                                                                            </li>
                                                                                            <li data-jstree='{ "selected" : true ,"type" : "file","icon":"fa fa-check icon-state-success"}'> custom icon URL </li>
                                                                                            <li data-jstree='{ "icon": "fa fa-folder icon-state-danger" }'> initially open
                                                                                                <ul>
                                                                                                    <li data-jstree='{ "selected" : true ,"type" : "file","icon":"fa fa-check icon-state-success"}'> Disabled Node </li>
                                                                                                    <li data-jstree='{ "selected" : true ,"type" : "file","icon":"fa fa-check icon-state-success"}'> Another node </li>
                                                                                                </ul>
                                                                                            </li>
                                                                                            <li data-jstree='{ "selected" : true ,"type" : "file","icon":"fa fa-check icon-state-success"}'> Custom icon class (bootstrap) </li>
                                                                                        </ul>
                                                                                    </li>
                                                                                    <li data-jstree='{ "selected" : true ,"type" : "file","icon":"fa fa-check icon-state-success"}'>
                                                                                        <a href="http://www.jstree.com"> Clickanle link node </a>
                                                                                    </li>
                                                                                </ul>
                                                                            </div>--%>
                                                                                </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>

                                                        <div class="tab-pane" id="tab4">
                                                            <h3 class="block">输入账号详细信息</h3>

                                                            <div class="alert alert-success">

                                                            </div>
                                                            <div class="note note-success">
                                                                <h4 class="block">点击提交保存后，您注册的信息已经发送后台进行审核，请耐心等待！</h4>
                                                            </div>
                                                        </div>


                                                    </div>
                                                </div>

                                                <div class="form-actions">
                                                    <div class="row">
                                                        <div class="col-md-offset-3 col-md-9">
                                                            <a href="javascript:;" class="btn default button-previous">
                                                                <i class="fa fa-angle-left"></i> 上一步 </a>
                                                            <a href="javascript:;" class="btn btn-outline green button-next"> 下一步
                                                                <i class="fa fa-angle-right"></i>
                                                            </a>
                                                            <a href="javascript:apiUser.registSave();" class="btn green button-submit"> 提交保存
                                                                <i class="fa fa-check"></i>
                                                            </a>
                                                        </div>
                                                    </div>
                                                </div>

                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- END PAGE CONTENT INNER -->
                </div>

            </div>
        </div>
    </div>
        <!-- BEGIN FOOTER -->
        <%@include file="/WEB-INF/views/include/admin/footer.jsp" %>
        <!-- END FOOTER -->
    <script>
        $(function () {

        });
    </script>
    </body>
</html>
