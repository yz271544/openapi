Ext.define('OpenApi.view.document.display.DocStructView', { 
    extend: 'OpenApi.ui.BaseView', 
    requires : ['OpenApi.ui.Kwb.textfield.ClearTextField'],
    
    initComponent: function() { 
        Ext.apply(this, {
        	id : 'docstructview',
        	title : '开放平台',
        }); 
        this.callParent(arguments); 
    },
    onRender : function() {
		this.callParent(arguments);
		var store = Ext.create("OpenApi.store.PlatformDisStore");
		this.getComponent("opBody").add({
			xtype : 'treepanel',
			border : false,
			width : 300,
			height : 360,
			autoScroll : true,
			rootVisible : false,
			store : store,
			tbar : [{
				xtype : 'cleartextfield',
				emptyText : '关键字搜索...',
				flex : 1,
				enableKeyEvents : true
			}]
		});
	}

}); 