Ext.define('OpenApi.controller.PopMenu', {
	extend : 'Ext.app.Controller',
	views : [],
	refs : [{
		ref : 'oapm',
		selector : '#popmenu'
	},{
		ref : 'op',
		selector : '#mainview > container'
	}],
	init : function() {
		this.control({
			'#popmenu' : {
				add : this.itemAdd
			}
		})
	},
	onLaunch : function() {
	},
	itemAdd : function(popMenu, component, index, eOpts) {
		var ctrl = this;
		component.addListener("click", function(args) {
			ctrl.getOp().removeAll();
			var panel = Ext.create(args);
			ctrl.getOp().add(panel);
		}, this);
	},
	menuItemClick : function(me, e, eOpts) {
		alert(me.getText());
	}
})