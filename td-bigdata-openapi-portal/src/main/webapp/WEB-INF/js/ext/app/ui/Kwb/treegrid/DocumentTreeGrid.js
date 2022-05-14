Ext.define('OpenApi.ui.Kwb.treegrid.DocumentTreeGrid', {
    extend: 'Ext.tree.Panel',
    alternateClassName : ['OpenApi.ui.Kwb.treegrid.DocumentTreeGrid'],
	alias : ['widget.doctreegrid'],
    requires: [
        'OpenApi.model.Node'
    ],    
    useArrows: true,
    rootVisible: false,
    multiSelect: true,
    singleExpand: false,
    
    initComponent: function() {
    	var treegrid = this;
    	
    	
    	var rowEditing = Ext.create('Ext.grid.plugin.RowEditing', {  
			pluginId : 're',
			triggerEvent : 'celldblclick',
			saveBtnText : '确认',
			cancelBtnText : '取消',
	        autoCancel: true,
	        listeners : {
	        	canceledit : function(editor, context, eOpts) {},
	        	beforeedit : function( editor, context, eOpts ) {},
	        	edit : function(editor, context, eOpts) {}
	        }
        });
    	
    	
        Ext.apply(this, {
            store : treegrid.store,
            columnLines : true,
            rowLines : true,
            bodyBorder : false,
            frame : true,
            viewConfig: {
                stripeRows: true
            },
            tbar : [{
            	xtype : 'button',
            	text : '保存',
            	action : 'save',
            	baseCls : 'button_base',
            	overCls : 'button_base_hover'
            }],
            plugins : [rowEditing],
            columns: [{
                xtype: 'treecolumn',
                text: '文档&目录名称',
                flex: 2,
                sortable: true,
                dataIndex: 'text',
                editor : {
        			xtype : 'textfield',
        			emptyText : '请输入目录名称...',
        			allowBlank : true,
        			value : '111'
        		}
            },{
                text: '文档目录描述',
                flex: 5,
                dataIndex: 'nodeDesc',
                sortable: true,
                editor : {
        			xtype : 'textfield',
        			emptyText : '请输入目录描述...',
        			allowBlank : true,
        			value : '111'
        		}
            }]
        });
        this.callParent();
    }
});