Ext.define('OpenApi.view.ApiDisMainView', { 
    extend: 'Ext.container.Container', 
    requires : [],
    
    initComponent: function() { 
        Ext.apply(this, {
        	id : 'apidismainview',
			autoScroll : true,
			region: 'center',
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

				layout : {
					type : 'card'
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
						Ext.create("OpenApi.view.api.display.ApiStructView", {
							flex : 2
						}),
						Ext.create("OpenApi.view.api.display.ApiContentView", {
							margin : '0 0 0 20',
							flex : 5
						})
					]
	        	},{
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
						Ext.create("OpenApi.view.api.display.ApiListOfSortView", {
							flex : 2
						}),
						Ext.create("OpenApi.view.api.display.ApiDetailView", {
							margin : '0 0 0 20',
							flex : 5
						})
					]
	        	},{
	        		xtype : 'container',
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
					  Ext.create("OpenApi.view.api.display.ApiSearchResultView",{
						  flex : 1
					  })
		        	]
	        	}]
			}]
        }); 
        this.callParent(arguments); 
    }

}); 