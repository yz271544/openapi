Ext.define('OpenApi.ui.ApiSearchResultItem', { 
    extend: 'Ext.container.Container', 
    alternateClassName : ['OpenApi.ui.ApiSearchResultItem'],
	alias : ['widget.apisearchresultitem'],
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
        		xtype : 'container',
        		layout : {
        			type : 'hbox',
        			align : 'stretch'
        		},
        		items : [{
	        		xtype : 'label',
	        		html : me.apiName,
	        		baseCls : 'common_click_cursor',
	        		cls : 'search_result_title',
	        		flex : 2
	        	},{
	        		xtype : 'label',
	        		html : me.apiDesc,
	        		baseCls : 'common_click_cursor',
	        		cls : 'search_result_title',
	        		flex : 5
	        	}]
        	},{
        		xtype : 'container',
        		layout : {
        			type : 'hbox',
        			align : 'stretch'
        		},
        		margin : '10 0 0 0',
        		items : [{
        			xtype : 'label',
	        		html : '参数列表：',
	        		baseCls : 'common_click_cursor',
	        		cls : 'search_result_title'
        		}]
        	},{
        		xtype : 'container',
        		itemId : 'argsList',
        		layout : {
        			type : 'vbox',
        			align : 'stretch',
        			padding : '0 0 0 20'
        		},
        		items : []
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