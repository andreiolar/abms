package com.andreiolar.abms.client.rpc;

import java.util.Map;

import com.andreiolar.abms.shared.FinishedVoteSession;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBGetVoteSessionsAsync {

	void getVoteSessions(boolean active, AsyncCallback<Map<String, FinishedVoteSession>> callback);
}
