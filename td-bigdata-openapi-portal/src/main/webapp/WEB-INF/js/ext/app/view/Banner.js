Ext.define('OpenApi.view.Banner', { 
    extend: 'Ext.container.Container', 
    requires : [],
    
    initComponent: function() { 
        Ext.apply(this, {
        	id : 'banner',
        	layout : {
    			type : 'vbox',
    			align : 'center'
    		},
    		border : 0,
			style : {
				borderColor : 'green',
				borderStyle : 'solid',
				backgroundColor : '#ffffff'
			},
        	height : 75,
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
					xtype : 'container',
					items : [{
						xtype : 'image',
						src : (projRoot + '/img/logo-default.jpg'),
						width : 513,
						height : 42,
						autoEl : 'div',
						margin : '29.5 0 0 0'
					}]
				},{
					flex : 1
				},{
					xtype : 'container',
					layout : {
						type : 'hbox',
						align : 'middle',
						padding : '0 4 0 4'
					},
					items : [{
						xtype : 'image',
						src : (projRoot + '/img/avatar.png'),
						style : 'border-radius: 50%!important;margin:0 8 0 0;cursor:pointer;',
						hidden : true,
						height : 40,
						listeners : {
							afterrender : function() {
								var mscope = this;
								this.mon(this.getEl(), "mouseover", function() {
									mscope.oflag = true;
									if(!Ext.getCmp("umenu")) {
										Ext.create("OpenApi.ui.SysMenu", {
											sourceTri : mscope
										}).showAt(this.getX() - 195 + this.getWidth() * 2, this.getY() + this.getHeight());
									}
								}, this);
								this.getEl().monitorMouseLeave(1000, function() {
									mscope.oflag = false;
									if(!mscope.oflag && !Ext.getCmp("umenu").oflag) {
										Ext.destroy(Ext.getCmp("umenu"));
									}
								}, this);
							}
						}
					},{
						xtype : 'label',
						text : 'Nick',
						hidden : true,
						style : 'font-size:14px;color:#8ea3b6;'
					}]
				}]
        	}],
        	listeners : {
        		resize : function(me, width, height, oldWidth, oldHeight, eOpts) {
        			if(Ext.getBody().getWidth() >= 1200) {
        				var coms = Ext.ComponentQuery.query("container[isContainer='1']");
        				for(i=0;i<coms.length;i++) {
        					coms[i].setWidth(1170);
        				}
        			} else if(Ext.getBody().getWidth() >= 992 && Ext.getBody().getWidth() <= 1200) {
        				var coms = Ext.ComponentQuery.query("container[isContainer='1']");
        				for(i=0;i<coms.length;i++) {
        					coms[i].setWidth(970);
        				}
        			} else if(Ext.getBody().getWidth() <= 991) {
        				var coms = Ext.ComponentQuery.query("container[isContainer='1']");
        				for(i=0;i<coms.length;i++) {
        					coms[i].setWidth(Ext.getBody().getWidth());
        				}
        			}
        		}
        	}
        }); 
        this.callParent(arguments); 
    }

}); 