function StringBuffer() { 
	this._strs = new Array; 
};
StringBuffer.prototype.append = function (str) { 
	this._strs.push(str); 
}; 
StringBuffer.prototype.toStr = function() { 
	return this._strs.join(""); 
}; 