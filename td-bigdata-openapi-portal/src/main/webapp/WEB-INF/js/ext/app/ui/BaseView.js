Ext.define('OpenApi.ui.BaseView', { 
    extend: 'Ext.container.Container', 
    requires : [
                'OpenApi.ui.Image'
                ],
    closeable : false,
    btnhidden : true,
    
    initComponent: function() { 
    	var scope = this;
        Ext.apply(this, {
        	layout : {
    			type : 'vbox',
    			align : 'stretch'
    		},
    		style : {
    			backgroundColor : '#ffffff'
    		},
    		padding : '12 20 15 20',
        	items : [{
        		xtype : 'container',
        		minHeight : 48,
        		layout : {
        			type : 'hbox',
        			align : 'middle',
        			pack : 'end'
        		},
        		style : 'border-bottom : 1px solid #eef1f5',
        		margin : '0 0 10 0',
        		items : [{
        			xtype : 'container',
        			layout : {
        				type : 'hbox',
        				align : 'middle',
        				defaultMargins : {
        					top : 0,
        					right : 5,
        					bottom : 0,
        					left : 0
        				}
        			},
        			items : [{
        				xtype : 'image',
        				src : (projRoot + '/js/ext/resources/images/title_icon.png'),
        				width : 15,
        				height :15
        			},{
        				xtype : 'label',
        				panelTitle : true,
        				text : this.title,
        				cls : 'panel_title_font'
        			}]
        		},{
        			xtype : 'container',
        			flex : 1
        		},{
        			xtype : 'button',
	            	text : '返回',
	            	action : 'publish',
	            	baseCls : 'button_base',
	            	overCls : 'button_base_hover',
	            	hideMode : 'display',
	            	hidden : scope.btnhidden
        		},{
        			xtype : 'opimage',
        			src : (projRoot + '/img/remove-icon-small.png'),
        			hidden : !scope.closeable,
        			listeners : {
        				click : function() {
        					scope.hide();
        				}
        			}
        		}]
        	},{
        		xtype : 'container',
        		itemId : 'opBody',
        		layout : {
        			type : 'vbox',
        			align : 'stretch'
        		}
        	}]
        }); 
        this.callParent(arguments); 
    }

}); 