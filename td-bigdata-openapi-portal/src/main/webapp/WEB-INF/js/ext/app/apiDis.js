Ext.Ajax.timeout = 120000;
Ext.Loader.setConfig({
	enabled : true
});
Ext.application({
	name : 'OpenApi',
	autoCreateViewport : false,
	appFolder : (projRoot + '/js/ext/app'),
	controllers : [
	               'Menu',
//	               'PopMenu',
	               'OpenApi.controller.api.display.ApiDisplay'
	],
	launch : function() {
		Ext.create("OpenApi.view.ApiDisViewport");
	}
});