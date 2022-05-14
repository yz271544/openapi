<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE HTML>
<html>
    <!-- BEGIN HEAD -->
    <head>
	  <title>首页</title>
	  <%@include file="/WEB-INF/views/include/common.jsp" %>	
    </head>
    <!-- END HEAD -->
    <body class="page-container-bg-solid page-boxed">
    <%@include file="/WEB-INF/views/include/header.jsp"%>
        <!-- BEGIN CONTENT -->
          <!-- BEGIN CONTAINER -->
        <div class="page-container">
            <!-- BEGIN CONTENT -->
            <div class="page-content-wrapper">
          	  	<!-- BANNER AREA -->
		        <div id="myCarousel" class="carousel slide">
			      <ol class="carousel-indicators">
			        <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
			        <li data-target="#myCarousel" data-slide-to="1"></li>
			        <li data-target="#myCarousel" data-slide-to="2"></li>
			      </ol>
			      <!-- Carousel items -->
			      <div class="carousel-inner">
			        <div class="active item">
			        	<img src="${ctx }/img/slider/2.jpg" alt="First slide"/>
			        </div>
			        <div class="item">
			        	<img src="${ctx }/img/slider/1.jpg" alt="Second slide"/>
			        </div>
			        <div class="item">
						<img src="${ctx }/img/slider/3.jpg" alt="Three slide"/>
					</div>
			      </div>
			      <!-- Carousel nav -->
			      <a class="carousel-control left" href="#myCarousel" data-slide="prev">
			      	<span class="glyphicon glyphicon-chevron-left"></span>
			      </a>
			      <a class="carousel-control right" href="#myCarousel" data-slide="next">
			      	<span class="glyphicon glyphicon-chevron-right"></span>
			      </a>
			    </div>
			    <div class="page-head">
                    <div class="container">
                        <!-- BEGIN PAGE TITLE -->
                        <div class="page-title">
                            <h1>欢迎访问山西移动OPEN API系统
                                <small>开放 & 能力</small>
                            </h1>
                        </div>
                    </div>
                </div>    
			</div>   
        </div>   
        <!-- END CONTENT -->
    <%@include file="/WEB-INF/views/include/footer.jsp"%>
		<script>
		   $(function(){
			   $("#myCarousel").carousel({
				   interval: 3000
			   });
		   });
		</script>
    </body>
</html>