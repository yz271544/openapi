Ext.define('OpenApi.store.ApiSearchStore', {
	extend : 'Ext.data.Store',
	model : 'OpenApi.model.Api',
	proxy : {  
        type : 'ajax',  
        url : (projRoot + '/apiDisplay/searchApisByText.json'),  
        reader : {
            type : 'json',
            root : 'result'
        }
    },
    autoLoad : true
})