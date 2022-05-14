<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/views/include/admin/taglib.jsp" %>
<html>
<!-- BEGIN HEAD -->
<head>
    <%@include file="/WEB-INF/views/include/admin/common.jsp" %>
    <!-- BEGIN PAGE LEVEL PLUGINS -->
    <script src="${ctx}/js/plugins/jstree/dist/jstree.min.js" type="text/javascript"></script>
    <!-- END PAGE LEVEL PLUGINS -->


    <!-- BEGIN CUSTOMER PAGE LEVEL SCRIPTS -->
    <script src="${ctx}/js/scripts/admin/auth/userManage.js" type="text/javascript"></script>
    <!-- END CUSTOMER PAGE LEVEL SCRIPTS -->

    <!-- BEGIN PAGE LEVEL SCRIPTS -->
    <%--<script src="${ctx}/js/scripts/ui-tree.js" type="text/javascript"></script>--%>
    <!-- END PAGE LEVEL SCRIPTS -->

    <title>openapi控制台 -- 系统管理 -- 用户管理</title>
</head>
<!-- END HEAD -->
<body class="page-container-bg-solid page-boxed">
<!-- BEGIN HEADER -->
<%@include file="/WEB-INF/views/include/admin/header.jsp" %>
<!-- END HEADER -->
<!-- BEGIN CONTENT -->
<!-- BEGIN CONTAINER -->
<div class="page-container">
    <div class="page-content-wrapper">
        <!-- BANNER AREA -->
        <div class="page-head">
            <div class="container">
                <!-- BEGIN PAGE TITLE -->
                <div class="page-title">
                    <h1>系统管理
                        <small>用户管理</small>
                    </h1>
                </div>
            </div>
        </div>

        <!-- BEGIN PAGE CONTENT BODY -->
        <div class="page-content">
            <div class="container">
                <!-- BEGIN PAGE CONTENT INNER -->
                <div class="page-content-inner">
                    <!-- BEGIN PAGE CONTENT-->
                    <div class="row">
                        <div class="col-md-4">
                            <div class="portlet light ">
                                <div class="portlet-title">
                                    <div class="caption">
                                        <i class="icon-social-dribbble font-blue-sharp"></i>
                                        <span class="caption-subject font-blue-sharp bold uppercase">用户分类</span>
                                    </div>
                                </div>
                                <div class="portlet-body">
                                    <ul class="nav nav-pills">
                                        <li class="active">
                                            <a href="#tab_2_1" data-toggle="tab"> 按用户组查询 </a>
                                        </li>
                                        <%--<li>
                                            <a href="#tab_2_2" data-toggle="tab"> 按组织查询 </a>
                                        </li>--%>
                                    </ul>
                                    <div class="tab-content">
                                        <div class="tab-pane fade active in" id="tab_2_1">
                                            <div id="tree_1" class="tree-demo">
                                                <%--<ul>
                                                    <li> Root node 1
                                                        <ul>
                                                            <li data-jstree='{ "selected" : true }'>
                                                                <a href="javascript:;"> Initially selected </a>
                                                            </li>
                                                            <li data-jstree='{ "icon" : "fa fa-briefcase icon-state-success " }'> custom icon URL </li>
                                                            <li data-jstree='{ "opened" : true }'> initially open
                                                                <ul>
                                                                    <li data-jstree='{ "disabled" : true }'> Disabled Node </li>
                                                                    <li data-jstree='{ "type" : "file" }'> Another node </li>
                                                                </ul>
                                                            </li>
                                                            <li data-jstree='{ "icon" : "fa fa-warning icon-state-danger" }'> Custom icon class (bootstrap) </li>
                                                        </ul>
                                                    </li>
                                                    <li data-jstree='{ 'type' : 'file' }'>
                                                        <a href="http://www.jstree.com"> Clickanle link node </a>
                                                    </li>
                                                </ul>--%>
                                            </div>
                                        </div>
                                        <%--<div class="tab-pane fade" id="tab_2_2">
                                            <p> Food truck fixie locavore, accusamus mcsweeney's marfa nulla single-origin coffee squid. Exercitation +1 labore velit, blog sartorial PBR leggings next level wes anderson artisan four loko farm-to-table
                                                craft beer twee. Qui photo booth letterpress, commodo enim craft beer mlkshk aliquip jean shorts ullamco ad vinyl cillum PBR. Homo nostrud organic, assumenda labore aesthetic magna delectus mollit.
                                                Keytar helvetica VHS salvia yr, vero magna velit sapiente labore stumptown. Vegan fanny pack odio cillum wes anderson 8-bit, sustainable jean shorts beard ut DIY ethical culpa terry richardson biodiesel.
                                                Art party scenester stumptown, tumblr butcher vero sint qui sapiente accusamus tattooed echo park. </p>
                                        </div>--%>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="col-md-8">
                            <!-- BEGIN SAMPLE TABLE PORTLET-->
                            <div class="portlet box blue">
                                <div class="portlet-title">
                                    <div class="caption">
                                        <i class="fa fa-cogs"></i>查询条件 </div>
                                </div>
                                <div class="portlet-body">
                                    <form class="form-horizontal" role="form">
                                        <div class="form-body">
                                            <div class="form-group">

                                                    <label class="col-md-2 control-label">用户账号</label>
                                                    <div class="col-md-4">
                                                        <div class="input-icon">
                                                            <i class="fa fa-user"></i>
                                                            <input id="loginAcct" type="text" class="form-control" placeholder="" value=""> </div>
                                                    </div>

                                                    <label class="col-md-2 control-label">用户姓名</label>
                                                    <div class="col-md-4">
                                                        <div class="input-icon">
                                                            <i class="fa fa-bell-o"></i>
                                                            <input id="userName" type="text" class="form-control" placeholder="" value=""> </div>
                                                    </div>

                                                </div>

                                                <div class="form-group">
                                                <%--<label class="col-md-2 control-label">用户姓名</label>
                                                <div class="col-md-4">
                                                    <div class="input-icon">
                                                        <i class="fa fa-bell-o"></i>
                                                        <input id="userName" type="text" class="form-control" placeholder=""> </div>
                                                </div>--%>

                                                <%--<label class="col-md-2 control-label">用户账号</label>
                                                <div class="col-md-4">
                                                    <div class="input-icon">
                                                        <i class="fa fa-user"></i>
                                                        <input type="text" class="form-control" placeholder="Left icon"> </div>
                                                </div>--%>
                                                    <div class="col-md-offset-9 col-md-12" id="queryConditionButtonDiv">
                                                        <button id="queryConditionButton" type="button" class="btn green">查询</button>
                                                        <button type="button" class="btn default">重置</button>
                                                    </div>



                                        </div>
                                        <%--<div class="form-actions">
                                            <div class="row">
                                                <div class="col-md-offset-3 col-md-9">
                                                    <button type="submit" class="btn green">Submit</button>
                                                    <button type="button" class="btn default">Cancel</button>
                                                </div>
                                            </div>
                                        </div>--%>
                                            </div>
                                    </form>
                                </div>
                            </div>
                            <!-- END SAMPLE TABLE PORTLET-->

                            <!-- BEGIN SAMPLE TABLE PORTLET-->
                            <div class="portlet box blue">
                                <div class="portlet-title">
                                    <div class="caption">
                                        <i class="fa fa-table"></i>用户信息列表 </div>
                                </div>
                                <div class="portlet-body">
                                    <div class="table-toolbar">
                                        <div class="row">
                                            <div class="col-md-6">
                                                <div class="btn-group" id="addUserButtonDiv">
                                                    <button id="addUser" class="btn sbold green"> 添加新用户
                                                        <i class="fa fa-plus"></i>
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <table class="table table-striped table-bordered table-hover  order-column" id="sample_1">
                                    </table>
                                </div>
                            </div>
                            <!-- END SAMPLE TABLE PORTLET-->
                        </div>

                    </div>
                    <!-- END PAGE CONTENT-->
                </div>
                <!-- END PAGE CONTENT INNER -->
            </div>
        </div>
        <!-- END PAGE CONTENT BODY -->
    </div>
