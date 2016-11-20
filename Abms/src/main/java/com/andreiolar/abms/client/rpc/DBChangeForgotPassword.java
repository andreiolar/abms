package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;

public interface DBChangeForgotPassword extends RemoteService {

	public Boolean resetPassword(String token, String password) throws Exception;
}
