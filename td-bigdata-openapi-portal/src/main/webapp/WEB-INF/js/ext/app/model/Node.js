Ext.define('OpenApi.model.Node', {
    extend: 'Ext.data.Model',
    fields: [
             {name: 'id', type: 'int'},
             {name: 'tempId', type: 'int'},
             {name: 'parentId', type: 'int'}, 
             {name: 'leaf', type: 'boolean'}, 
             {name: 'index', type: 'int'}, 
             {name: 'text', type: 'string'}, 
             {name: 'nodeDesc', type: 'string'}
    ]
}); 