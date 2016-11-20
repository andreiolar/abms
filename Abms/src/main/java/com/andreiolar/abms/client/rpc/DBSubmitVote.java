package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;

public interface DBSubmitVote extends RemoteService {

	public void submitVoteToDB(String option, String username) throws Exception;
}
