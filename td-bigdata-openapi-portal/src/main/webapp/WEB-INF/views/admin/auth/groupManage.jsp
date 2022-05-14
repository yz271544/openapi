<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/views/include/admin/taglib.jsp" %>
<html>
<!-- BEGIN HEAD -->
<head>
    <%@include file="/WEB-INF/views/include/admin/common.jsp" %>

    <%--<link href="${ctx}/js/plugins/bootstrap-editable/bootstrap-editable/css/bootstrap-editable.css" rel="stylesheet" type="text/css" />
    <link href="${ctx}/js/plugins/bootstrap-editable/inputs-ext/address/address.css" rel="stylesheet" type="text/css" />--%>


    <!-- BEGIN PAGE LEVEL PLUGINS -->
    <script src="${ctx}/js/plugins/jstree/dist/jstree.min.js" type="text/javascript"></script>
    <%--<script src="${ctx}/js/plugins/bootstrap-editable/bootstrap-editable/js/bootstrap-editable.js" type="text/javascript"></script>
    <script src="${ctx}/js/plugins/bootstrap-editable/inputs-ext/address/address.js" type="text/javascript"></script>--%>
    <%--<script src="${ctx}/js/scripts/form-editable.min.js" type="text/javascript"></script>--%>
    <!-- END PAGE LEVEL PLUGINS -->


    <!-- BEGIN CUSTOMER PAGE LEVEL SCRIPTS -->
    <script src="${ctx}/js/scripts/admin/auth/groupManage.js" type="text/javascript"></script>
    <!-- END CUSTOMER PAGE LEVEL SCRIPTS -->

    <!-- BEGIN PAGE LEVEL SCRIPTS -->
    <%--<script src="${ctx}/js/scripts/ui-tree.js" type="text/javascript"></script>--%>
    <!-- END PAGE LEVEL SCRIPTS -->

    <title>openapi控制台 -- 系统管理 -- 用户组管理</title>
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
                        <small>用户组管理</small>
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
                                        <span class="caption-subject font-blue-sharp bold uppercase">用户组列表</span>
                                    </div>
                                </div>
                                <div class="portlet-body">
                                    <div id="tree_1" class="tree-demo"></div>
                                </div>
                            </div>
                        </div>

                        <div class="col-md-8">
                            <!-- BEGIN SAMPLE TABLE PORTLET-->
                            <div class="portlet light portlet-fit ">
                                <div class="portlet-title">
                                    <div class="caption">
                                        <i class="icon-settings font-dark"></i>
                                        <span class="caption-subject font-dark sbold uppercase">用户组信息</span>
                                    </div>
                                    <div class="actions">
                                        <div class="btn-group btn-group-devided" data-toggle="buttons">
                                                <button id="grpAuth" type="button" class="btn sbold blue">组权限</button>
                                        </div>
                                    </div>
                                </div>
                                <div class="portlet-body">
                                    <div class="row">
                                        <div class="col-md-12">
                                            <table id="userGrpTable" class="table table-bordered table-striped">
                                                <tbody>
                                                    <tr style="display: none">
                                                        <td style="width:30%"> 用户组id </td>
                                                        <td style="width:70%">
                                                            <%--<a href="javascript:;" id="grpId" data-type="text" data-pk="1" data-original-title="Enter username"> superuser </a>--%>
                                                            <input id="grpId" type="text" class="form-control input-sm" value="">
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td style="width:30%"> 用户组名称 </td>
                                                        <td style="width:70%">
                                                            <%--<a href="javascript:;" id="grpName" data-type="text" data-pk="1" data-original-title="输入用户组名称"> </a>--%>
                                                            <input id="grpName" type="text" class="form-control input-sm" value="">
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td style="width:30%"> 上级组名称 </td>
                                                        <td style="width:70%">
                                                            <%--<a href="javascript:;" id="fathrGrpName" data-type="text" data-pk="1" data-original-title="Enter username"> superuser </a>--%>
                                                            <a href="javascript:;" id="fathrGrpName" idValue=""></a>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td style="width:30%"> 用户组描述 </td>
                                                        <td style="width:70%">
                                                            <%--<a href="javascript:;" id="grpDesc" data-type="text" data-pk="1" data-original-title="Enter username"> superuser </a>--%>
                                                            <input id="grpDesc" type="text" class="form-control input-sm" value="">
                                                        </td>
                                                    </tr>
                                                </tbody>
                                            </table>

                                            <div class="form-group">
                                                <div class="col-md-offset-7 col-md-12" id="queryConditionButtonDiv">
                                                    <button id="removeGrp" type="submit" class="btn green">删除用户组</button>
                                                    <button id="openAddGrpDialog" type="submit" class="btn green">新增用户组</button>
                                                    <button id="updateGrp" type="submit" class="btn green">更新信息</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- END SAMPLE TABLE PORTLET-->
                            <div class="portlet box blue">
                                <div class="portlet-title">
                                    <div class="caption">
                                        <i class="fa fa-table"></i>所包含用户信息列表 </div>
                                </div>
                                <div class="portlet-body">
                                    <div class="table-toolbar">
                                        <div class="row">
                                            <div class="col-md-6">
                                                <div class="btn-group" id="addUserButtonDiv">
                                                    <%--<button id="addUser" class="btn sbold green"> 添加新用户
                                                        <i class="fa fa-plus"></i>
                                                    </button>--%>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <table class="table table-striped table-bordered table-hover  order-column" id="sample_1">

                                    </table>
                                </div>
                            </div>
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

