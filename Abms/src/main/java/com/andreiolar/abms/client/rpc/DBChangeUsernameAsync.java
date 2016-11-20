package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBChangeUsernameAsync {

	public void changeUsername(String originalUsername, String newUsern, AsyncCallback<Boolean> callback);

}
