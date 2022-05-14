Ext.define('OpenApi.view.api.display.ApiListOfSortView', { 
    extend: 'OpenApi.ui.BaseView', 
    requires : [
                'OpenApi.ui.ApiListItem'
                ],
    
    initComponent: function() { 
        Ext.apply(this, {
        	id : 'apilistofsortview',
        	title : '分类名称',
        	btnhidden : false
        }); 
        this.callParent(arguments); 
    },
    onRender : function() {
		this.callParent(arguments);
		this.getComponent("opBody").add({
			xtype : 'container',
			layout : {
				type : 'vbox',
				align : 'stretch'
			},
			items : [{
				xtype : 'apilistitem',
				text : '列表',
				baseCls : 'popmenuitem_font',
				overCls : 'popmenuitem_mouseover',
			}]
		});
	}

}); 
