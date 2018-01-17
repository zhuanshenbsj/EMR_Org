package com.cloud.emr.pattern.chain;

import com.cloud.emr.pattern.state.Context;

public abstract class Handler {
	protected Handler successor;

	/**
	 * @return the successor
	 */
	public Handler getSuccessor() {
		return successor;
	}

	/**
	 * @param successor the successor to set
	 */
	public void setSuccessor(Handler successor) {
		this.successor = successor;
	}
	
	/**
	 * deal the request
	 */
	public abstract boolean HandleRequest(Context context);
	
}
