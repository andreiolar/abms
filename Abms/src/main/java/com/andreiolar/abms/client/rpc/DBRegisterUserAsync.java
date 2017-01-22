package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBRegisterUserAsync {

	public void registerUser(UserDetails userDetails, AsyncCallback<Boolean> callback);
}
