package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBGetUserVoteAsync {

	void getUserVote(String username, String voteId, AsyncCallback<String> callback);

}
