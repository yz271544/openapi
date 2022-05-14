Ext.define('OpenApi.store.PlatformDisStore', {
	extend : 'Ext.data.TreeStore',
    proxy: {
        type: 'ajax',
        url: (projRoot + '/document/queryOpenApiPlatformDocs.json')
    },
    root : {
    	id : 1001,
    	text : '开放平台'
    },
    folderSort: true
});