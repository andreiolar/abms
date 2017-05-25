package com.andreiolar.abms.client.rpc;

import java.util.Set;

import com.andreiolar.abms.shared.VoteSession;
import com.google.gwt.user.client.rpc.RemoteService;

public interface CreateVotingSession extends RemoteService {

	public Void createVotingSession(VoteSession voteSessions, Set<String> votingOptions) throws Exception;
}
