<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE HTML>
<html>
<!-- BEGIN HEAD -->
<head>
<%@include file="/WEB-INF/views/include/common.jsp" %>	
<title>订阅管理</title>
</head>
<!-- END HEAD -->
<body>
	<%@include file="/WEB-INF/views/include/admin/header.jsp"%>
	<!-- BEGIN CONTENT -->
	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container">
				 	<!-- BEGIN PAGE BREADCRUMBS -->
                     <ul class="page-breadcrumb breadcrumb">
                         <li>
                             <a href="#">支持中心</a>
                             <i class="fa fa-circle"></i>
                         </li>
                         <li>
                             <span>订阅管理</span>
                         </li>
                     </ul>
                    <!-- END PAGE BREADCRUMBS -->
					<!-- BEGIN PAGE CONTENT INNER -->
					<div class="page-content-inner">
						<div class="row ">
							<div class="col-md-12">
							    <!-- BEGIN Portlet PORTLET-->
                                <div class="portlet box green">
                                    <div class="portlet-title">
                                        <div class="caption">
                                            <i class="fa fa-gift"></i>查询区 </div>
                                        <div class="tools">
                                            <a href="javascript:;" class="collapse"> </a>
                                        </div>
                                    </div>
                                    <div class="portlet-body portlet-empty"> 
                                    	  <form action="#" class="form-horizontal" id="filter_form">
                                    	  	  <div class="row">
                                                  <div class="col-md-4">
                                                      <div class="form-group">
                                                          <label class="control-label col-md-4">API分类</label>
                                                          <div class="col-md-8">
                                                              <select id="api_sort" name="apiSort" class="form-control" >
																	<option value="-1">全部</option>		
															  </select>
                                                          </div>
                                                      </div>
                                                  </div>
                                                  <!--/span-->
                                                  <div class="col-md-4">
                                                      <div class="form-group">
                                                          <label class="control-label col-md-4">状态</label>
                                                          <div class="col-md-7">
                                                              <select name="effFlag" class="form-control">
                                                                  <option value="-1">全部</option>
                                                                  <option value="1">有效</option>
                                                                  <option value="0">无效</option>
                                                              </select>
                                                          </div>
                                                      </div>
                                                  </div>
                                                  <div class="col-md-4">
                                                      <div class="form-group">
                                                          <label class="control-label col-md-4">数据周期</label>
                                                          <div class="col-md-7">
                                                              <select name="dataCycleType" class="form-control">
                                                                  <option value="-1">全部</option>
                                                                  <option value="0">日表</option>
                                                                  <option value="1">月表</option>
                                                              </select>
                                                          </div>
                                                      </div>
                                                  </div>
                                                  <!--/span-->
                                              </div>
                                              <div class="row">
                                                  <div class="col-md-4">
                                                      <div class="form-group">
                                                          <label class="control-label col-md-4">API名称</label>
                                                          <div class="col-md-8">
                                                              <input type="text" id="api_name" name="apiName" class="form-control" />
                                                          </div>
                                                      </div>
                                                  </div>
                                                  <!--/span-->
                                                  <div class="col-md-6">
                                                      <div class="form-group">
                                                          <label class="control-label col-md-6"></label>
                                                          <div class="col-md-3">
                                                             <input type="button" id="rss_query" class="btn blue" value="查&nbsp;&nbsp;询"/>&nbsp;&nbsp;
                                                          </div>
                                                           <div class="col-md-3">
                                                             <input type="reset" id="rss_reset" class="btn green" value="重&nbsp&nbsp;置"/>
                                                          </div>
                                                      </div>
                                                  </div>
                                                  <!--/span-->
                                              </div>
                                    	  </form>
                                    </div>
                                </div>
                                <!-- END Portlet PORTLET-->
							</div>
							<div class="col-md-12">
							    <!-- BEGIN Portlet PORTLET-->
                                <div class="portlet box green">
                                    <div class="portlet-title">
                                        <div class="caption">
                                            <i class="fa fa-gift"></i>结果区
                                        </div>
                                        <div class="tools">
                                            <a href="javascript:;" class="collapse"> </a>
                                        </div>
                                        <div class="actions">
                                        	<a name="rss_edit" class="btn blue btn-sm" data-toggle="modal" data-url="${ctx}/admin/rss/rssOperate.htm?method=add"><i class="fa fa-plus"></i>新增</a>
                                        </div>
                                    </div>
                                    <div class="portlet-body portlet-empty"> 
	                                   	 <table class="table table-striped table-bordered table-hover table-checkable order-column" id="rss_table">
                                             
	                                      </table>  
                                    </div>
                                </div>
                                <!-- END Portlet PORTLET-->
							</div>
						</div>
						<!-- END PAGE CONTENT INNER -->
					</div>
				</div>
				<!-- END PAGE CONTENT BODY -->
				<!-- END CONTENT BODY -->
			</div>
			<!-- END CONTENT -->
			<!-- 详情展示区 --> 
			<div id="rss_detail_div" class="modal modalextend fade bs-modal-lg" tabindex="-1" data-width="820">
			 </div>
		</div>
	</div>
	<!-- END CONTENT -->
	<%@include file="/WEB-INF/views/include/footer.jsp"%>
	<script src="${ctx}/js/modules/rss/rss.js" type="text/javascript"></script>
	<script>
		jQuery(document).ready(function() {
		    //设置对应的menu为选中状态
	        $(".hor-menu ul").find("li").filter(".menu-dropdown").eq(3).addClass("active");
	        $(".hor-menu ul").find("li").filter(".menu-dropdown").eq(3).find("li").eq(0).addClass("active");
			rss.init();
			$("#rss_query").on("click",function(){
				 rss.queryForm();
			});
		});
	</script>
</body>
</html>