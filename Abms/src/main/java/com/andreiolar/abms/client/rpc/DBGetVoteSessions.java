package com.andreiolar.abms.client.rpc;

import java.util.Map;

import com.andreiolar.abms.shared.FinishedVoteSession;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBGetVoteSessions extends RemoteService {

	public Map<String, FinishedVoteSession> getVoteSessions(boolean active) throws Exception;
}
