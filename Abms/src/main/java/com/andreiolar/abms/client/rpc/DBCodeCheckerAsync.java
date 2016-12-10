package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBCodeCheckerAsync {

	void checkCode(String code, String email, AsyncCallback<Void> callback);

}
