Ext.define('OpenApi.store.ApiManaStore', {
	extend : 'Ext.data.TreeStore',
	model: 'OpenApi.model.Node',
    proxy: {
        type: 'ajax',
        url: (projRoot + '/documentPublish/getOpenApiDirs.json')
    },
    folderSort: true
});