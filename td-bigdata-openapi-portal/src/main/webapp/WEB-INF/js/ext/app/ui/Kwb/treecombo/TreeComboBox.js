Ext.define('OpenApi.ui.Kwb.treecombo.TreeComboBox', {
	extend : 'Ext.form.field.ComboBox',
	alternateClassName : [ 'OpenApi.ui.Kwb.treecombo.TreeComboBox' ],
	alias : [ 'widget.treecombo' ],

	border : 1,
	style : {
		borderStyle : 'solid',
		borderColor : '#c2cad8'
	},
	store : new Ext.data.ArrayStore({
		fields : [],
		data : [[]]
	}),
	editable : false,
	allowBlank : false,
	listConfig : {
		resizable : false,
		minWidth : 250,
		maxWidth : 450
	},
	_idValue : null,
	_txtValue : null,
	callback : Ext.emptyFn,
	initComponent : function() {
		var store = Ext.create("OpenApi.store.PublishComboStore");
		this.treeObj = new Ext.tree.Panel({
			border : false,
			height : 250,
			autoScroll : true,
			rootVisible : false,
			store : store
		});
		this.treeRenderId = Ext.id();
		this.tpl = "<tpl><div id='" + this.treeRenderId + "'></div></tpl>";
		this.callParent(arguments);
		this.on({
			'expand' : function() {
				if (!this.treeObj.rendered && this.treeObj && !this.readOnly) {
					Ext.defer(function() {
						this.treeObj.render(this.treeRenderId);
					}, 300, this);
				}
			}
		});
		this.treeObj.on('itemclick', function(view, record) {
			if (record) {
				this.setValue(this._txtValue = record.get('text'));
				this._idValue = record.get('id');
				this.callback.call(this, record.get('id'), record.get('text'));
				this.collapse();
			}
		}, this);
	},
	getValue : function() {
		return this._idValue;
	},
	getTextValue : function() {
		return this._txtValue;
	},
	setLocalValue : function(txt, id) {
		this._idValue = id;
		this.setValue(this._txtValue = txt);
	}

});