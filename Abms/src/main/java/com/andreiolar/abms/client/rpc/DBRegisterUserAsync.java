package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.UserInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBRegisterUserAsync {

	public void registerUser(UserInfo userInfo, AsyncCallback<Boolean> callback);

}
