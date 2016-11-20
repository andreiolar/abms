package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBCheckIfActiveVotingSessionAsync {

	public void ckeckForActiveVotingSession(AsyncCallback<Boolean> callback);

}
