package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBConnectionAsync {

	void authenticateUser(String username, String password, AsyncCallback<UserDetails> callback);
}
