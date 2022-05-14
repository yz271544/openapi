<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/include/admin/taglib.jsp"%>
<html>
    <!-- BEGIN HEAD -->
    <head>
        <%--<meta content="width=device-width, initial-scale=1.0" name="viewport">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">

        <meta content="Metronic Shop UI description" name="description">
        <meta content="Metronic Shop UI keywords" name="keywords">
        <meta content="keenthemes" name="author">

        <meta property="og:site_name" content="-CUSTOMER VALUE-">
        <meta property="og:title" content="-CUSTOMER VALUE-">
        <meta property="og:description" content="-CUSTOMER VALUE-">
        <meta property="og:type" content="website">
        <meta property="og:image" content="-CUSTOMER VALUE-">
        <meta property="og:url" content="-CUSTOMER VALUE-">--%>

        <%@include file="/WEB-INF/views/include/admin/common.jsp"%>
        <link href="${ctx}/js/plugins/slider-revolution-slider/rs-plugin/css/settings.css" rel="stylesheet">
        <link href="${ctx}/js/plugins/fancybox/source/jquery.fancybox.css" rel="stylesheet">
        <%--<link href="${ctx}/assets/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet">
        <link href="${ctx}/assets/global/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet">
        <link href="${ctx}/assets/global/plugins/slider-revolution-slider/rs-plugin/css/settings.css" rel="stylesheet">
        <link href="${ctx}/assets/global/plugins/fancybox/source/jquery.fancybox.css" rel="stylesheet">
        <link href="${ctx}/assets/global/css/components.css" rel="stylesheet">--%>
        <link href="${ctx}/assets/frontend/onepage/css/style.css" rel="stylesheet" type="text/css">
        <link href="${ctx}/assets/frontend/onepage/css/style-responsive.css" rel="stylesheet" type="text/css">
        <link href="${ctx}/assets/frontend/onepage/css/themes/red.css" rel="stylesheet" id="style-color" type="text/css">
        <link href="${ctx}/assets/frontend/onepage/css/custom.css" rel="stylesheet" type="text/css">
        <title>openapi账户注册选择页面</title>
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

    <div class="promo-block" id="promo-block">
        <div class="tp-banner-container">
            <div class="tp-banner" >
                <ul>
                    <li data-transition="fade" data-slotamount="5" data-masterspeed="700" data-delay="9400" class="slider-item-1">
                        <img src="${ctx}/img/slide1.jpg" alt="" data-bgfit="cover" style="opacity:0.4 !important;" data-bgposition="center center" data-bgrepeat="no-repeat">
                        <div class="tp-caption large_text customin customout start"
                             data-x="center"
                             data-hoffset="0"
                             data-y="center"
                             data-voffset="60"
                             data-customin="x:0;y:0;z:0;rotationX:90;rotationY:0;rotationZ:0;scaleX:1;scaleY:1;skewX:0;skewY:0;opacity:0;transformPerspective:200;transformOrigin:50% 0%;"
                             data-customout="x:0;y:0;z:0;rotationX:0;rotationY:0;rotationZ:0;scaleX:0.75;scaleY:0.75;skewX:0;skewY:0;opacity:0;transformPerspective:600;transformOrigin:50% 50%;"
                             data-speed="1000"
                             data-start="500"
                             data-easing="Back.easeInOut"
                             data-endspeed="300">

                            <br>
                            <br>
                            <br>

                            <div class="promo-like" style="top: -16px;"><i class="fa fa-thumbs-up"></i></div>
                            <div class="promo-like-text">
                                <h2>API使用者</h2>
                                <p>该入口提供使用者注册功能，创建后可以登录系统<br>进行相应的功能操作<a href="/openapi/apiUserRegistIndex.htm"> 点击进入</a></p>
                            </div>

                            <br>


                            <div class="promo-like" style="top: -16px;"><i class="fa fa-cogs"></i></div>
                            <div class="promo-like-text">
                                <h2>API开发者</h2>
                                <p>该入口提供开发者注册功能，创建后可以登录系统<br>进行API创建发布等相应功能 <a href="/openapi/apiDeveloperRegistIndex.htm"> 点击进入</a></p>
                            </div>

                        </div>

                        <div class="tp-caption large_bold_white fade"
                             data-x="center"
                             data-y="center"
                             data-voffset="-110"
                             data-speed="300"
                             data-start="1700"
                             data-easing="Power4.easeOut"
                             data-endspeed="500"
                             data-endeasing="Power1.easeIn"
                             data-captionhidden="off"
                             style="z-index: 6"><span></span> OPEN API 账号注册
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </div>


</div>
        </div>
        <!-- BEGIN FOOTER -->
        <%@include file="/WEB-INF/views/include/admin/footer.jsp" %>
        <!-- END FOOTER -->


        <script src="${ctx}/js/plugins/slider-revolution-slider/rs-plugin/js/jquery.themepunch.revolution.min.js" type="text/javascript"></script>
        <script src="${ctx}/js/plugins/slider-revolution-slider/rs-plugin/js/jquery.themepunch.tools.min.js" type="text/javascript"></script>
        <script src="${ctx}/js/plugins/fancybox/source/jquery.fancybox.pack.js" type="text/javascript"></script><!-- pop up -->
        <script src="${ctx}/assets/frontend/onepage/scripts/revo-ini.js" type="text/javascript"></script>

        <%--<script src="${ctx}/assets/global/plugins/jquery.min.js" type="text/javascript"></script>
        <script src="${ctx}/assets/global/plugins/jquery-migrate.min.js" type="text/javascript"></script>
        <script src="${ctx}/assets/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
        <script src="${ctx}/assets/global/plugins/slider-revolution-slider/rs-plugin/js/jquery.themepunch.revolution.min.js" type="text/javascript"></script>
        <script src="${ctx}/assets/global/plugins/slider-revolution-slider/rs-plugin/js/jquery.themepunch.tools.min.js" type="text/javascript"></script>
        <script src="${ctx}/assets/frontend/onepage/scripts/revo-ini.js" type="text/javascript"></script>
        <script src="${ctx}/assets/global/plugins/fancybox/source/jquery.fancybox.pack.js" type="text/javascript"></script><!-- pop up -->
        <script src="${ctx}/assets/global/plugins/jquery.easing.js"></script>
        <script src="${ctx}/assets/global/plugins/jquery.parallax.js"></script>
        <script src="${ctx}/assets/global/plugins/jquery.scrollTo.min.js"></script>
        <script src="${ctx}/assets/frontend/onepage/scripts/jquery.nav.js"></script>
        <script src="${ctx}/assets/frontend/onepage/scripts/layout.js" type="text/javascript"></script>--%>

    <script>
        $(function () {
            /*$('.tp-caption').css("top","215px");*/
        });
    </script>
    </body>
</html>
