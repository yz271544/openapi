var rss = (function() {
	var oTable;

	var handleRecords = function() {
		oTable = $('#rss_table');

		if (typeof oTable == 'undefined' && oTable != null) {
			oTable.fnClearTable(0); // 清空数据
			oTable.fnDraw(); // 重新加载数据
		} else {
			oTable.dataTable({
				"oLanguage" : {
					"sProcessing" : '<i class="fa fa-coffee"></i>数据加载中...',
					"sLengthMenu": "每页显示 _MENU_条",
					"sZeroRecords" : "没有找到符合条件的数据",
					"sInfo" : "当前第 _START_ - _END_ 条　共计 _TOTAL_ 条",
					"sInfoEmpty" : "没有记录",
					"oPaginate" : {

						"sFirst" : "首页",
						"sPrevious" : "前一页",
						"sNext" : "后一页",
						"sLast" : "尾页"
					}
				},

				"pagingType" : "full_numbers",
				"bStateSave" : true,

				"lengthMenu" : [ [ 5, 15, 20, -1 ], [ 5, 15, 20, "All" ]],
				"aLengthMenu" : [ 5, 20, 30 ],
				"iDisplayStart" : 0,
				"iDisplayLength" : 5,
				"bProcessing" : true,
				"bServerSide" : true,
				"bPaginate" : true,// 是否显示分页器
				"bInfo" : true,// 是否显示表格的一些信息
				"bFilter" : false,// 是否启用客户端过滤器
				"bJQueryUI" : true,// 是否启用JQueryUI风格
				"bAutoWidth" : true,
				"bDestroy" : true,
				"bSort" : false,
				"sPaginationType" : "full_numbers",
				"aoColumns":[
		                     {'sTitle':'API名称',"mData":"apiName","sWidth": "26%",'sClass':'center'}
		                    ,{'sTitle':'API分类',"mData":"apiSortName","sWidth": "14%",'sClass':'center'}
		                    ,{'sTitle':'API版本',"mData":"apiVersion","sWidth": "2%",'sClass':'center'}
		                    ,{'sTitle':'数据周期',"mData":"dataCycleDesc","sWidth": "3%",'sClass':'center'}
		                    ,{'sTitle':'订阅开始时间',"mData":"rssStartTime","sWidth": "12%",'sClass':'center'}
		                    ,{'sTitle':'订阅结束时间',"mData":"rssEndTime","sWidth": "12%",'sClass':'center'}
		                    ,{'sTitle':'状态',"mData":"effDesc","sWidth": "5%",'sClass':'center'}
		                    ,{'sTitle':'操作',"mData":null,"sWidth": "26%",'sClass':'center'}

		                ],
                "columnDefs": [
                        {"aTargets":[4],"mRender":function(data,type,full){
                        	return getSmpFormatDateByLong(data,false);
                		}},
                		{"aTargets":[5],"mRender":function(data,type,full){
                			return getSmpFormatDateByLong(data,false);
                        }},
                        {"aTargets":[7],"mRender":function(data,type,full){
                            var operHtml = [];
                            var ajaxUrl = $.basePath + "/admin/rss/rssOperate.htm?rssId="+full.rssId;
                            operHtml.push('<a name="rss_edit" class="btn btn-outline btn-circle btn-sm purple" data-toggle="modal" data-url="'+ajaxUrl+'&method=detail"> <i class="fa fa-list"></i>详情</a>');
                            if(full.effFlag == '1'){
                            	operHtml.push('<a name="rss_edit" class="btn btn-outline btn-circle btn-sm purple" data-toggle="modal" data-url="'+ajaxUrl+'&method=edit"> <i class="fa fa-edit"></i>修改</a>'); 
                            	operHtml.push('<a href="javascript:void(0); rss.removeOrAddRss('+full.rssId+',0);" class="btn btn-outline btn-circle dark btn-sm black"> <i class="fa fa-trash-o"></i>解除订阅</a>');
                            }else{
                            	operHtml.push('<a href="javascript:void(0); rss.removeOrAddRss('+full.rssId+',1);" class="btn btn-outline btn-circle dark btn-sm black"> <i class="fa fa-plus"></i>订阅</a>');
                            }
                        	return operHtml.join(" ");
                        }}],
				"sAjaxSource" : $.basePath + "/admin/rss/queryRssInfoList.json",
				"fnServerData" : function(sSource, aoData, fnCallback) {
					for (var i = 0; i < aoData.length; i++) {
						if (aoData[i].name == 'iDisplayStart') {
							showIndex = aoData[i].value + 1;
						}

						if (aoData[i].name == 'iDisplayLength') {
							rows = aoData[i].value;
						}
					}
					if(showIndex > rows){
						page = showIndex % rows + 1;
					}else{
						page = 1;
					}
					$.ajax({
						"type" : "post",
						"url" : sSource,
						"dataType" : "json",
						"data" : $("#filter_form").serialize() + "&page=" + page + "&rows=" + rows,
						"success" : function(resp) {
							fnCallback(resp);
							$("a[name='rss_edit']").on('click', function(){
								  $('body').modalmanager('loading');
					              var el = $(this);
					              $("#rss_detail_div").load(el.attr('data-url'), '', function(){
					                  $("#rss_detail_div").modal("show");
					              });
				            });
						}
					});
				},
				"bSort" : false
			});
		}
	}

	
	return {
		init : function() {
			handleRecords();
			//初始化api分类
			var control = $('#api_sort');
			$.ajax({
				cache : false,
				url : $.basePath + "/apitools/getSelectJson.json?selectName=apiSort",
				type : "post",
				dataType : "json",
				success : function(data) {
					if (!showError(data)) {
						control.empty();// 清空下拉框
						control.append("<option value='-1'>全部</option>");
						$.each(data, function(i, item) {
							control.append("<option value='" + item.Value + "'>" + item.Text + "</option>");
						});
					}
				},
				error : function(data, e) {
					bootbox.alert("系统错误,请稍候再试！");
				}
			});
		},
		queryForm : function(){
			oTable.fnClearTable(0); // 清空数据
			oTable.fnDraw()
		},
		removeOrAddRss : function(rssId,effFlag){
			var info = "";
			if(effFlag ==0){
				info = "解除";
			}
			$.ajax({
				cache : false,
				url : $.basePath +"/admin/rss/saveRssInfo.json",
				type : "post",
				data : {
					rssId : rssId,
					effFlag : effFlag ,
					method : "operate"
				},
				dataType : "json",
				success : function(data) {
					if(data.retCode == '00'){
						bootbox.alert(info+"订阅成功！",function(){
							 rss.queryForm();
						});
					}else{
						bootbox.alert(info+"订阅失败！");
					}
				},
				error : function(data, e) {
					bootbox.alert("系统错误,请稍候再试！");
				}
			});
		}
	}
})();
