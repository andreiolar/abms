package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBCheckUpkeepReportExistsAsync {

	void checkUpkeepReport(String previousMonthAndYear, AsyncCallback<Void> callback);

}