<!-- 用户组树选择页面 -->
<div class="modal fade bs-modal-lg" id="userGrpSelect" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">用户组树</h4>
            </div>
            <div class="modal-body">
                <input id="selectedGrpNodeId" type="text" class="form-control input-sm" value="" style="display:none;">
                <input id="selectedGrpNodeName" type="text" class="form-control input-sm" value="" style="display:none;">
                <div id="grpTree" class="tree-demo"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn dark btn-outline" data-dismiss="modal">关闭</button>
                <button type="button" class="btn green" id="selectedGrp" >选择</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<!-- 新增用户组页面 -->
<div class="modal fade bs-modal-lg" id="addUserGrpPage" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">新增用户组</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-12">
                        <table id="addUserGrpTable" class="table table-bordered table-striped">
                            <tbody>
                            <tr>
                                <td style="width:30%"> 用户组名称 </td>
                                <td style="width:70%">
                                    <%--<a href="javascript:;" id="grpName" data-type="text" data-pk="1" data-original-title="输入用户组名称"> </a>--%>
                                    <input id="addGrpName" type="text" class="form-control input-sm" value="">
                                </td>
                            </tr>
                            <tr>
                                <td style="width:30%"> 上级组名称 </td>
                                <td style="width:70%">
                                    <%--<a href="javascript:;" id="fathrGrpName" data-type="text" data-pk="1" data-original-title="Enter username"> superuser </a>--%>
                                    <a href="javascript:;" id="addFathrGrpName" idValue=""></a>
                                </td>
                            </tr>
                            <tr>
                                <td style="width:30%"> 用户组描述 </td>
                                <td style="width:70%">
                                    <%--<a href="javascript:;" id="grpDesc" data-type="text" data-pk="1" data-original-title="Enter username"> superuser </a>--%>
                                    <input id="addGrpDesc" type="text" class="form-control input-sm" value="">
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn dark btn-outline" data-dismiss="modal">关闭</button>
                <button type="button" class="btn green" id="saveUserGrp" >保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<!-- 组授权页面 -->

<div class="modal fade bs-modal-lg" id="userGrpAuth" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">用户组权限</h4>
            </div>
            <div class="modal-body">

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
                    <%--<div class="portlet box blue">
                        <div class="portlet-title">
                            <div class="caption">
                                <i class="fa fa-table"></i>权限数据源区 </div>
                        </div>
                        <div class="portlet-body">

                        </div>
                    </div>--%>
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
            <div class="modal-footer">
                <button type="button" class="btn dark btn-outline" data-dismiss="modal">关闭</button>
                <button type="button" class="btn green" id="saveUserGrpAuth" >保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>


<!-- 更新用户对应的用户组页面 -->
<div class="modal fade bs-modal-lg" id="updateUserGrpSelect" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">用户组树</h4>
            </div>
            <div class="modal-body">
                <input id="updateSelectedGrpNodeId" type="text" class="form-control input-sm" value="" style="display:none;">
                <input id="updateSelectedLoginAcct" type="text" class="form-control input-sm" value="" style="display:none;">
                <div id="updateUserGrpRelatTree" class="tree-demo"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn dark btn-outline" data-dismiss="modal">关闭</button>
                <button type="button" class="btn green" id="updateSelectedGrp" >选择</button>
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
        $(".hor-menu ul").find("li").filter(".menu-dropdown").last().find("li").eq(1).addClass("active");

        /*var a = $(".hor-menu ul").find("li").filter(".menu-dropdown").last().html()
         alert(a)*/
    });
</script>
</body>
</html>
