package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBCheckIfVoteIDAvailableAsync {

	public void checkVoteId(String voteId, AsyncCallback<Boolean> callback);

}
