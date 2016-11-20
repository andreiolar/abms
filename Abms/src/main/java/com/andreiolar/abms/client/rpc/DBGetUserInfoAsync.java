package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.UserInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBGetUserInfoAsync {

	public void getUserInfo(String username, AsyncCallback<UserInfo> callback);

}
