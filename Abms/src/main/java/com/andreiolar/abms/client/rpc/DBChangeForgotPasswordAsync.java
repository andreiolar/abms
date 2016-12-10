package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBChangeForgotPasswordAsync {

	void resetPassword(String email, String password, AsyncCallback<Boolean> callback);

}
