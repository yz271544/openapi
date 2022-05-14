Ext.define('OpenApi.store.ApiOfSortStore', {
	extend : 'Ext.data.Store',
	model : 'OpenApi.model.Api',
	pageSize : 20,
	proxy : {  
        type : 'ajax',  
        url : (projRoot + '/apiDisplay/getApiBySortId.json'),  
        reader : {
            type : 'json',
            root : 'result',
            totalProperty: 'totalCount'
        }
    },
    autoLoad : false
})