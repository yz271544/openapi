Ext.define('OpenApi.view.Viewport',{ 
    extend: 'Ext.Viewport', 
    requires : [], 
    initComponent : function(){ 
        var me = this; 
        Ext.apply(me, { 
			id:'desk',
			autoScroll : true,
			overflowY : 'auto',
			layout: {
				type : 'border'
			},
			border : 0,
			items: [{
				xtype : 'container',
				region: 'north',
				layout : {
					type : 'vbox',
					align : 'stretch'
				},
				items : [
					Ext.create('OpenApi.view.Banner'),
					Ext.create('OpenApi.view.ConsoleMenu')
				]
			},Ext.create('OpenApi.view.MainView'),{
				xtype : 'container',
				region: 'south',
				layout : {
					type : 'vbox',
					align : 'stretch'
				},
				items : [
					Ext.create('OpenApi.view.OAPageFooter'),
					Ext.create('OpenApi.view.OAVersionInfo')
				]
			}]
        });
        me.callParent(arguments); 
    } 
}); 
