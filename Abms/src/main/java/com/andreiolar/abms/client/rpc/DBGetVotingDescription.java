package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;

public interface DBGetVotingDescription extends RemoteService {

	public String getVotingDescription() throws Exception;
}
