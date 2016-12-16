package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.client.exception.InvalidCredentialsException;
import com.andreiolar.abms.shared.UserInfo;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBConnection extends RemoteService {

	public UserInfo authenticateUser(String username, String password) throws InvalidCredentialsException;
}
