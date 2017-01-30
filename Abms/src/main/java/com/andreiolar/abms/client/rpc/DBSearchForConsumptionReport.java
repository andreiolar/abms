package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.SelfReading;
import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBSearchForConsumptionReport extends RemoteService {

	public SelfReading searchForConsumptionReport(UserDetails userDetails, String date) throws Exception;
}
