Ext.define('OpenApi.view.OAPageFooter', { 
    extend: 'Ext.container.Container', 
    requires : [],
    
    initComponent: function() { 
        Ext.apply(this, {
        	id : 'oapagefooter',
        	layout : {
    			type : 'vbox',
    			align : 'center'
    		},
    		border : 0,
			style : {
				borderColor : 'green',
				borderStyle : 'solid',
				backgroundColor : '#48525e',
				color : '#a2abb7'
			},
        	height : 116,
        	items : [{
        		xtype : 'container',
    			layout : {
    				type : 'vbox',
    				align : 'stretch',
    				padding : '0 15 0 15'
    			},
    			isContainer : '1',
    			flex : 1,
    			border : 0,
    			style : {
					borderColor : 'red',
					borderStyle : 'solid'
				},
				items : [{
					xtype : 'container',
					margin : '30 0 0 0',
					layout : {
						type : 'hbox',
						align : 'stretch',
						height : 16,
						style : {
							color : '#32c5d2',
							fontWeight : '700',
							fontSize : '15px'
						}
					},
					items : [{
						xtype : 'label',
						text : '联系我们',
						cls : 'footer_label',
						flex : 1
					},{
						xtype : 'label',
						text : '诚聘英才',
						cls : 'footer_label',
						flex : 1
					},{
						xtype : 'label',
						text : '企业合作',
						cls : 'footer_label',
						flex : 1
					},{
						xtype : 'label',
						text : '站点导航',
						cls : 'footer_label',
						flex : 1
					}]
				},{
					xtype : 'container',
					margin : '10 0 0 0',
					layout : {
						type : 'hbox',
						align : 'stretch'
					},
					items : [{
						xtype : 'label',
						text : '中国移动研究院',
						cls : 'footer_label',
						flex : 1
					},{
						xtype : 'label',
						text : '中国移动设计院',
						cls : 'footer_label',
						flex : 1
					},{
						xtype : 'label',
						text : '网站地图',
						cls : 'footer_label',
						flex : 1
					},{
						xtype : 'label',
						text : '友情链接',
						cls : 'footer_label',
						flex : 1
					}]
				}]
        	}]
        }); 
        this.callParent(arguments); 
    }

}); 