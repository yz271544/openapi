var View = function() {
	var handleToolTip = function() {
		$('.toolTip').tooltip();
	}
	
	var handleStateDesc = function(){
		var $apiTotal = $('#apiTotal');
		var $visitedNum = $('#visitedNum');
		var $visitedTime = $('#visitedTime');
		var $avgTime = $('#avgTime');
		
		App.blockUI({
			target : '#stateDesc',
			message : '加载中。。。'
		})
		var data_url = $.basePath + '/home/getStateCount.json';
		var ifGroup = $('ul.sort-group li.font-green').attr('ifGroup');
		var data_param = {'ifGroup':ifGroup};
		$.ajax({
			url : data_url, // 提交到一般处理程序请求数据
			type : "POST",
			data : data_param,
			dataType : "json",
			success : function(data) {
				if(data[0] != null){
					var item = data[0];
					var apiTotal = item.apiTotal;
					var visitedNum = item.visitedNum;
					var visitedTime = item.visitedTime;
					var avgTime = item.avgTime;
					$apiTotal.text(apiTotal);
					$visitedNum.text(visitedNum);
					$visitedTime.text(visitedTime);
					$avgTime.text(avgTime);
				}else{
					$apiTotal.text('0');
					$visitedNum.text('0');
					$visitedTime.text('0');
					$avgTime.text('0');
				}
				App.unblockUI('#stateDesc');
			},
			error : function(data, e) {alert(12);
				bootbox.alert("系统错误,请稍候再试！");
			}
		});
	}
	
	var initPortletBody2 = function(){
		if(!$.isIE()){
			$('#nav_tabs_chart a:first').tab('show');
		}else{
			setTimeout(function() {
				$('#nav_tabs_chart a:first').tab('show');
			}, 300);
		}
	}
	
	var initPortletBody3 = function(){
		$('#sel_apiSort').getSelect($.basePath+'/apiRelease/getSelect.json', {method:'getApiSort'},{valueField:'apiSort', textField:'apiSortName'}, '');
	}

	var initChart = function(chartId) {
		var myChart = echarts.init($('#' + chartId)[0]);

		var option = {
			color : [ '#3398DB', '#92D050', '#FFC000' ],
			legend : {
				x : 'center',
				data : [ '同步', '异步', '订阅' ]
			},
			tooltip : {
				trigger : 'axis',
				axisPointer : { // 坐标轴指示器，坐标轴触发有效
					type : 'shadow' // 默认为直线，可选为：'line' | 'shadow'
				}
			},
			toolbox : {
				feature : {
					magicType : {
						show : true,
						type : [ 'bar', 'line' ]
					}
				}
			},
			grid : {
				left : 0,
				right : 15,
				bottom : 35,
				containLabel : true
			},
			xAxis : [ {
				type : 'category',
				data : [],
				axisTick : {
					alignWithLabel : true
				}
			} ],
			yAxis : [ {
				type : 'value'
			} ],
			dataZoom : [
					{
						type : 'inside',
						start : 0,
						end : 100
					},
					{
						start : 0,
						end : 10,
						handleIcon : 'M10.7,11.9v-1.3H9.3v1.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4v1.3h1.3v-1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z',
						handleSize : '80%',
						handleStyle : {
							color : '#3398DB',
							shadowBlur : 3,
							shadowColor : 'rgba(0, 0, 0, 0.6)',
							shadowOffsetX : 2,
							shadowOffsetY : 2
						},
						bottom : 0
					} ],
			series : []
		};
		myChart.setOption(option);
		return myChart;
	}
	
	var handlePortletBody2 = function() {
		App.blockUI({
			target : '#portlet_body_2',
			message : '加载中...'
		});
		
		$target = $('#nav_tabs_chart li.active a');
		var chartId = $target.attr('chartId');
		var itemId = $target.attr('itemId');
		var chartUnit = $target.attr('chartUnit');

		var myChart = echarts.getInstanceByDom($('#' + chartId)[0]);
		if (myChart == undefined) {
			myChart = initChart(chartId);
		}

		var data_url = $.basePath + '/home/getOwnApiChart.json';
		var ifGroup = $('ul.sort-group li.font-green').attr('ifGroup');
		var data_param = {
			itemId : itemId,
			ifGroup : ifGroup
		};
		$.ajax({
			url : data_url, // 提交到一般处理程序请求数据
			type : "POST",
			data : data_param,
			dataType : "json",
			success : function(data) {
				var category = new Array();
				var syncData = new Array();
				var asyncData = new Array();
				var bookData = new Array();
				$.each(data, function(index, item) {
					var dealDate = item.dealDate;
					var valSync = item.valSync;
					var valAsync = item.valAsync;
					var valBook = item.valBook;
					category.push(dealDate);
					syncData.push(valSync);
					asyncData.push(valAsync);
					bookData.push(valBook);
				});

				myChart.setOption({
					xAxis : [ {
						data : category
					} ],
					yAxis : [ {
						name : '单位：' + chartUnit
					} ],
					series : [ {
						name : '同步',
						type : 'bar',
						data : syncData
					}, {
						name : '异步',
						type : 'bar',
						data : asyncData
					}, {
						name : '订阅',
						type : 'bar',
						data : bookData
					} ]
				});

				App.unblockUI('#portlet_body_2');
			},
			error : function(data, e) {
				bootbox.alert("系统错误,请稍候再试！");
			}
		});
	}

	var pageIndex = 0; //页面索引初始值   
	var pageSize = 5; //每页显示条数初始化，修改显示条数，修改这里即可
	var handlePortletBody3 = function() {
		var pg_detail = 'pg_detail';
		var pg_total = 'pg_total';
		var tb_id = 'tb_detail';
		var data_url = $.basePath + '/home/getApiStatus.json';
		var viewType = $('#viewType').val();
		var ifGroup = $('ul.sort-group li.font-green').attr('ifGroup');
		var data_param = {method : 'getApiStatus', viewType : viewType, ifGroup : ifGroup};
		var form_param = $('#form_search').getFormJson();
		
		initTable(0); //Load事件，初始化表格数据，页面索引为0（第一页）

		function pageInit(totalCount, pageIndex) {
			//分页，totalCount是总条目数，这是必选参数，其它参数都是可选
			$('#' + pg_detail).pagination(totalCount, {
				callback : pageCallback, //PageCallback() 为翻页调用次函数。
				prev_text : "« 上一页",
				next_text : "下一页 »",
				items_per_page : pageSize,
				num_edge_entries : 1, //两侧首尾分页条目数
				num_display_entries : 6, //连续分页主体部分分页条目数
				current_page : pageIndex, //当前页索引
			});

			var startIndex = (pageIndex * pageSize + 1);
			var endIndex = (pageIndex + 1) * pageSize > totalCount ? totalCount
					: (pageIndex + 1) * pageSize;
			$('#' + pg_total).html(
					"当前第&nbsp;" + startIndex + '-' + endIndex
							+ '&nbsp;条&nbsp;&nbsp;共计&nbsp;' + totalCount
							+ '&nbsp;条');
		}
		//翻页调用   
		function pageCallback(index, jq) {
			initTable(index);
		}
		//请求数据   
		function initTable(pageIndex) {
			App.blockUI({
				target : '#portlet_body_3',
				message : '加载中...'
			});
			var totalCount;//总条目数
			var itemNum = pageIndex * pageSize + 1;//当前页开始记录行数
			$.ajax({
				url : data_url, //提交到一般处理程序请求数据    
				type : "POST",
				//分页参数：pageIndex(页面索引)，pageSize(显示条数) 
				data : $.extend({}, data_param, {
					'pageIndex' : pageIndex,
					'pageSize' : pageSize
				}, form_param),
				dataType : "json",
				success : function(data) {
					totalCount = data.totalCount;
					pageInit(totalCount, pageIndex);
					$('#' + tb_id + ' tr:gt(0)').remove(); //移除Id为Result的表格里的行，从第二行开始（这里根据页面布局不同页变）   
					$.each(data.items, function(i, item) {
						var sb = new StringBuffer();
						var apiSortName = item.apiSortName;
						var apiName = item.apiName;
						var apiDesc = item.apiDesc;
						var apiVisitMethdDesc = item.apiVisitMethdDesc;
						var visitTimes = item.visitTimes;
						var avgRespnDur = item.avgRespnDur;
						var apiStatDesc = item.apiStatDesc;
						var earliestUseTime = item.earliestUseTime;
						var mostRecUseTime = item.mostRecUseTime;

						sb.append('<tr>');
						sb.append('<td>' + (itemNum++) + '</td>');
						sb.append('<td>' + apiSortName + '</td>');
						sb.append('<td>' + apiName + '</td>');
						sb.append('<td>' + apiDesc + '</td>');
						sb.append('<td>' + apiVisitMethdDesc + '</td>');
						sb.append('<td>' + visitTimes + '</td>');
						sb.append('<td>' + avgRespnDur + '</td>');
						sb.append('<td>' + apiStatDesc + '</td>');
						sb.append('<td>' + earliestUseTime + '</td>');
						sb.append('<td>' + mostRecUseTime + '</td>');
						sb.append('</tr>');

						$('#' + tb_id + ' tbody').append(sb.toStr());
					});

					App.unblockUI('#portlet_body_3');
				},
				error : function(data, e) {
					bootbox.alert("系统错误,请稍候再试！");
				}
			});
		}
	}

	return {
		init : function() {
			//查询范围切换（个人、开发组）
			$(document).on('click', 'ul.sort-group li', function(){
				$ul = $(this).closest('ul.sort-group');
				$ul.find('li').removeClass('font-green');
				$(this).addClass('font-green');
				
				handleStateDesc();
				handlePortletBody2();
				handlePortletBody3();
			});
			
			//绑定事件：tab页切换加载页面
			$('#nav_tabs_chart a[data-toggle="tab"]').on('shown.bs.tab',
					function(e) {
						handlePortletBody2();
					});
			
			//绑定事件：表单查询
			$(document).on('click', '#btn_search, input[name=apiStat]', function(){
				handlePortletBody3();
			});
			
			//绑定事件：表单重置
			$(document).on('click', '#btn_reset', function(){
				$('#form_search')[0].reset();
				$('#btn_search').trigger('click');
			});

			handleToolTip();
			handleStateDesc();
			initPortletBody2();
			initPortletBody3();
			handlePortletBody3();
		}
	}
}();
$(function() {
	View.init();
	if(!$.isIE()){//ie8不起作用
		window.onresize = function() {
	        $(".chart-type-default").each(function(){
	            var id = $(this).attr('_echarts_instance_');
	            if(id == undefined) return true;
	            window.echarts.getInstanceById(id).resize();
	        });
	    };
	}
});