Ext.define('OpenApi.store.PublishComboStore', {
	extend : 'Ext.data.TreeStore',
    proxy: {
        type: 'ajax',
        url: (projRoot + '/documentPublish/getOpenPlatformDirs.json')
    }
});