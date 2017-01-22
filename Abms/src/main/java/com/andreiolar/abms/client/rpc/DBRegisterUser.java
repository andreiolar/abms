package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.client.exception.UsernameUnavailableException;
import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBRegisterUser extends RemoteService {

	public Boolean registerUser(UserDetails userDetails) throws UsernameUnavailableException;
}
