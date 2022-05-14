Ext.define('OpenApi.ui.Kwb.plugins.HtmlEditorPreviewWindow', { 
    extend: 'OpenApi.ui.BaseView', 
    requires : [],
    
    initComponent: function() { 
        Ext.apply(this, {
        	floating : true,
        	shadow : true,
        	layout : {
        		type : 'vbox',
        		align : 'stretch'
        	},
        	style : {
        		backgroundColor : '#55616f'
        	}
        }); 
        this.callParent(arguments); 
    },
	onRender : function() {
		this.callParent(arguments);
	}

}); 