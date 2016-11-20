package com.andreiolar.abms.client.rpc;

import java.util.List;

import com.andreiolar.abms.shared.UserInfo;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBGetAllSuggestions extends RemoteService {

	public List<String> getAllSuggestions(UserInfo userInfo) throws Exception;
}
