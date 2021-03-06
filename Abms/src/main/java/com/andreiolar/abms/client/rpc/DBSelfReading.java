package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.SelfReading;
import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBSelfReading extends RemoteService {

	boolean insertReading(UserDetails userDetails, SelfReading reading) throws Exception;
}
