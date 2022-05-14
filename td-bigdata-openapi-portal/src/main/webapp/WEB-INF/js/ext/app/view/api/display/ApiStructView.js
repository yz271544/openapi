Ext.define('OpenApi.view.api.display.ApiStructView', { 
    extend: 'OpenApi.ui.BaseView', 
    requires : ['OpenApi.ui.Kwb.textfield.ClearTextField'],
    
    initComponent: function() { 
        Ext.apply(this, {
        	id : 'apistructview',
        	title : '所有类目',
        }); 
        this.callParent(arguments); 
    },
    onRender : function() {
		this.callParent(arguments);
		var store = Ext.create("OpenApi.store.ApiManaStore");
		var tree = Ext.create("Ext.tree.Panel", {
			itemId : 'api_tree',
			border : false,
			width : 300,
			height : 360,
			autoScroll : true,
			rootVisible : false,
			selModel : Ext.create('Ext.selection.RowModel'),
			store : store,
			tbar : [{
				xtype : 'cleartextfield',
				emptyText : '关键字搜索...',
				flex : 1,
				enableKeyEvents : true
			}]
		});
		this.getComponent("opBody").add(tree);
	}

}); 