Ext.define('OpenApi.view.document.manage.ApiManaView', { 
    extend: 'OpenApi.ui.BaseView', 
    requires : [
                'OpenApi.ui.Kwb.treegrid.DocumentTreeGrid'
	],
    
    initComponent: function() { 
        Ext.apply(this, {
        	id : 'apimanaview',
        	title : 'API目录管理'
        }); 
        this.callParent(arguments); 
    },
    onRender : function() {
		this.callParent(arguments);
		var store = Ext.create("OpenApi.store.ApiManaStore");
		this.getComponent("opBody").add({
			xtype : 'doctreegrid',
			itemId : 'am_dtg',
			store : store
		});
	}

}); 