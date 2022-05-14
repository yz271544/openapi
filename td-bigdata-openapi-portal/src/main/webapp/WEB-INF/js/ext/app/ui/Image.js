Ext.define('OpenApi.ui.Image',{
	extend : 'Ext.Img',
	alternateClassName : ['OpenApi.ui.Image'],
	alias : ['widget.opimage'],
	style : 'cursor:pointer',
	
	initComponent : function(){
		this.addEvents("click");
		this.callParent(arguments);
	},
	onRender : function() {
		this.callParent(arguments);
		this.mon(this.el, "click", this.onClick, this);
	},
	onClick : function() {
		this.fireEvent("click",this);
	}
});