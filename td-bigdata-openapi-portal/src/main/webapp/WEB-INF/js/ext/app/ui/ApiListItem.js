Ext.define('OpenApi.ui.ApiListItem', { 
    extend: 'Ext.container.Container', 
    alternateClassName : ['OpenApi.ui.ApiListItem'],
	alias : ['widget.apilistitem'],
    requires : [],
    
    initComponent: function() { 
        Ext.apply(this, {
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