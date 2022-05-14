 <meta charset="utf-8"/>
 <!--[if IE 8]> <meta http-equiv="X-UA-Compatible" content="IE=8" /> <![endif]-->
 <!--[if IE 9]> <meta http-equiv="X-UA-Compatible" content="IE=9"> <![endif]-->
 <!--[if !IE]><!-->
 <meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=7; IE=EDGE" />
 <!-- <html lang="en" class="no-js"> -->
 <!--<![endif]-->
 <meta content="width=device-width, initial-scale=1" name="viewport"/>

<!-- BEGIN GLOBAL MANDATORY STYLES -->
<link href="${ctx}/js/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/js/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/js/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/js/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/js/plugins/bootstrap-switch/css/bootstrap-switch.min.css" rel="stylesheet" type="text/css" />
<!-- END GLOBAL MANDATORY STYLES -->

<!-- BEGIN PAGE LEVEL PLUGINS -->
 <link href="${ctx}/js/plugins/jstree/dist/themes/default/style.min.css" rel="stylesheet" type="text/css" />
 <!-- pagination plugin begin-->
<link href="${ctx}/js/plugins/jquery-pagination/pagination.css" rel="stylesheet" type="text/css" />
<!-- pagination plugin end-->
<!-- multi-select2 plugin begin-->
<link href="${ctx}/js/plugins/bootstrap-select/css/bootstrap-select.min.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/js/plugins/jquery-multi-select/css/multi-select.css" rel="stylesheet" type="text/css" />
<!-- multi- plugin end-->
<!-- select2 plugin begin-->
<link href="${ctx}/js/plugins/select2/css/select2.min.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/js/plugins/select2/css/select2-bootstrap.min.css" rel="stylesheet" type="text/css" />
<!-- select2 plugin end -->
<link href="${ctx}/js/plugins/bootstrap-wizard2/css/bwizard.min.css" rel="stylesheet" type="text/css" />
<!-- END PAGE LEVEL PLUGINS -->

<!-- BEGIN THEME GLOBAL STYLES -->
<link href="${ctx}/css/components.min.css" rel="stylesheet" id="style_components" type="text/css" />
<link href="${ctx}/css/plugins.min.css" rel="stylesheet" type="text/css" />
<!-- END THEME GLOBAL STYLES -->
<!-- BEGIN THEME LAYOUT STYLES -->
<link href="${ctx}/css/layout.min.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/themes/default.min.css" rel="stylesheet" type="text/css" id="style_color" />
<link href="${ctx}/css/custom.min.css" rel="stylesheet" type="text/css" />
<!-- END THEME LAYOUT STYLES -->

<!-- BEGIN CORE PLUGINS -->
<script src="${ctx}/js/plugins/jquery.min.js" type="text/javascript"></script>
<script src="${ctx}/js/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<script src="${ctx}/js/plugins/js.cookie.min.js" type="text/javascript"></script>
<script src="${ctx}/js/plugins/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js" type="text/javascript"></script>
<script src="${ctx}/js/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
<script src="${ctx}/js/plugins/jquery.blockui.min.js" type="text/javascript"></script>
<script src="${ctx}/js/plugins/uniform/jquery.uniform.min.js" type="text/javascript"></script>
<script src="${ctx}/js/plugins/bootstrap-switch/js/bootstrap-switch.min.js" type="text/javascript"></script>
<!-- END CORE PLUGINS -->

<!-- BEGIN THEME GLOBAL SCRIPTS -->
<script src="${ctx}/js/scripts/app.min.js" type="text/javascript"></script>
<!-- END THEME GLOBAL SCRIPTS -->

<!-- BEGIN PAGE LEVEL PLUGINS -->
<script src="${ctx}/js/plugins/json2.js"></script>
<script src="${ctx}/js/plugins/bootbox/bootbox.min.js" type="text/javascript"></script>
<script src="${ctx}/js/plugins/jquery-validation/js/jquery.validate.min.js" type="text/javascript"></script>
<script src="${ctx}/js/plugins/jquery-validation/js/additional-methods.min.js" type="text/javascript"></script>
<script src="${ctx}/js/plugins/bootstrap-datepicker/js/bootstrap-datepicker.min.js" type="text/javascript"></script>
<!-- dw_begin -->
<script src="${ctx}/js/plugins/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js" type="text/javascript"></script>
<!-- pagination plugin begin -->
<script src="${ctx}/js/plugins/jquery-pagination/jquery.pagination.js" type="text/javascript"></script>
<!-- pagination plugin end -->
<!-- select2 plugin begin -->
<script src="${ctx}/js/plugins/select2/js/select2.full.min.js" type="text/javascript"></script>
<script src="${ctx}/js/plugins/select2/js/i18n/zh-CN.js"></script>
<!-- select2 plugin end -->

<script src="${ctx}/js/modules/apiRelease/StringBuffer.js"></script>
<script src="${ctx}/js/plugins/lhgdialog/lhgdialog.min.js?skin=default" type="text/javascript"></script>
<script src="${ctx}/js/plugins/bootstrap-wizard2/js/bwizard.js"></script>
<!-- multi-select2 plugin begin -->
<script src="${ctx}/js/plugins/bootstrap-select/js/bootstrap-select.min.js" type="text/javascript"></script>
<script src="${ctx}/js/plugins/jquery-multi-select/js/jquery.multi-select.js" type="text/javascript"></script>
<!-- multi-select2 plugin end -->
<!-- echarts begin -->
<script src="${ctx}/js/plugins/echarts/3.2.3/echarts.common.min.js" type="text/javascript"></script>
<!-- echarts end -->
<!-- counterup begin -->
<script src="${ctx}/js/plugins/counterup/jquery.waypoints.min.js" type="text/javascript"></script>
<script src="${ctx}/js/plugins/counterup/jquery.counterup.min.js" type="text/javascript"></script>
<!-- counterup end -->
<!-- dw_end -->
<!-- END PAGE LEVEL PLUGINS -->


<!-- BEGIN THEME LAYOUT SCRIPTS -->
<script src="${ctx}/js/layouts/layout.min.js" type="text/javascript"></script>
<script src="${ctx}/js/scripts/common.fn.js" type="text/javascript"></script>
<!-- END THEME LAYOUT SCRIPTS -->
 <!-- BEGIN PAGE LEVEL PLUGINS -->
 <script src="${ctx}/js/plugins/bootstrap-confirmation/bootstrap-confirmation.min.js" type="text/javascript"></script>
 <!-- END PAGE LEVEL PLUGINS -->
 <!-- BEGIN PAGE LEVEL SCRIPTS -->
 <%--<script src="${ctx}/js/scripts/ui-confirmations.js" type="text/javascript"></script>--%>
 <!-- END PAGE LEVEL SCRIPTS -->
<script type="text/javascript">
	jQuery.basePath = "${ctx}";
	/* dw_begin*/
	//修改blockUI加载等待图标的路径
	App.setAssetsPath("../");
	App.setGlobalImgPath("/img/");
	/* dw_end*/
</script>