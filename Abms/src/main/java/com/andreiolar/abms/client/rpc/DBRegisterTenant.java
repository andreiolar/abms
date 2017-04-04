package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;

public interface DBRegisterTenant extends RemoteService {

	public void registerTenant(String email, String aptNumber) throws Exception;
}
