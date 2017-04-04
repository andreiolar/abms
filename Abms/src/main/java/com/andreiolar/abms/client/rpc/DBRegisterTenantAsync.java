package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBRegisterTenantAsync {

	void registerTenant(String email, String aptNumber, AsyncCallback<Void> callback);

}
