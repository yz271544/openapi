Ext.define('OpenApi.controller.document.display.DocDisplay', {
	extend : 'Ext.app.Controller',
	
	views : [],
	refs : [{
		ref : 'dc',
		selector : '#docdismainview #docontentview'
	},{
		ref : 'content',
		selector : '#docdismainview #discontent'
	},{
		ref : 'card',
		selector : '#docdismainview #docontentview #card_lay'
	},{
		ref : 'list',
		selector : '#docdismainview #docontentview #card_lay #search_list'
	}],
	init : function() {
		this.control({
			'#docdismainview #docstructview #opBody' : {
				add : this.itemAdd
			}
		})
	},
	onLaunch : function() {
	},
	itemAdd : function(opBody, component, index, eOpts) {
		var scope = this;
		component.down("trigger").addListener("keyup", function(textfield, e, eOpts) {
			if(e.getKey() == e.RETURN) {
				if(textfield.getValue().trim() != "") {
					component.getSelectionModel().setSelectionMode("MULTI");
					component.getSelectionModel().selectAll();
					var sels = component.getSelectionModel().getSelection();
					component.getSelectionModel().deselectAll();
					component.getSelectionModel().setSelectionMode("SINGLE");
					component.getSelectionModel().select(sels[0]);
					var matchStr = new RegExp(eval("/" + textfield.getValue() + "/i"));
					var document = sels.filter(function(element, index, array) {
						return ((element.data.leaf) && 
								((element.raw.text.match(matchStr)) || 
								(element.raw.nodeDesc.match(matchStr))));
					});
					if(document.length > 0) {
						scope.getList().removeAll();
						document.forEach(function(element, index, array) {
							var content = element.raw.nodeDesc.replace(/((<(\S*) [^>]*>)|<(\S*)>|\&nbsp;)/g, "");
							var p = new RegExp(eval("/(" + textfield.getValue() + ")/gi"));
							var start = content.search(p) - 200 < 0 ? 0 : content.search(p) - 200;
							var end = (content.search(p) + 200 > content.length - 1) ? content.length - 1 : content.search(p) + 200;
							content = "……" + content.substring(start, end) + "……";
							content = content.replace(p, "<span style='color:red;font-style:bolder;'>$1</span>");
							scope.getList().add(Ext.create("OpenApi.ui.SearchResultItem", {
								records : element.raw,
								title : ("<b>文档名称：</b>" + element.raw.text),
								content : ("<b>文档内容：</b><br/>" + content),
								listeners : {
									click : function(e, t, eOpts) {
										console.log(this.records);
										component.getSelectionModel().select(component.getStore().getNodeById(this.records.id));
										scope.getCard().getLayout().setActiveItem(0);
									}
								}
							}));
						});
					} else {
						scope.getList().removeAll();
						scope.getList().add(Ext.create("OpenApi.ui.SearchResultItem", {
							title : '',
							content : '没有符合条件的结果'
						}));
					}
					scope.getCard().getLayout().setActiveItem(1);
				}
			} else if(e.getKey() == e.ESC) {
				textfield.setValue("");
			}
		});
		component.down("trigger").addListener("change", function(me, newValue, oldValue, eOpts) {
			if(newValue == "") {
				scope.getCard().getLayout().setActiveItem(0);
			}
		});
		component.addListener("select", function(view, record, item, index, e, eOpts) {
			if(record.raw.leaf) {
				scope.getDc().down("label").setText(record.raw.text);
				scope.getContent().getEl().setHTML(record.raw.nodeDesc);
			}
		});
		var defaultSel = null;
		if(nodeId != -100) {
			defaultSel = nodeId;
		} else {
			defaultSel = 10016;
		}
		component.addListener("afterlayout", function(me, layout, eOpts) {
			if(component.getStore().getNodeById(defaultSel) && !component.isInited) {
				component.isInited = true;
				component.getSelectionModel().select(component.getStore().getNodeById(defaultSel), false, false);
			}
		});
	}
})