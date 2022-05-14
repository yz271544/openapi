Ext.define('OpenApi.view.document.manage.OpenApiPlatformManaView', { 
    extend: 'OpenApi.ui.BaseView', 
    requires : [
                'OpenApi.ui.Kwb.treegrid.DocumentTreeGrid'
	],
    
    initComponent: function() { 
        Ext.apply(this, {
        	id : 'openapiplatformmanaview',
        	title : '开放平台目录管理'
        }); 
        this.callParent(arguments); 
    },
	onRender : function() {
		this.callParent(arguments);
		var store = Ext.create("OpenApi.store.OpenApiPlatformDirsStore");
		this.getComponent("opBody").add({
			xtype : 'doctreegrid',
			itemId : 'oapm_dtg',
			store : store
		});
	}

}); 