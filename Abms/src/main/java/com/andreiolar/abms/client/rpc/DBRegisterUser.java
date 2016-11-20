package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.UserInfo;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBRegisterUser extends RemoteService {

	public Boolean registerUser(UserInfo userInfo) throws Exception;

}
