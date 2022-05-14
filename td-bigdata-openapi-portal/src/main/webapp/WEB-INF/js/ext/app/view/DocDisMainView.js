Ext.define('OpenApi.view.DocDisMainView', { 
    extend: 'Ext.container.Container', 
    requires : [],
    
    initComponent: function() { 
        Ext.apply(this, {
        	id : 'docdismainview',
        	layout : {
    			type : 'vbox',
    			align : 'center'
    		},
    		bodyCls : 'main_view',
			style : {
				borderWidth : 1,
				borderColor : 'green',
				borderStyle : 'solid'
			},
			items : [{
        		xtype : 'container',
				autoScroll : true,
    			layout : {
    				type : 'hbox',
    				align : 'top'
    			},
    			padding : '15 15 15 15',
    			isContainer : '1',
    			flex : 1,
    			border : 0,
    			style : {
					borderColor : 'red',
					borderStyle : 'solid'
				},
				items : [
					Ext.create("OpenApi.view.document.display.DocStructView", {
						flex : 2
					}),
					Ext.create("OpenApi.view.document.display.DocContentView", {
						margin : '0 0 0 20',
						flex : 5
					})
				         ]
        	}]
        }); 
        this.callParent(arguments); 
    }

}); 