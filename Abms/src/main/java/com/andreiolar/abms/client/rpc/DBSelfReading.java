package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.SelfReading;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBSelfReading extends RemoteService {

	public Boolean insertReading(String usernmae, SelfReading reading) throws Exception;
}
