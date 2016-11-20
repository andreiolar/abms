package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;

public interface DBChangeUsername extends RemoteService {

	public Boolean changeUsername(String originalUsername, String newUsername) throws Exception;
}
