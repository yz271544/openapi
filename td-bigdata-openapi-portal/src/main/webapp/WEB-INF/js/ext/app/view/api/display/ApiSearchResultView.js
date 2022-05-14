Ext.define('OpenApi.view.api.display.ApiSearchResultView', { 
    extend: 'OpenApi.ui.BaseView', 
    requires : ['OpenApi.ui.ApiSearchResultItem'],
    
    initComponent: function() { 
        Ext.apply(this, {
        	id : 'apisearchresultview',
        	title : '搜索结果',
        	btnhidden : true
        }); 
        this.callParent(arguments); 
    },
    onRender : function() {
		this.callParent(arguments);
		this.getComponent("opBody").add({
			xtype : 'container',
			itemId : 'search_list',
			layout : {
				type : 'vbox',
				align : 'stretch'
			},
			items : []
		});
	}

});