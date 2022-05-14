Ext.define('OpenApi.ui.SearchResultItem', { 
    extend: 'Ext.container.Container', 
    alternateClassName : ['OpenApi.ui.SearchResultItem'],
	alias : ['widget.searchresultitem'],
    requires : [],
    baseCls : 'common_click_cursor',
    overCls : 'search_mover',
    
    initComponent: function() { 
    	var me = this;
        Ext.apply(this, {
        	padding : '10 0 15 0',
        	style : {
        		borderBottomWidth : '1px',
        		borderBottomColor : '#eef1f5',
        		borderBottomStyle : 'solid'
        	},
        	layout : {
        		type : 'vbox',
        		align : 'stretch',
        		pack : 'center'
        	},
        	items : [{
        		xtype : 'label',
        		html : me.title,
        		baseCls : 'common_click_cursor',
        		cls : 'search_result_title'
        	},{
        		xtype : 'label',
        		html : me.content,
        		baseCls : 'common_click_cursor',
        		cls : 'search_result_title'
        	}]
        }); 
        this.addEvents("click");
		this.callParent(arguments);
    },
    onRender : function() {
		this.callParent(arguments);
		this.mon(this.el, "click", this.onClick, this, this.action);
	},
	onClick : function(me, t, eOpts) {
		this.fireEvent("click", eOpts);
	}

}); 