Ext.define('OpenApi.store.PubInfoStore', {
	extend : 'Ext.data.Store',
	proxy : {  
        type : 'ajax',  
        url : (projRoot + '/apiDisplay/getPubInfo.json'),  
    },
    autoLoad : true
})