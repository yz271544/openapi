Ext.define('OpenApi.view.document.display.DocContentView', { 
    extend: 'OpenApi.ui.BaseView', 
    requires : ['OpenApi.ui.SearchResultItem'],
    
    initComponent: function() { 
        Ext.apply(this, {
        	id : 'docontentview',
        	title : ''
        }); 
        this.callParent(arguments); 
    },
    onRender : function() {
		this.callParent(arguments);
		this.getComponent("opBody").add({
			xtype : 'container',
			itemId : 'card_lay',
			layout : {
				type : 'card'
			},
			activeItem : 0,
			items : [{
				xtype : 'container',
				itemId : 'discontent',
				width : 500,
				height : 500,
				autoScroll : true,
				overflowY : 'auto'
			},{
				xtype : 'container',
				itemId : 'search_list',
				layout : {
					type : 'vbox',
					align : 'stretch'
				},
				items : [{
					xtype : 'searchresultitem'
				}]
			}]
		});
	}

});