package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.UserInfo;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBGetUserInfo extends RemoteService {

	UserInfo getUserInfo(String username) throws Exception;
}
