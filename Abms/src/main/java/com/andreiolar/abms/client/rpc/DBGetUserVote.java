package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;

public interface DBGetUserVote extends RemoteService {

	public String getUserVote(String username, String voteId) throws Exception;
}
