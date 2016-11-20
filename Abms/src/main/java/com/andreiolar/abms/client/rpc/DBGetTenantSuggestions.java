package com.andreiolar.abms.client.rpc;

import java.util.List;

import com.andreiolar.abms.shared.UserInfo;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBGetTenantSuggestions extends RemoteService {

	public List<String> getTenantSuggestions(UserInfo userInfo) throws Exception;
}
