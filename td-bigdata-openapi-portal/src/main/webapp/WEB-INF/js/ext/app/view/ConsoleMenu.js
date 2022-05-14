Ext.define('OpenApi.view.ConsoleMenu', { 
    extend: 'Ext.container.Container', 
    requires : [
                'OpenApi.ui.OAMenuItem',
                'OpenApi.ui.PopMenuItem'
	],
    
    initComponent: function() { 
    	
    	menuMe = this;
//    	console.log(Ext.decode(menuJson));
    	var menu = Ext.decode(menuJson);
    	var rootMenus = [];
    	menu.forEach(function(element, index, array) {
    		rootMenus.push({
				xtype : 'oamenuitem',
				itemId : ("pmenu" + element.id),
				text : element.name,
				childs : Ext.clone(element.childrens),
				selected : (element.resCode == "DocumentCenter" ? true : false),
				listeners : {
					click : function() {
						menuMe.oaMenuItemClick(this);
						if(element.id == 20000)
							location.href = (projRoot + element.link);
					},
					mouseover : function(me, e, eOpts) {
						var popX = me.getX();
						var popY = me.getY() + me.getHeight();
						if(menuMe.popMenu) {
							menuMe.popMenu.removeAll();
							menuMe.popMenu.hide();
						}
						if(menuMe.popMenu.isHidden()) {
							var childMenus = [];
							this.childs.forEach(function(celement, cindex, carray) {
								childMenus.push({
									xtype : 'popmenuitem',
									html : celement.name,
									action : celement.link,
									baseCls : 'popmenuitem_font',
									overCls : 'popmenuitem_mouseover',
									padding : '10 12 10 12',
									width : 195,
									height : 38
								});
							});
							menuMe.popMenu.parentCmp = me;
							menuMe.popMenu.add(childMenus);
							menuMe.popMenu.addListener("mouseleave", function() {
								if(menuMe.popMenu) {
									menuMe.popMenu.removeAll();
									menuMe.popMenu.hide();
								}
							}, me);
							menuMe.popMenu.show();
							menuMe.popMenu.setX(popX);
							menuMe.popMenu.setY(popY);
						}
					}
				}	
    		});
    	});
//    	console.log(rootMenus);
    	
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
				items : rootMenus
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