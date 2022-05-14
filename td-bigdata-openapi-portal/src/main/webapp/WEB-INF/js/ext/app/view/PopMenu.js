Ext.define('OpenApi.view.PopMenu', { 
    extend: 'Ext.container.Container', 
    requires : [],
    
    initComponent: function() { 
        Ext.apply(this, {
        	id : 'popmenu',
        	floating : true,
        	shadow : false,
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
		this.mon(this.el, "mouseover", this.onMouseOver, this);
		this.mon(this.el, "mouseleave", this.onMouseOut, this);
	},
	onMouseOut : function() {
		this.fireEvent("mouseleave",this);
		this.parentCmp.el.down("div").removeCls("oa_menu_item_select");
	},
	onMouseOver : function() {
		this.parentCmp.el.down("div").addCls("oa_menu_item_select");
	}

}); 