package com.andreiolar.abms.client.rpc;

import java.util.List;

import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBGetOtherTenants extends RemoteService {

	public List<UserDetails> getOtherTenants(String username) throws Exception;
}
