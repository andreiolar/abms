package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.Vote;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBGetVotingSessionAsync {

	void getVotingSession(AsyncCallback<Vote> callback);
}
