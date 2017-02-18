package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBSubmitVoteAsync {

	void submitVoteToDB(String voteId, String option, String title, String description, UserDetails userDetails, AsyncCallback<Void> callback);

}
