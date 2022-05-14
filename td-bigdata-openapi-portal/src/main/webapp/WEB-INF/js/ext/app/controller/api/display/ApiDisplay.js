Ext.define('OpenApi.controller.api.display.ApiDisplay', {
	extend : 'Ext.app.Controller',
	
	views : [],
	refs : [{
		ref : 'addr',
		selector : '#addr'
	},{
		ref : 'pubArgs',
		selector : '#pubArgs'
	},{
		ref : 'reqArgs',
		selector : '#reqArgs'
	},{
		ref : 'resArgs',
		selector : '#resArgs'
	},{
		ref : 'dc',
		selector : '#apidismainview #apicontentview'
	},{
		ref : 'tree',
		selector : '#apidismainview #apistructview'
	},{
		ref : 'grid',
		selector : '#apidismainview #discontent grid'
	},{
		ref : 'main',
		selector : '#apidismainview > container'
	},{
		ref : 'apiBody',
		selector : '#apidismainview #apilistofsortview #opBody'
	},{
		ref : 'detailTitle',
		selector : '#apidismainview #apidetailview label[panelTitle=true]'
	},{
		ref : 'detail',
		selector : '#apidismainview #apidetailview #opBody'
	},{
		ref : 'searchList',
		selector : '#apidismainview #apisearchresultview'
	}],
	init : function() {
		this.control({
			'#apidismainview #apistructview #opBody' : {
				add : this.itemAdd
			},
			'#apidismainview #discontent grid' : {
				select : this.itemclick,
				afterlayout : this.afl
			},
			'#apidismainview #apistructview' : {
				afterrender : this.treear
			},
			'#apidismainview #apilistofsortview #opBody' : {
				add : this.detailListAdd
			},
			'#apidismainview #apilistofsortview button' : {
				click : this.goup
			},
			'#apidismainview #apidetailview #opBody' : {
				add : this.detailAdd
			}
		})
	},
	onLaunch : function() {
	},
	afl : function(grid, layout, eOpts) {
		if(apiId != -100 && !grid.autoSel && grid.getStore().data.items.length > 0) {
			var index = grid.getStore().findBy(function(record, id) {
				if(record.get("apiId") == apiId && record.get("apiVersion") == apiVersion) {
					return true;
				} else {
					return false;
				}
			});
			grid.autoSel = true;
			grid.getSelectionModel().select(grid.getStore().getAt(index));
			this.getTree().down("#opBody").down("#api_tree").getSelectionModel().deselectAll();
		}
	},
	detailAdd : function(opBody, component, index, eOpts) {
		var scope = this;
		Ext.Ajax.request({
		    url: (projRoot + '/apiDisplay/getPubInfo.json'),
		    success: function(response, opts) {
		        var obj = Ext.decode(response.responseText);
		        if(obj.retCode) {
		    		component.down("#addr").getStore().loadRawData(obj.addData);
		    		component.down("#pubArgs").getStore().loadRawData(obj.addData1);
		        } else {
		        	Ext.example.msg('提示', '地址与公共参数加载失败！');
		        }
		    },
		    failure: function(response, opts) {
		        Ext.example.msg('提示', obj.retMsg);
		    }
		});
	},
	itemAdd : function(opBody, component, index, eOpts) {
		var scope = this;
		var ml = scope.getMain().getLayout();
		var defaultSel = null;
		
		if(apiSort != -100) {
			defaultSel = apiSort;
		} else {
			defaultSel = 1;
		}
		component.addListener("afterlayout", function(me, layout, eOpts) {
			if(component.getStore().getNodeById(defaultSel) && !component.isInited) {
				component.isInited = true;
				component.getSelectionModel().select(component.getStore().getNodeById(defaultSel), false, false);
			}
		});
		
		component.down("trigger").addListener("keyup", function(textfield, e, eOpts) {
			if(e.getKey() == e.RETURN) {
				if(scope.getSearchList().down("button").hasListener("click")) {
					scope.getSearchList().down("button").removeListener("click", scope.forback, scope.getSearchList().down("button"), scope);
				}
				scope.getSearchList().down("button").addListener("click", scope.forback, scope.getSearchList().down("button"), scope);
				component.getSelectionModel().deselectAll();
				Ext.create("OpenApi.store.ApiSearchStore", {
					listeners : {
						beforeload : function(store, operation, eOpts) {
							store.getProxy().extraParams = {
								searchText : Ext.encode(textfield.getValue())
							};
						},
						load : function(store, records, successful, eOpts) {
//							console.log(store.getProxy().getReader().jsonData);
							scope.getGrid().getStore().loadData(store.getProxy().getReader().jsonData.addData);
							var slist = scope.getSearchList().down("#search_list");
							slist.removeAll();
							var p = new RegExp(eval("/(" + textfield.getValue() + ")/gi"));
							records.forEach(function(element, index, array) {
								var item = Ext.create("OpenApi.ui.ApiSearchResultItem", {
									record : scope.getGrid().getStore().getAt(index),
									apiName : ("名称：" + element.raw.apiName.replace(p, "<span style='color:red;font-style:bolder;'>$1</span>")),
									apiDesc : ("描述：" + element.raw.apiDesc.replace(p, "<span style='color:red;font-style:bolder;'>$1</span>")),
									listeners : {
										click : function() {
											ml.setActiveItem(0);
											scope.getGrid().getSelectionModel().select(this.record);
										}
									}
								});
								var argList = item.down("#argsList");
								var filtedArgs = element.raw.structArgs.filter(function(e, i, a) {
									return (e.fieldName.search(p) != -1 || e.fieldFileDesc.search(p) != -1);
								});
								if(filtedArgs.length > 0) {
									filtedArgs.forEach(function(element1, index1, array1) {
										var argItem = Ext.create("Ext.container.Container", {
											layout : {
												type : 'vbox',
												align : 'stretch'
											},
											items : [{
												xtype : 'label',
								        		html : ("参数名称：" + element1.fieldName.replace(p, "<span style='color:red;font-style:bolder;'>$1</span>")),
								        		baseCls : 'common_click_cursor',
								        		cls : 'search_result_title'
											},{
												xtype : 'label',
								        		html : ("参数描述：" + element1.fieldFileDesc.replace(p, "<span style='color:red;font-style:bolder;'>$1</span>")),
								        		baseCls : 'common_click_cursor',
								        		cls : 'search_result_title'
											}]
										});
										argList.add(argItem);
									});
								} else {
									var argItem = Ext.create("Ext.container.Container", {
										layout : {
											type : 'vbox',
											align : 'stretch'
										},
										items : [{
											xtype : 'label',
							        		html : '无匹配参数',
							        		baseCls : 'common_click_cursor',
							        		cls : 'search_result_title'
										}]
									});
									argList.add(argItem);
								}
								slist.add(item);
							});
							ml.setActiveItem(2);
						}
					}
				});
			} else if(e.getKey() == e.ESC) {
				textfield.setValue("");
			}
		});
		
		component.addListener("select", function(view, record, item, index, e, eOpts) {
			scope.getDc().down("label").setText(record.raw.text);
			if(apiId == -100 || component.afirst) {
				// scope.getGrid().getStore().loadPage(1, {
				// 	params : {
				// 		'apiSort' : record.get("id"),
				// 		'apiId' : null,
				// 		'apiVersion' : null
				// 	}
				// });
				scope.getGrid().getStore().getProxy().extraParams = {
					'apiSort' : record.get("id"),
					'apiId' : null,
					'apiVersion' : null
				};
				scope.getGrid().getStore().loadPage(1);
			} else {
				// scope.getGrid().getStore().loadPage(1, {
				// 	params : {
				// 		'apiSort' : record.get("id"),
				// 		'apiId' : apiId,
				// 		'apiVersion' : apiVersion
				// 	}
				// });
				scope.getGrid().getStore().getProxy().extraParams = {
					'apiSort' : record.get("id"),
					'apiId' : apiId,
					'apiVersion' : apiVersion
				};
				scope.getGrid().getStore().loadPage(1);
				component.afirst = true;
			}
		});
	},
	itemclick : function(rowmodel, record, index, eOpts) {
		var scope = this;
		var reqArgs = [];
		var resArgs = [];
		record.raw.structArgs.forEach(function(element, index, array) {
			if(element.reqArgId == '1') {
				reqArgs.push(element);
			}
			if(element.respnArgId == '1') {
				resArgs.push(element);
			}
		});
		scope.getDetail().down("#reqArgs").getStore().loadRawData(reqArgs);
		scope.getDetail().down("#resArgs").getStore().loadRawData(resArgs);
		
		var records = scope.getGrid().getStore().getRange(0, scope.getGrid().getStore().getCount());
		var list = this.getApiBody().down("container");
		list.removeAll(true);
		records.forEach(function(element, index, array) {
			var text = element.get("apiName") + "<br/>" + element.get("apiDesc");
			list.add({
				xtype : 'apilistitem',
				html : text,
				nodeData : element,
				baseCls : 'apilisttem_font',
				overCls : 'apilisttem_mouseover',
				padding : '15 15 15 15',
				listeners : {
					click : function(e, t, eOpts) {
						var lItem = this;
						this.up("container").items.each(function(item, index, length) {
							item.removeCls("apilisttem_selected");
						});
						this.addCls("apilisttem_selected");
						var text = this.nodeData.get("apiName") + "(" + this.nodeData.get("apiDesc") + ")";
						scope.getDetailTitle().setText(text);
						
						var reqArgs1 = [];
						var resArgs1 = [];
						this.nodeData.raw.structArgs.forEach(function(element, index, array) {
							if(element.reqArgId == '1') {
								reqArgs1.push(element);
							}
							if(element.respnArgId == '1') {
								resArgs1.push(element);
							}
						});
						scope.getDetail().down("#reqArgs").getStore().loadRawData(reqArgs1);
						scope.getDetail().down("#resArgs").getStore().loadRawData(resArgs1);
						if(scope.getDetail().down("button[action=apiTest]").hasListener("click")) {
							scope.getDetail().down("button[action=apiTest]").removeListener("click", scope.testApi, scope);
						}
//						console.log(lItem.nodeData.raw);
						scope.getDetail().down("button[action=apiTest]").addListener("click", scope.testApi, scope, lItem.nodeData.raw);
					},
					afterrender : function(me, eOpts) {
						var lItem = this;
						if(record.get("apiId") == me.nodeData.get("apiId") && 
								record.get("apiVersion") == me.nodeData.get("apiVersion")) {
							if(!me.hasCls("apilisttem_selected")) {
								me.addCls("apilisttem_selected");
								if(scope.getDetail().down("button[action=apiTest]").hasListener("click")) {
									scope.getDetail().down("button[action=apiTest]").removeListener("click", scope.testApi, scope);
								}
//								console.log(lItem.nodeData.raw);
								scope.getDetail().down("button[action=apiTest]").addListener("click", scope.testApi, scope, lItem.nodeData.raw);
							}
						}
					}
				}
			});
		});
		this.getDetailTitle().setText(record.get("apiName") + "(" + record.get("apiDesc") + ")");
		var ml = this.getMain().getLayout();
		ml.setActiveItem(1);
	},
	treear : function(tree, eOpts) {
	},
	detailListAdd : function(opBody, component, index, eOpts) {
		
	},
	goup : function() {
		var ml = this.getMain().getLayout();
		ml.setActiveItem(0);
	},
	testApi : function(button, e, eOpts) {
		//alert("sortId=" + eOpts.apiSort + "&apiId=" + eOpts.apiId + "&apiName=" + eOpts.apiName + "&apiVersion=" + eOpts.apiVersion);
		location.href = (projRoot + "/apitools/apitools.htm?sourceFlag=1&sortId=" + eOpts.apiSort + "&apiId=" + eOpts.apiId + "&apiName=" + eOpts.apiName + "&apiVersion=" + eOpts.apiVersion);
	},
	forback : function(button, e, eOpts) {
		var ml = eOpts.getMain().getLayout();
		ml.setActiveItem(0);
	}

});