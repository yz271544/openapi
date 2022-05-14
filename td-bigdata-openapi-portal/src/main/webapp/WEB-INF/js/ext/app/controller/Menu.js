Ext.define('OpenApi.controller.Menu', {
	extend : 'Ext.app.Controller',
	views : [],
	refs : [{
		ref : 'oam',
		selector : '#oamenu'
	},{
		ref : 'oami',
		selector : 'oamenuitem'
	}],
	init : function() {
		this.control({
			'oamenuitem' : {
				click : this.menuItemClick
			}
		})
	},
	onLaunch : function() {
	},
	menuItemClick : function(me, e, eOpts) {
//		alert(me.getItemId());
	}
})