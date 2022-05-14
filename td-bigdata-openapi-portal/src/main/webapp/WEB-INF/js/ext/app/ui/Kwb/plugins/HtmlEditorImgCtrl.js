Ext.define('OpenApi.ui.Kwb.plugins.HtmlEditorImgCtrl', {
	extend : 'Ext.tree.Panel',
	alternateClassName : [ 'OpenApi.ui.Kwb.plugins.HtmlEditorImgCtrl' ],
	alias : [ 'widget.htmleditorimgctrl' ],
	requires : [],
	tipTitle : '插入图片',
	ctrlCls : 'htmleditor_image',

	init : function(view) {
		var scope = this;
		view.on("render", function() {
			scope.onRender(view);
		});
	},

	onRender : function(view) {
		var scope = this;
		view.getToolbar().add({
			iconCls : scope.ctrlCls,
			tooltip : {
				title : scope.tipTitle,
				width : 60
			},
			handler : function() {
				
			}
		});
	}

});