package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBGetUserDetails extends RemoteService {

	public UserDetails geUserDetails(String username) throws Exception;
}
