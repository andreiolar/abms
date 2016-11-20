package com.andreiolar.abms.client.rpc;

import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;

public interface DBGetVotingResults extends RemoteService {

	public Map<String, Number> getVotingResults(String voteId, boolean all) throws Exception;
}
