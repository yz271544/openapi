Ext.define('OpenApi.ui.OAMenuItem',{
	extend : 'Ext.Component',
	alternateClassName : ['OpenApi.ui.OAMenuItem'],
	alias : ['widget.oamenuitem'],
	overCls : 'oa_menu_item_select',
	curSelCls : 'oa_menu_item_select',
	border : 0,
	style : {
		borderColor : 'green',
		borderStyle : 'solid'
	},
	tpl : ['<div class="oa_menu_item ',
	       		'<tpl if="selected && selected == true"> {curSelCls}</tpl>" unselectable="on">',
	       		'{text}',
	       	 '</div>'],
	initComponent : function(){
		this.data = {
			text : this.text,
			curSelCls : this.curSelCls,
			selected : this.selected
		};
		this.addEvents("click");
		this.callParent(arguments);
	},
	onRender : function() {
		this.callParent(arguments);
		this.mon(this.el, "click", this.onClick, this);
		this.mon(this.el, "mouseover", this.onMouseOver, this);
	},
	setSelected : function(selected) {
		if(selected) {
			this.el.down("div").addCls(this.curSelCls);
		} else {
			this.el.down("div").removeCls(this.curSelCls);
		}
		
	},
	onClick : function() {
		this.fireEvent("click",this);
	},
	onMouseOver : function() {
		this.fireEvent("mouseover",this);
	}
});