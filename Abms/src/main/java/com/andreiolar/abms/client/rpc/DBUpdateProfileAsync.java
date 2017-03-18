package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBUpdateProfileAsync {

	void updateProfile(UserDetails newDetails, String updatedUsername, AsyncCallback<Boolean> callback);

}
