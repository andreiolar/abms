package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.User;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBCheckAvailableUsernameAsync {

	public void checkForUsername(String username, AsyncCallback<User> callback);

}
