package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBInsertEmailAsync {

	public void insertEmail(String email, String aptNumber, AsyncCallback<Boolean> callback);

}
