package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;

public interface ChangePassword extends RemoteService {

	public void changePassword(String username, String oldPassword, String newPassword) throws Exception;
}
