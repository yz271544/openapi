Ext.define('OpenApi.controller.document.publish.DocumentPublishView', {
	extend : 'Ext.app.Controller',
	
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
			if(component.getItemId() == "dpv") {
				component.down("button[action=publish]").addListener("click", function(publishBtn, e, eOpts) {
					var title = component.down("textfield[name=docTitle]").getValue();
					var toNode = component.down("treecombo").getValue();
					var content = component.down("htmleditor").getValue();
					if(title == null || title == '') {
						Ext.example.msg('提示', '[标题]不能为空！');
					} else if(toNode == null || toNode == ''){
						Ext.example.msg('提示', '[发布到]不能为空！');
					} else if(content == null || content == '') {
						Ext.example.msg('提示', '[正文]不能为空！');
					} else {
						var doc = {
							nodeId : null,
							fathrNode : toNode,
							isleafNode : 1,
							nodeOrder : null,
							nodeDesc : title,
							effDate : null,
							fileTitle : title,
							fileDate : null,
							fileAuthor : 'test',
							fileContent : content
						};
						Ext.Ajax.request({
						    url: (projRoot + '/documentPublish/addDoc.json'),
						    params : {
						    	'docJson' : Ext.encode(doc)
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
					}
				});
			}
		}, this);
	}
})