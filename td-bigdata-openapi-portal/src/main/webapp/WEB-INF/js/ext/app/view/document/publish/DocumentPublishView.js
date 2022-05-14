Ext.define('OpenApi.view.document.publish.DocumentPublishView', { 
    extend: 'OpenApi.ui.BaseView', 
    requires : ['OpenApi.ui.Kwb.treecombo.TreeComboBox'],
    
    initComponent: function() { 
        Ext.apply(this, {
        	id : 'documentpublishview',
        	title : '文档发布'
        }); 
        this.callParent(arguments); 
    },
    onRender : function() {
		this.callParent(arguments);
		this.getComponent("opBody").add({
			xtype : 'container',
			itemId : 'dpv',
			layout : {
				type : 'vbox',
				align : 'stretch'
			},
			items : [{
				xtype : 'container',
				layout : {
					type : 'vbox',
					align : 'stretch'
				},
				padding : '20 20 20 20',
				items : [{
					xtype : 'container',
					layout : {
						type : 'hbox',
						align : 'stretch'
					},
					margin : '0 0 15 0',
					items : [{
						xtype : 'label',
						baseCls : 'common_font',
						text : '标题 ： ',
						width : 100
					},{
						xtype : 'textfield',
						fieldCls : 'textfield',
						height : 34,
						flex : 1,
						name : 'docTitle',
						allowBlank : false
					}]
				},{
					xtype : 'container',
					layout : {
						type : 'hbox',
						align : 'stretch'
					},
					margin : '0 0 15 0',
					items : [{
						xtype : 'label',
						baseCls : 'common_font',
						text : '发布到 ： ',
						width : 100
					},{
						xtype : 'treecombo',
						width : 200,
						name : 'toNode',
						allowBlank : false
					}]
				},{
					xtype : 'container',
					layout : {
						type : 'hbox',
						align : 'stretch'
					},
					border : 0,
					style : {
						borderStyle : 'solid',
						borderColor : 'red'
					},
					items : [{
						xtype : 'label',
						baseCls : 'common_font',
						text : '正文 ： ',
						width : 100
					},{
						xtype : 'htmleditor',
						height : 600,
						flex : 1,
						allowBlank : false,
						margin : '0 0 15 0',
						name : 'content',
						plugins : [
						           Ext.create("OpenApi.ui.Kwb.plugins.HtmlEditorImgCtrl"),
						           Ext.create("OpenApi.ui.Kwb.plugins.HtmlEditorPreview")
						           ]
					}]
				},{
					xtype : 'container',
					layout : {
						type : 'hbox',
						align : 'right'
					},
					
					items : [{
						flex : 1
					},{
		            	xtype : 'button',
		            	text : '发布',
		            	action : 'publish',
		            	baseCls : 'button_base',
		            	overCls : 'button_base_hover'
		            }]
				}]
			}]
		});
	}

}); 