</div>
<!-- END CONTAINER -->
<!-- END CONTENT -->
<!-- BEGIN FOOTER -->
<%@include file="/WEB-INF/views/include/admin/footer.jsp" %>
<!-- END FOOTER -->

<!-- 用户新增管理页面 -->
<div class="modal fade bs-modal-lg" id="addUserPage" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">新增用户</h4>
            </div>
            <div class="modal-body">

                <form class="form-horizontal" role="form">

                    <div class="form-body">

                        <div class="form-group has-success">
                            <label class="col-md-2 control-label">用户名</label>
                            <div class="col-md-4">
                                <input id="addLoginAcct" type="text" class="form-control" placeholder="请输入用户名">
                                <span class="help-block"> 用户名为用户为使用系统的工号 </span>
                            </div>

                            <label class="col-md-2 control-label">真实姓名</label>
                            <div class="col-md-4">
                                <input id="addUserName" type="text" class="form-control" placeholder="请输入真实姓名">
                                <span class="help-block"> 真实姓名为真实有效 </span>
                            </div>
                        </div>

                        <div class="form-group has-success">
                            <label class="col-md-2 control-label">登录密码</label>
                            <div class="col-md-4">
                                <input id="addLoginPwd" type="password" class="form-control" placeholder="请输入登录密码">
                                <span class="help-block"> 密码可以是数字或字母的组合 </span>
                            </div>

                            <label class="col-md-2 control-label">登录密码</label>
                            <div class="col-md-4">
                                <input id="addLoginPwdSecd" type="password" class="form-control" placeholder="请再次输入登录密码">
                                <span class="help-block"> 密码可以是数字或字母的组合 </span>
                            </div>
                        </div>

                        <div class="form-group has-success">
                            <label class="col-md-2 control-label">性别</label>
                            <div class="col-md-4">
                                <div class="radio-list">
                                    <label class="radio-inline">
                                        <input type="radio" name="addGender" id="addGender1" value="0" checked> 男 </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="addGender" id="addGender2" value="1"> 女 </label>
                                </div>
                            </div>

                            <%--<label class="col-md-2 control-label">所属机构</label>
                            <div class="col-md-4">
                                <select class="form-control" id="addOrg">
                                    <option>Option 1</option>
                                    <option>Option 2</option>
                                    <option>Option 3</option>
                                    <option>Option 4</option>
                                    <option>Option 5</option>
                                </select>
                                <span class="help-block"> 所属机构 </span>
                            </div>--%>

                        </div>

                        <div class="form-group has-success">
                            <label class="col-md-2 control-label">手机号</label>
                            <div class="col-md-4">
                                <input id="addPhone" type="text" class="form-control" placeholder="请输入手机号">
                                <span class="help-block"> 11位数字组合 </span>
                            </div>

                            <label class="col-md-2 control-label">邮箱</label>
                            <div class="col-md-4">
                                <input id="addMail" type="text" class="form-control" placeholder="请再邮箱">
                                <span class="help-block"> 邮箱为真实有效 </span>
                            </div>
                        </div>

                        <%--<div class="form-group has-success">
                            <label class="col-md-2 control-label">所属地域</label>
                            <div class="col-md-3">
                                <select class="form-control" id="addRegion">
                                    <option>Option 1</option>
                                    <option>Option 2</option>
                                    <option>Option 3</option>
                                    <option>Option 4</option>
                                    <option>Option 5</option>
                                </select>
                                <span class="help-block"> 所属地市 </span>
                            </div>
                            <div class="col-md-3">
                                <select class="form-control" id="addCity">
                                    <option>Option 1</option>
                                    <option>Option 2</option>
                                    <option>Option 3</option>
                                    <option>Option 4</option>
                                    <option>Option 5</option>
                                </select>
                                <span class="help-block"> 所属区县 </span>
                            </div>
                            <div class="col-md-4">
                                <select class="form-control" id="addSite">
                                    <option>Option 1</option>
                                    <option>Option 2</option>
                                    <option>Option 3</option>
                                    <option>Option 4</option>
                                    <option>Option 5</option>
                                </select>
                                <span class="help-block"> 所属营业厅 </span>
                            </div>
                        </div>--%>

                        <div class="form-group has-success">
                            <label class="col-md-2 control-label">职位</label>
                            <div class="col-md-4">
                                <input id="addPosition" type="text" class="form-control" placeholder="请输入职位">
                                <span class="help-block"> 用户的职位 </span>
                            </div>

                            <label class="col-md-2 control-label">用户类型</label>
                            <div class="col-md-4">
                                <select class="form-control" id="addUserType">
                                    <option value="1">API使用者</option>
                                    <option value="2">API开发者</option>
                                    <option value="3">平台维护者</option>
                                </select>
                                <span class="help-block"> 用户类型 </span>
                            </div>
                        </div>

                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn dark btn-outline" data-dismiss="modal">关闭</button>
                <button type="button" class="btn green" id="addUserSave">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<!-- 用户详细信息管理页面 -->
