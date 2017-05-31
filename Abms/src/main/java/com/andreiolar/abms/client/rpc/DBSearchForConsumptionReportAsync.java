package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.ConsumptionCostReport;
import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBSearchForConsumptionReportAsync {

	void searchForConsumptionReport(UserDetails userDetails, String date, AsyncCallback<ConsumptionCostReport> callback);
}
