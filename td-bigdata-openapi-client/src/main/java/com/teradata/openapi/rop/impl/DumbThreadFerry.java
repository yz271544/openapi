package com.teradata.openapi.rop.impl;

import com.teradata.openapi.rop.ThreadFerry;

/**
 * 不进行任何操作的实现类,仅为方便逻辑的运行
 * 
 * @author : chenxh
 * @date: 14-2-12
 */
public class DumbThreadFerry implements ThreadFerry {

	@Override
	public void doInDestThread() {
	}

	@Override
	public void doInSrcThread() {
	}
}
