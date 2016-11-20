package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;

public interface DBGetActiveVoteID extends RemoteService {

	public String getActiveVoteID() throws Exception;
}
