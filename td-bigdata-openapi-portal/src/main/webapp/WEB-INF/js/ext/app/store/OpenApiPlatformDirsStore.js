Ext.define('OpenApi.store.OpenApiPlatformDirsStore', {
	extend : 'Ext.data.TreeStore',
	model: 'OpenApi.model.Node',
    proxy: {
        type: 'ajax',
        url: (projRoot + '/documentPublish/getOpenPlatformDirs.json')
    },
    folderSort: true,
    rootVisible : true
});