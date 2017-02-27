package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBGetUserDetailsAsync {

	void geUserDetails(String username, AsyncCallback<UserDetails> callback);

}
