package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;

public interface DBForgotPassword extends RemoteService {

	public Boolean sendMailToServer(String email) throws Exception;
}
