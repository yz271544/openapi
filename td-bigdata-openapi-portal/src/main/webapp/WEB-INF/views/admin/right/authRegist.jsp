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
    <script src="${ctx}/js/scripts/admin/right/authRegist.js" type="text/javascript"></script>

    <title>openapi控制台 -- 注册管理 -- 权限申请</title>
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
                    <h1>注册管理
                        <small>权限申请</small>
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

                        <div class="col-md-12">
                            <!-- BEGIN SAMPLE TABLE PORTLET-->
                            <div class="portlet box blue">
                                <div class="portlet-title">
                                    <div class="caption">
                                        <i class="fa fa-cogs"></i>API权限选择区 </div>
                                </div>
                                <div class="portlet-body">
                                    <form class="form-horizontal" role="form">
                                        <div class="form-body">
                                            <div class="form-group">

                                                <div id="ApiAuthTree" class="tree-demo"></div>

                                            </div>

                                            <div class="form-group">
                                                <div class="col-md-offset-9 col-md-12" id="queryConditionButtonDiv">
                                                    <button id="authRegistButton" type="button" class="btn green">保存</button>
                                                    <button type="button" class="btn default">取消</button>
                                                </div>
                                            </div>
                                        </div>
                                    </form>
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

<script>
    $(function () {
        //设置对应的menu为选中状态
        $(".hor-menu ul").find("li").filter(".menu-dropdown").eq(0).addClass("active");
        $(".hor-menu ul").find("li").filter(".menu-dropdown").eq(0).find("li").eq(1).addClass("active");

    });
</script>
</body>
</html>
