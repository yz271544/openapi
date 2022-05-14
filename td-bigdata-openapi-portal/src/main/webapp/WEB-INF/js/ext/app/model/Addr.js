Ext.define('OpenApi.model.Addr', {
	extend : 'Ext.data.Model',
	fields : [
		{name : 'addrId', type : 'int'},
		{name : 'addrName', type : 'string'},
		{name : 'httpAddr', type : 'string'},
		{name : 'httpsAddr', type : 'string'}
	]
});