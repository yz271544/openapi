Ext.define('OpenApi.model.StructArg', {
	extend : 'Ext.data.Model',
	fields : [
		{name : 'fieldName', type : 'string'},
		{name : 'fieldTargtType', type : 'string'},
		{name : 'syncMustDesc', type : 'string'},
		{name : 'asynMustDesc', type : 'string'},
		{name : 'rssMustDesc', type : 'string'},
		{name : 'reqArgId', type : 'string'},
		{name : 'reqArgDefltVal', type : 'string'},
		{name : 'respnArgId', type : 'string'},
		{name : 'respnArgSmpVal', type : 'string'},
		{name : 'fieldFileDesc', type : 'string'}
	]
});