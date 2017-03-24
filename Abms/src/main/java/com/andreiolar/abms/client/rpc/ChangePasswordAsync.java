package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ChangePasswordAsync {

	void changePassword(String username, String oldPassword, String newPassword, AsyncCallback<Void> callback);

}