<div class="modal fade bs-modal-lg" id="userDetailInfo" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">用户详细信息</h4>
            </div>
            <div class="modal-body">
                <ul class="nav nav-pills">
                    <li class="active">
                        <a href="#usertab_1_1" data-toggle="tab"> 按用户组查询 </a>
                    </li>
                    <li>
                        <a href="#usertab_1_2" data-toggle="tab"> 按组织查询 </a>
                    </li>
                </ul>
                <div class="tab-content">
                    <div class="tab-pane fade active in" id="usertab_1_1">
                        <form class="form-horizontal" role="form">

                            <div class="form-body">
                                <div class="row">
                                <div class="col-md-8">
                                    <div class="portlet light ">

                                        <div class="form-group">
                                            <label class="col-md-2 control-label">用户名</label>
                                            <input id="updateUserId" type="text" class="form-control input-sm" value="" style="display:none;">
                                            <div class="col-md-4">
                                                <input id="updateLoginAcct" type="text" class="form-control" readonly>
                                            </div>

                                            <label class="col-md-2 control-label">登录密码</label>
                                            <div class="col-md-4">
                                                <input id="updateLoginPwd" type="text" class="form-control">
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="col-md-2 control-label">真实姓名</label>
                                            <div class="col-md-4">
                                                <input id="updateUserName" type="text" class="form-control">
                                            </div>

                                            <label class="col-md-2 control-label">所属机构</label>
                                            <div class="col-md-4">
                                                <select name="updateOrgCode" id="updateOrgCode" class="form-control">
                                                    <option value=""></option>
                                                    <option value="1">山西移动</option>
                                                    <option value="2">Teradata</option>
                                                    <option value="3">思特奇</option>
                                                    <option value="4">创我</option>
                                                    <option value="5">浪潮</option>
                                                </select>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="col-md-2 control-label">性别</label>
                                            <div class="col-md-4">
                                                <div class="radio-list">
                                                    <label class="radio-inline">
                                                        <input type="radio" name="updateUserGender" id="updateUserGender1" value="0"> 男 </label>
                                                    <label class="radio-inline">
                                                        <input type="radio" name="updateUserGender" id="updateUserGender2" value="1"> 女 </label>
                                                </div>
                                            </div>

                                            <label class="col-md-2 control-label"></label>
                                            <div class="col-md-4">
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="control-label col-md-2">手机号
                                            </label>
                                            <div class="col-md-4">
                                                <input type="text" class="form-control" name="updateUserPhone" id="updateUserPhone"/>
                                            </div>

                                            <label class="control-label col-md-2">邮箱
                                            </label>
                                            <div class="col-md-4">
                                                <input type="text" class="form-control" name="updateUserMail" id="updateUserMail"/>
                                            </div>

                                        </div>

                                        <div class="form-group">
                                            <label class="control-label col-md-2">账号状态
                                            </label>
                                            <div class="col-md-4">
                                                <select name="updateUserStat" id="updateUserStat" class="form-control">
                                                    <option value=""></option>
                                                    <option value="1">未审核</option>
                                                    <option value="2">生效</option>
                                                    <option value="3">过期</option>
                                                </select>
                                            </div>

                                            <label class="control-label col-md-2">用户类型
                                                <%--<span class="required"> * </span>--%>
                                            </label>
                                            <div class="col-md-4">
                                                <select name="updateUserType" id="updateUserType" class="form-control">
                                                    <option value=""></option>
                                                    <option value="1">API使用者</option>
                                                    <option value="2">API开发者</option>
                                                    <option value="3">系统维护者</option>
                                                </select>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="control-label col-md-2">创建人
                                            </label>
                                            <div class="col-md-4">
                                                <input type="text" class="form-control" name="updateRegistPersn" id="updateRegistPersn"/>
                                            </div>

                                            <label class="control-label col-md-2">创建日期
                                            </label>
                                            <div class="col-md-4">
                                                <input type="text" class="form-control" name="updateRegistTime" id="updateRegistTime" readonly/>
                                            </div>

                                        </div>


                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="portlet light ">
                                        <div class="portlet-title">
                                            <div class="caption">
                                                <i class="icon-social-dribbble font-blue-sharp"></i>
                                                <span class="caption-subject font-blue-sharp bold uppercase">所属用户组</span>
                                            </div>
                                        </div>
                                        <div class="portlet-body">
                                            <div id="userGrpTree" class="tree-demo">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            </div>
                        </form>
                    </div>
                    <div class="tab-pane fade" id="usertab_1_2">
                        <div class="col-md-12">
                            <div class="portlet box blue">
                                <div class="portlet-title">
                                    <div class="caption">
                                        <i class="fa fa-table"></i>权限菜单区
                                    </div>
                                </div>
                                <div class="portlet-body">
                                    <div id="MenuAuthTree" class="tree-demo"></div>
                                </div>
                            </div>
                            <div class="portlet box blue">
                                <div class="portlet-title">
                                    <div class="caption">
                                        <i class="fa fa-table"></i>权限API区 </div>
                                </div>
                                <div class="portlet-body">
                                    <div id="ApiAuthTree" class="tree-demo"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn dark btn-outline" data-dismiss="modal">关闭</button>
                <button type="button" class="btn green" id="editedUserInfo">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<script>
    $(function () {
        //设置对应的menu为选中状态
        $(".hor-menu ul").find("li").filter(".menu-dropdown").last().addClass("active");
        $(".hor-menu ul").find("li").filter(".menu-dropdown").last().find("li").eq(0).addClass("active");

    });
</script>
</body>
</html>
