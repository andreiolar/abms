package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.User;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBCheckAvailableUsername extends RemoteService {

	public User checkForUsername(String username) throws Exception;

}
