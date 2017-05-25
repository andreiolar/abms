package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.VoteSession;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBActiveVoteSession extends RemoteService {

	public VoteSession getActiveVoteSession() throws Exception;
}
