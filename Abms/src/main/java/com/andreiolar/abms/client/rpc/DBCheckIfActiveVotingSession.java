package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;

public interface DBCheckIfActiveVotingSession extends RemoteService {

	public Boolean ckeckForActiveVotingSession() throws Exception;
}
