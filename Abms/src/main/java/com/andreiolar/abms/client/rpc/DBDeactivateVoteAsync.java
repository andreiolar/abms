package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBDeactivateVoteAsync {

	public void deactivateVote(String voteId, AsyncCallback<Boolean> callback);

}
