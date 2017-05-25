package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.VoteSession;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBActiveVoteSessionAsync {

	void getActiveVoteSession(AsyncCallback<VoteSession> callback);

}
