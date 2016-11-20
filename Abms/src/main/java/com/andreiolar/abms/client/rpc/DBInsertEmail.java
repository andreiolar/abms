package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;

public interface DBInsertEmail extends RemoteService {

	public Boolean insertEmail(String email, String aptNumber) throws Exception;
}
