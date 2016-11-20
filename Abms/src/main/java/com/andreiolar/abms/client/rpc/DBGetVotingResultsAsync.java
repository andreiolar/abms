package com.andreiolar.abms.client.rpc;

import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBGetVotingResultsAsync {

	void getVotingResults(String voteId, boolean all, AsyncCallback<Map<String, Number>> callback);
}
