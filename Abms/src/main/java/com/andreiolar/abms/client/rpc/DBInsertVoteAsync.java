package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.Vote;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBInsertVoteAsync {

	public void insertVote(Vote vote, String voteDescription, AsyncCallback<Boolean> callback);

}
