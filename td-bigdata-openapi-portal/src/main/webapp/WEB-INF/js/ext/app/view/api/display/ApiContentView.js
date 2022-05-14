Ext.define('OpenApi.view.api.display.ApiContentView', { 
    extend: 'OpenApi.ui.BaseView', 
    requires : [],
    
    initComponent: function() { 
        Ext.apply(this, {
        	id : 'apicontentview',
        	title : '待修改'
        }); 
        this.callParent(arguments); 
    },
    onRender : function() {
		this.callParent(arguments);
		var store = Ext.create("OpenApi.store.ApiOfSortStore");
		this.getComponent("opBody").add({
			xtype : 'container',
			itemId : 'discontent',
			width : 500,
			height : 500,
			autoScroll : true,
			overflowY : 'auto',
			layout : {
				type : 'vbox',
				align : 'stretch'
			},
			items : [{
				xtype : 'grid',
				store : store,
	            columnLines : true,
	            rowLines : true,
	            bodyBorder : false,
	            frame : true,
	            viewConfig: {
	                stripeRows: true
	            },
	            columns : [
	                {text : 'API编号', dataIndex : 'apiId', sortable : true, hidden : true},
	            	{text : '分类编号', dataIndex : 'apiSort', sortable : true, hidden : true},
	            	{text : 'API列表', flex : 3, dataIndex : 'apiName', sortable : true, hidden : false},
	            	{text : '描述', flex : 3, dataIndex : 'apiDesc', sortable : true, hidden : false},
	            	{text : '版本号', flex : 1, dataIndex : 'apiVersion', sortable : true, hidden : false}
	            ],
	            bbar: Ext.create('Ext.PagingToolbar', {
	                store: store,
	                displayInfo: true,
	                displayMsg: '当前 {0} - {1} 共{2}条记录',
	                emptyMsg: "无数据",
	            })
			}]
		});
	}

});