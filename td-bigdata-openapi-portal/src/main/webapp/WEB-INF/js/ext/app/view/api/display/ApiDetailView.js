Ext.define('OpenApi.view.api.display.ApiDetailView', { 
    extend: 'OpenApi.ui.BaseView', 
    requires : [],
    
    initComponent: function() { 
        Ext.apply(this, {
        	id : 'apidetailview',
        	title : 'API名称'
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
				xtype : 'label',
				baseCls : 'apilisttem_font',
				text : '公共参数',
				padding : '10 10 10 10'
			},{
				xtype : 'label',
				height : 1,
				style : {
					backgroundColor : '#eef1f5'
				}
			},{
				xtype : 'label',
				baseCls : 'apilisttem_font',
				text : '请求地址',
				padding : '10 10 10 10'
			},{
				xtype : 'grid',
				id : 'addr',
				store : Ext.create("OpenApi.store.AddrStore"),
	            columnLines : true,
	            rowLines : true,
	            bodyBorder : false,
	            frame : true,
	            viewConfig: {
	                stripeRows: true
	            },
	            columns : [
	                {text : '环境代码', dataIndex : 'addrId', sortable : true, hidden : true},
	            	{text : '环境', flex : 1, dataIndex : 'addrName', sortable : true, hidden : false},
	            	{text : 'HTTP请求地址', flex : 2, dataIndex : 'httpAddr', sortable : true, hidden : false},
	            	{text : 'HTTPS请求地址', flex : 2, dataIndex : 'httpsAddr', sortable : true, hidden : false}
	            ]
			},{
				xtype : 'label',
				height : 1,
				style : {
					backgroundColor : '#eef1f5'
				}
			},{
				xtype : 'label',
				baseCls : 'apilisttem_font',
				text : '公共请求参数',
				padding : '10 10 10 10'
			},{
				xtype : 'label',
				height : 1,
				style : {
					backgroundColor : '#eef1f5'
				}
			},{
				xtype : 'grid',
				id : 'pubArgs',
				store : Ext.create("OpenApi.store.PubArgStore"),
	            columnLines : true,
	            rowLines : true,
	            bodyBorder : false,
	            frame : true,
	            viewConfig: {
	                stripeRows: true
	            },
	            columns : [
	                {text : '公共参数编码', dataIndex : 'publicArgId', sortable : true, hidden : true},
	            	{text : '名称', flex : 2, dataIndex : 'publicArgName', sortable : true, hidden : false},
	            	{text : '类型', flex : 2, dataIndex : 'publicArgDataType', sortable : true, hidden : false},
					{text : '是否必选参数', flex : 1, dataIndex : 'codeDesc', sortable : true, hidden : false},
					{text : '描述', flex : 4, dataIndex : 'publicArgDesc', sortable : true, hidden : false}
	            ]
			},{
				xtype : 'label',
				baseCls : 'apilisttem_font',
				text : '请求参数',
				padding : '10 10 10 10'
			},{
				xtype : 'label',
				height : 1,
				style : {
					backgroundColor : '#eef1f5'
				}
			},{
				xtype : 'grid',
				id : 'reqArgs',
				store : Ext.create("OpenApi.store.StructArgStore"),
	            columnLines : true,
	            rowLines : true,
	            bodyBorder : false,
	            frame : true,
	            viewConfig: {
	                stripeRows: true
	            },
	            columns : [
	                {text : '名称', flex : 2, dataIndex : 'fieldName', sortable : true, hidden : false},
	            	{text : '类型', flex : 1, dataIndex : 'fieldTargtType', sortable : true, hidden : false},
					{text : '同步必选参数', flex : 1, dataIndex : 'syncMustDesc', sortable : true, hidden : false},
					{text : '异步必选参数', flex : 1, dataIndex : 'asynMustDesc', sortable : true, hidden : false},
					{text : '订阅必选参数', flex : 1, dataIndex : 'rssMustDesc', sortable : true, hidden : false},
	            	{text : '默认值', flex : 1, dataIndex : 'reqArgDefltVal', sortable : true, hidden : false},
	            	{text : '描述', flex : 4, dataIndex : 'fieldFileDesc', sortable : true, hidden : false}
	            ]
			},{
				xtype : 'label',
				baseCls : 'apilisttem_font',
				text : '响应参数',
				padding : '10 10 10 10'
			},{
				xtype : 'label',
				height : 1,
				style : {
					backgroundColor : '#eef1f5'
				}
			},{
				xtype : 'grid',
				id : 'resArgs',
				store : Ext.create("OpenApi.store.StructArgStore"),
	            columnLines : true,
	            rowLines : true,
	            bodyBorder : false,
	            frame : true,
	            viewConfig: {
	                stripeRows: true
	            },
	            columns : [
	                {text : '名称', flex : 2, dataIndex : 'fieldName', sortable : true, hidden : false},
	            	{text : '类型', flex : 1, dataIndex : 'fieldTargtType', sortable : true, hidden : false},
					{text : '同步必选参数', flex : 1, dataIndex : 'syncMustDesc', sortable : true, hidden : false},
					{text : '异步必选参数', flex : 1, dataIndex : 'asynMustDesc', sortable : true, hidden : false},
					{text : '订阅必选参数', flex : 1, dataIndex : 'rssMustDesc', sortable : true, hidden : false},
	            	{text : '示例值', flex : 1, dataIndex : 'respnArgSmpVal', sortable : true, hidden : false},
	            	{text : '描述', flex : 4, dataIndex : 'fieldFileDesc', sortable : true, hidden : false}
	            ]
			},{
				xtype : 'label',
				baseCls : 'apilisttem_font',
				text : 'API工具',
				padding : '10 10 10 10'
			},{
				xtype : 'label',
				height : 1,
				style : {
					backgroundColor : '#eef1f5'
				}
			},{
    			xtype : 'container',
    			layout : {
    				type : 'vbox',
    				align : 'left'
    			},
    			padding : '10 10 10 10',
    			items : [{
    				xtype : 'button',
	            	text : 'API测试工具',
	            	action : 'apiTest',
	            	baseCls : 'button_base',
	            	overCls : 'button_base_hover'
    			}]
    		}]
		});
	}

});