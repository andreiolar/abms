package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBChangePasswordAsync {

	public void changePassword(String username, String password, AsyncCallback<Boolean> callback);

}
