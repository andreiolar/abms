package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;

public interface DBDeactivateVote extends RemoteService {

	public Boolean deactivateVote(String voteId) throws Exception;
}
