Ext.define('OpenApi.controller.document.manage.ApiManaView', {
	extend : 'Ext.app.Controller',
	
	id : 'apictrl',
	views : [],
	refs : [{
		ref : 'op',
		selector : '#mainview > container'
	}],
	init : function() {
		this.control({
			'#mainview > container' : {
				add : this.viewAdd
			}
		})
	},
	onLaunch : function() {
	},
	viewAdd : function(view, component, index, eOpts) {
		component.getComponent("opBody").addListener("add", function(opBody, component, index, eOpts) {
			if(component.getItemId() == "am_dtg") {
				component.down("button").addListener("click", function(saveBtn, e, eOpts) {
					var newRecord = new Array();
					component.getStore().getNewRecords().forEach(function(element, index, array) {
						var ret = Ext.clone(element.raw);
						Ext.copyTo(ret, element.data, Object.keys(ret));
						newRecord.push(ret);
					});
//					console.log(newRecord);
					var updateRecord = new Array();
					component.getStore().getUpdatedRecords().forEach(function(element, index, array) {
						var ret = Ext.clone(element.raw);
						Ext.copyTo(ret, element.data, Object.keys(ret));
						updateRecord.push(ret);
					});
//					console.log(updateRecord);
					var delRecord = new Array();
					component.getStore().getRemovedRecords().forEach(function(element, index, array) {
						var ret = Ext.clone(element.raw);
						Ext.copyTo(ret, element.data, Object.keys(ret));
						delRecord.push(ret);
					});
//					console.log(delRecord);
					Ext.Ajax.request({
					    url: (projRoot + '/documentPublish/modifySortDirStruct.json'),
					    params : {
					    	'addJson' : Ext.encode(newRecord),
					    	'updJson' : Ext.encode(updateRecord),
					    	'delJson' : Ext.encode(delRecord)
					    },
					    success: function(response, opts) {
					        var obj = Ext.decode(response.responseText);
					        if(obj.retCode) {
					    		Ext.example.msg('提示', obj.retMsg);
					    		component.getStore().reload();
					        } else {
					        	Ext.example.msg('提示', obj.retMsg);
					        }
					    },
					    failure: function(response, opts) {
					        Ext.example.msg('提示', obj.retMsg);
					    }
					});
				});
				component.addListener("itemcontextmenu", function(me, record, item, index, e, eOpts) {
	        		e.preventDefault();
	        		e.stopEvent();
	        		var curNode = (record.phantom) ? 
	        				component.getRootNode().findChild("tempId", record.raw.tempId, true) : 
	        					component.store.getNodeById(record.getId());
	        		var contextMenu = Ext.create("Ext.menu.Menu", {
	        			items : [{
	        				text : '新增目录',
	        				handler : function() {
	        					if(record.get("leaf")) {
	        						Ext.example.msg('提示', '此节点下不能添加目录！');
	        					} else {
	        						var pos = 0;
	        						if(curNode.childNodes.length > 0) {
	        							pos = curNode.lastChild.data.index + 1;
	        						}
	        						var newNode = {
	    								"id" : null,
	    								"tempId" : ((record.phantom ? record.get("tempId") : record.getId()) * 100 + pos),
	    								"root" : false,
	    								"parentId" : record.getId(),
	    								"leaf" : false,
	    								"index" : pos,
	    								"text" : "<b>新增节点</b>",
	    								"nodeDesc" : "<b>双击此处进行编辑</b>",
	    								"depth" : curNode.data.depth,
	    								"expandable" : false,
	    								"expanded" : false,
	    								"children" : null
	    							};
	        						curNode.appendChild(newNode);
	        						curNode.expand();
	        					}
	        				}
	        			},{
	        				text : '删除目录',
	        				handler : function() {
	        					var dels = component.getSelectionModel().getSelection();
	        					dels.forEach(function(element, index, array) {
	        						if(element.get("parentId") == -1) {
	        							Ext.example.msg('提示', '根部不能删除！');
	        						} else {
	        							var parentNode = component.getStore().getNodeById(element.get("parentId"));
	        							var node = parentNode.findChild("id", element.get("id"));
	            						if(node.hasChildNodes()) {
	                						Ext.example.msg('提示', '[' + node.data.nodeDesc + ']此目录下有子目录，不能删除！');
	                					} else {
	                						parentNode.removeChild(node);
	                					}
	        						}
	        					});
	        				}
	        			}]
	        		});
	        		contextMenu.showAt(e.getXY());
	        	}, component);
			}
		}, this);
	}
})