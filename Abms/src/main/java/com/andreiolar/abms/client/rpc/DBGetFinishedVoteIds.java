package com.andreiolar.abms.client.rpc;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

public interface DBGetFinishedVoteIds extends RemoteService {

	public List<String> getFinishedVoteIds(boolean all) throws Exception;
}
