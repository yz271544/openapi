Ext.define('OpenApi.view.OAVersionInfo', { 
    extend: 'Ext.container.Container', 
    requires : [],
    
    initComponent: function() { 
        Ext.apply(this, {
        	id : 'oaversioninfo',
        	layout : {
    			type : 'vbox',
    			align : 'center'
    		},
    		border : 0,
			style : {
				borderColor : 'green',
				borderStyle : 'solid',
				backgroundColor : '#3b434c',
				color : '#a2abb7'
			},
        	height : 52,
        	items : [{
        		xtype : 'container',
				margin : '20 0 0 0',
    			layout : {
    				type : 'hbox',
    				align : 'stretch',
    				padding : '0 15 0 15'
    			},
    			isContainer : '1',
    			flex : 1,
    			border : 0,
				items : [{
					xtype : 'label',
					html : '2016 © OPENAPI by TERADATA. <span style="color:#ffffff;">TERADATA</span>'
				}]
        	}]
        }); 
        this.callParent(arguments); 
    }

}); 