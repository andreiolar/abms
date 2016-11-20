package com.andreiolar.abms.client.rpc;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

public interface DBGetVotingOptions extends RemoteService {

	public List<String> getVotingOptions(String username) throws Exception;
}
