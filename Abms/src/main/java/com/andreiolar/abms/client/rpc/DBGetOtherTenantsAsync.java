package com.andreiolar.abms.client.rpc;

import java.util.List;

import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBGetOtherTenantsAsync {

	void getOtherTenants(String username, AsyncCallback<List<UserDetails>> callback);

}
