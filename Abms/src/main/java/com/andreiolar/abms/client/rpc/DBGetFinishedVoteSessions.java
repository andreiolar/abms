package com.andreiolar.abms.client.rpc;

import java.util.Map;

import com.andreiolar.abms.shared.FinishedVoteSession;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBGetFinishedVoteSessions extends RemoteService {

	public Map<String, FinishedVoteSession> getFinishedVoteSessions() throws Exception;
}
