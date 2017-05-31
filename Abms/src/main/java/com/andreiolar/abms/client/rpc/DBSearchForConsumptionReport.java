package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.ConsumptionCostReport;
import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBSearchForConsumptionReport extends RemoteService {

	public ConsumptionCostReport searchForConsumptionReport(UserDetails userDetails, String date) throws Exception;
}
