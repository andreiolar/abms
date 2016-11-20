package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBChangeForgotPasswordAsync {

	void resetPassword(String token, String password, AsyncCallback<Boolean> callback);

}
