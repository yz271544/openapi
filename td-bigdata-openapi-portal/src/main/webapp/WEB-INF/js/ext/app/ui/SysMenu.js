Ext.define('OpenApi.ui.SysMenu',{
	extend : 'Ext.container.Container',
	alternateClassName : ['OpenApi.ui.SysMenu'],
	alias : ['widget.opisysmenu'],
	style : 'cursor:pointer',
	
	initComponent : function(){
		Ext.apply(this, {
			id : 'umenu',
			floating : true,
			shadow : false,
			width : 195,
        	layout : {
        		type : 'vbox',
        		align : 'stretch',
        	},
        	items : [{
        		xtype : 'container',
        		height : 8,
        		layout : {
        			type : 'absolute'
        		},
        		items : [{
        			xtype : 'image',
        			src : (projRoot + '/js/ext/resources/images/sign.png'),
        			x : 168,
        			y : 0
        		}]
        	},{
        		xtype : 'container',
        		height : 34,
        		style : 'background-color:#2e343b;',
        		layout : {
        			type : 'hbox',
        			align : 'stretch'
        		},
        		items : [{
        			xtype : 'container',
        			items : [{
        				xtype : 'image',
	        			src : (projRoot + '/js/ext/resources/images/key.png'),
	        			flex : 1,
	        			margin : '8 16 8 16'
        			}]
        		},{
	        		xtype : 'label',
	        		text : '注销',
	        		cls : 'sys_menu_font',
	        		style : 'color:#aaa38c !important;cursor:pointer;',
	        		flex : 3,
	        		padding : '6 0 0 0'
	        	}]
        	}]
        }); 
		this.addEvents("click");
		this.callParent(arguments);
	},
	onRender : function() {
		var scope = this;
		this.callParent(arguments);
		this.mon(this.el, "click", this.onClick, this);
		this.mon(this.el, "mouseover", this.mouseover, this, this);
		this.mon(this.el, "mouseleave", this.mouseleave, this, this);
		this.el.monitorMouseLeave(1000, function() {
			if(!scope.sourceTri.oflag && !Ext.getCmp("umenu").oflag) {
				Ext.destroy(scope);
			}
		}, this)
	},
	onClick : function() {
//		this.fireEvent("click",this);
		location.href = projRoot + "/logout";
	},
	mouseover : function(e, t, eOpts) {
		eOpts.oflag = true;
	},
	mouseleave : function(e, t, eOpts) {
		eOpts.oflag = false;
	}
});