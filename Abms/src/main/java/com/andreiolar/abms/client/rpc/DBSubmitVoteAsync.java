package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBSubmitVoteAsync {

	void submitVoteToDB(String option, String username, AsyncCallback<Void> callback);

}
