package com.andreiolar.abms.client.rpc;

import java.util.Set;

import com.andreiolar.abms.shared.VoteSession;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CreateVotingSessionAsync {

	void createVotingSession(VoteSession voteSessions, Set<String> votingOptions, AsyncCallback<Void> callback);

}
