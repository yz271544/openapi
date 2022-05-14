Ext.define('OpenApi.view.MainView', { 
    extend: 'Ext.container.Container', 
    requires : [],
    
    initComponent: function() { 
        Ext.apply(this, {
        	id : 'mainview',
			region: 'center',

        	layout : {
    			type : 'vbox',
    			align : 'center'
    		},
    		bodyCls : 'main_view',
			style : {
				borderWidth : 0,
				borderColor : 'green',
				borderStyle : 'solid'
			},
        	items : [{
        		xtype : 'container',
				autoScroll : true,
    			layout : {
    				type : 'vbox',
    				align : 'stretch',
    				padding : '15 15 15 15'
    			},
    			isContainer : '1',
    			flex : 1,
    			border : 0,
    			style : {
					borderColor : 'red',
					borderStyle : 'solid'
				},
				items : []
        	}]
        }); 
        this.callParent(arguments); 
    }

}); 