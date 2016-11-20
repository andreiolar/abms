package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;

public interface DBChangePassword extends RemoteService {

	public Boolean changePassword(String username, String password) throws Exception;
}
