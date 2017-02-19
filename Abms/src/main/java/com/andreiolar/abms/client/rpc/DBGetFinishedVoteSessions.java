package com.andreiolar.abms.client.rpc;

import java.util.List;

import com.andreiolar.abms.shared.FinishedVoteSession;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBGetFinishedVoteSessions extends RemoteService {

	public List<FinishedVoteSession> getFinishedVoteSessions() throws Exception;
}
