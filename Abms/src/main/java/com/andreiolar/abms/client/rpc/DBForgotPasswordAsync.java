package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBForgotPasswordAsync {

	public void sendMailToServer(String email, AsyncCallback<Boolean> callback);

}
