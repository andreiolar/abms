package com.andreiolar.abms.client.rpc;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBGetFinishedVoteIdsAsync {

	public void getFinishedVoteIds(boolean all, AsyncCallback<List<String>> callback);

}
