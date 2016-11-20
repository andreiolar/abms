package com.andreiolar.abms.client.rpc;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBGetVotingOptionsAsync {

	void getVotingOptions(String username, AsyncCallback<List<String>> callback);

}
