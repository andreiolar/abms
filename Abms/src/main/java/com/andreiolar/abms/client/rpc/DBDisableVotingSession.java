package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;

public interface DBDisableVotingSession extends RemoteService {

	public Void disableActiveVotingSession() throws Exception;
}
