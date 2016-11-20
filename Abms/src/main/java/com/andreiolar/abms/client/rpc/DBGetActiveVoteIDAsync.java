package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBGetActiveVoteIDAsync {

	public void getActiveVoteID(AsyncCallback<String> callback);
}
