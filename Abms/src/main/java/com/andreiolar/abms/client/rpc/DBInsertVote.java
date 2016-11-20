package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.Vote;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBInsertVote extends RemoteService {

	public Boolean insertVote(Vote vote, String voteDescription) throws Exception;
}
