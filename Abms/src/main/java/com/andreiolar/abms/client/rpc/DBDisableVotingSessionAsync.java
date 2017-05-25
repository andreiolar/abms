package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBDisableVotingSessionAsync {

	void disableActiveVotingSession(AsyncCallback<Void> callback);

}
