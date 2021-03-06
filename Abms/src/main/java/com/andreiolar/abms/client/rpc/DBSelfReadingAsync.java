package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.SelfReading;
import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBSelfReadingAsync {

	public void insertReading(UserDetails userDetails, SelfReading reading, AsyncCallback<Boolean> callback);

}
