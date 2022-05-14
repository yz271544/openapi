Ext.define('OpenApi.ui.Kwb.textfield.ClearTextField', {
	extend : 'Ext.form.TriggerField',
	alternateClassName : [ 'OpenApi.ui.Kwb.textfield.ClearTextField' ],
	alias : [ 'widget.cleartextfield' ],
	componentCls : 'x-search-trigger-component',
	triggerBaseCls : Ext.baseCSSPrefix + 'search-trigger',
	focusCls : 'trigger-focus',
	
	initComponent : function() {
		var ctf = this;
		this.addEvents("click");
		Ext.apply(this, {
			hideTrigger : true,
			listeners : {
				change : function(me, newValue, oldValue, eOpts) {
					if(newValue == "") {
						me.triggerWrap.down(".x-trigger-cell").hide();
					} else {
						me.triggerWrap.down(".x-trigger-cell").show();
					}
				},
				afterrender : function(me, eOpts) {
					me.triggerEl.first().addListener("click", function() {
						me.setValue("");
					});
				}
			}
        });
		this.callParent(arguments);
	}

});