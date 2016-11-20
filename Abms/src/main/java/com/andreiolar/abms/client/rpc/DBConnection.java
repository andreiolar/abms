package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.User;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBConnection extends RemoteService {

	public User authenticateUser(String username, String password) throws Exception;
}
