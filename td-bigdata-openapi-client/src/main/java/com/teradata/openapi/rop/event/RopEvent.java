package com.teradata.openapi.rop.event;

import java.util.EventObject;

import com.teradata.openapi.rop.RopContext;

public abstract class RopEvent extends EventObject {

	private RopContext ropContext;

	public RopEvent(Object source, RopContext ropContext) {
		super(source);
		this.ropContext = ropContext;
	}

	public RopContext getRopContext() {
		return ropContext;
	}
}
