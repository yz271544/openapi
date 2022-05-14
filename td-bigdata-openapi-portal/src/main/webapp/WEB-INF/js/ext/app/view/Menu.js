Ext.define('OpenApi.view.Menu', { 
    extend: 'Ext.container.Container', 
    requires : [
                'OpenApi.ui.OAMenuItem',
                'OpenApi.ui.PopMenuItem'
	],
    
    initComponent: function() { 
    	
    	menuMe = this;
    	
        Ext.apply(this, {
        	id : 'oamenu',
        	popMenu : Ext.create("OpenApi.view.PopMenu"),
        	layout : {
    			type : 'vbox',
    			align : 'center'
    		},
    		border : 0,
			style : {
				borderColor : 'green',
				borderStyle : 'solid',
				backgroundColor : '#444d58'
			},
        	height : 56,
        	items : [{
        		xtype : 'container',
    			layout : {
    				type : 'hbox',
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
					xtype : 'oamenuitem',
					itemId : 'oamenuhome',
					text : '首 页',
					selected : (curPage == "oamenuhome" ? true : false),
					listeners : {
						click : function() {
							menuMe.oaMenuItemClick(this);
							location.href = (projRoot + "/dashboard/index.htm");
						},
						mouseover : function(me, e, eOpts) {
							if(menuMe.popMenu) {
								menuMe.popMenu.removeAll();
								menuMe.popMenu.hide();
							}
						}
					}
				},{
					xtype : 'oamenuitem',
					itemId : 'oamenudocument',
					text : '文档中心',
					selected : (curPage == "oamenudocument" ? true : false),
					listeners : {
						click : function() {
							menuMe.oaMenuItemClick(this);
							location.href = (projRoot + "/document/document.htm");
						},
						mouseover : function(me, e, eOpts) {
							var popX = me.getX();
							var popY = me.getY() + me.getHeight();
							if(menuMe.popMenu) {
								menuMe.popMenu.removeAll();
								menuMe.popMenu.hide();
							}
							
						}
					}
				},{
					xtype : 'oamenuitem',
					itemId : 'oamenuapi',
					text : 'API',
					selected : (curPage == "oamenuapi" ? true : false),
					listeners : {
						click : function() {
							menuMe.oaMenuItemClick(this);
							location.href = (projRoot + "/apiDisplay/apiDisplay.htm");
						},
						mouseover : function(me, e, eOpts) {
							if(menuMe.popMenu) {
								menuMe.popMenu.removeAll();
								menuMe.popMenu.hide();
							}
						}
					}
				},{
					xtype : 'oamenuitem',
					itemId : 'oamenusupport',
					text : '支持中心',
					selected : (curPage == "oamenusupport" ? true : false),
					listeners : {
						click : function() {
							menuMe.oaMenuItemClick(this);
						},
						mouseover : function(me, e, eOpts) {
							if(menuMe.popMenu) {
								menuMe.popMenu.removeAll();
								menuMe.popMenu.hide();
							}
						}
					}
				},{
					xtype : 'oamenuitem',
					itemId : 'oamenuconsole',
					text : '控制台',
					selected : (curPage == "oamenuconsole" ? true : false),
					listeners : {
						click : function() {
							menuMe.oaMenuItemClick(this);
							location.href = (projRoot + "/main.htm");
						},
						mouseover : function(me, e, eOpts) {
							if(menuMe.popMenu) {
								menuMe.popMenu.removeAll();
								menuMe.popMenu.hide();
							}
						}
					}
				}]
        	}]
        }); 
        this.callParent(arguments); 
    },
	oaMenuItemClick : function(curItem) {
		menuMe.down("container").items.each(function(item, index, length) {
			item.setSelected(false);
		});
		curItem.setSelected(true);
	},
	oaMenuItemOver : function(itemId) {
		
	}

}); 