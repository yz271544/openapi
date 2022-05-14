Ext.define('OpenApi.ui.PopMenuItem', { 
    extend: 'Ext.form.Label', 
    alternateClassName : ['OpenApi.ui.PopMenuItem'],
	alias : ['widget.popmenuitem'],
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