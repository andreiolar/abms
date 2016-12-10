package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.client.exception.InvalidCodeException;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBCodeChecker extends RemoteService {

	public void checkCode(String code, String email) throws InvalidCodeException;

}
