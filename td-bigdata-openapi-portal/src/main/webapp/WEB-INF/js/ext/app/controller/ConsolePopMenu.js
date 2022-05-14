Ext.define('OpenApi.controller.ConsolePopMenu', {
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
		var panel = Ext.create(curView);
		this.getOp().add(panel);
	},
	itemAdd : function(popMenu, component, index, eOpts) {
		var ctrl = this;
		component.addListener("click", function(args) {
			ctrl.getOp().removeAll();
			if(component.action.indexOf("?arg=") != -1) {
				var obj = component.action.substring(component.action.indexOf("=") + 1);
				var panel = Ext.create(obj);
				ctrl.getOp().add(panel);
			} else {
				if(component.action)
					location.href = projRoot + component.action;
			}
		}, this);
	},
	menuItemClick : function(me, e, eOpts) {
		alert(me.getText());
	}
})