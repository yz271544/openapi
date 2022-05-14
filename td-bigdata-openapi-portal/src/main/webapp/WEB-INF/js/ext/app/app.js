Ext.Ajax.timeout = 120000;
Ext.Loader.setConfig({
	enabled : true
});
Ext.application({
	name : 'OpenApi',
	autoCreateViewport : false,
	appFolder : (projRoot + '/js/ext/app'),
	controllers : [
	               'OpenApi.controller.ConsolePopMenu',
	               'document.manage.OpenApiPlatformManaView',
	               'OpenApi.controller.document.manage.ApiManaView',
	               'OpenApi.controller.document.publish.DocumentPublishView'
	],
	launch : function() {
		Ext.create("OpenApi.view.Viewport");
	}
});