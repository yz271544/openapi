Ext.define('OpenApi.model.Api', {
	extend : 'Ext.data.Model',
	fields : [
		{name : 'apiId', type : 'int'},
		{name : 'apiSort', type : 'int'},
		{name : 'apiVersion', type : 'int'},
		{name : 'apiName', type : 'string'},
		{name : 'apiDesc', type : 'string'}
	]
});
