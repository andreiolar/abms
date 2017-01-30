package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.SelfReading;
import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBSearchForConsumptionReportAsync {

	void searchForConsumptionReport(UserDetails userDetails, String date, AsyncCallback<SelfReading> callback);
}
