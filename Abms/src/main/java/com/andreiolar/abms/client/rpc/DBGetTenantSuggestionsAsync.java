package com.andreiolar.abms.client.rpc;

import java.util.List;

import com.andreiolar.abms.shared.UserInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBGetTenantSuggestionsAsync {

	public void getTenantSuggestions(UserInfo userInfo, AsyncCallback<List<String>> callback);

}
