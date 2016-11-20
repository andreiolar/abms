package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.User;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBConnectionAsync {

	public void authenticateUser(String username, String password, AsyncCallback<User> callback);

}
