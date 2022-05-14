Ext.define('OpenApi.ui.Kwb.plugins.HtmlEditorPreview', {
	extend : 'Ext.tree.Panel',
	alternateClassName : [ 'OpenApi.ui.Kwb.plugins.HtmlEditorPreview' ],
	alias : [ 'widget.htmleditorimgctrl' ],
	requires : [],
	tipTitle : '预览',
	preview : null,
	ctrlCls : 'htmleditor_preview',

	init : function(view) {
		var scope = this;
		view.on("render", function() {
			scope.onRender(view);
		});
	},

	onRender : function(view) {
		var scope = this;
		view.getToolbar().add({
			id : 'hepreviewbtn',
			iconCls : scope.ctrlCls,
			tooltip : {
				title : scope.tipTitle,
				width : 60
			},
			handler : function() {
				if(!scope.preview) {
					scope.preview = Ext.create("OpenApi.ui.Kwb.plugins.HtmlEditorPreviewWindow", {
						id : 'hepreview',
						title : '效果预览',
						width : (Ext.getBody().getWidth() / 2),
						height : 500,
						closeable : true
					});
					scope.preview.getComponent("opBody").add({
						xtype : 'container',
						height : 300,
						autoScroll : true,
						overflowY : 'auto'
					});
				}
				if(!scope.isVisible()) {
//					console.log(view.getValue());
					scope.preview.show();
					scope.preview.getComponent("opBody").down("container").getEl().setHTML(view.getValue());
				} else {
					scope.preview.hide();
				}
			}
		});
	}

});