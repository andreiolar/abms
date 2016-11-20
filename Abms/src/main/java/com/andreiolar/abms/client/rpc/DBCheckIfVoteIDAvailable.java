package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;

public interface DBCheckIfVoteIDAvailable extends RemoteService {

	public Boolean checkVoteId(String voteId) throws Exception;
}
