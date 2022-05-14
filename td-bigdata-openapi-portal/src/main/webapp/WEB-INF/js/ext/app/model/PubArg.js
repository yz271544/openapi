Ext.define('OpenApi.model.PubArg', {
	extend : 'Ext.data.Model',
	fields : [
		{name : 'publicArgId', type : 'int'},
		{name : 'publicArgName', type : 'string'},
		{name : 'publicArgDataType', type : 'string'},
		{name : 'codeDesc', type : 'string'},
		{name : 'publicArgDesc', type : 'string'}
	]
});