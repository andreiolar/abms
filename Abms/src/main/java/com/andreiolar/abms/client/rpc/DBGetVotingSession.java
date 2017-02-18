package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.UserDetails;
import com.andreiolar.abms.shared.Vote;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBGetVotingSession extends RemoteService {

	public Vote getVotingSession(UserDetails userDetails) throws Exception;
}
