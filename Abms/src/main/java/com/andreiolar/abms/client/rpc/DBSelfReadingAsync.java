package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.SelfReading;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBSelfReadingAsync {

	public void insertReading(String usernmae, SelfReading reading, AsyncCallback<Boolean> callback);

}
