package com.andreiolar.abms.client.rpc;

import java.util.List;

import com.andreiolar.abms.shared.UpkeepCostReport;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RetreiveUpkeepCostReportsAsync {

	void retreiveUpkeepCostReports(String date, AsyncCallback<List<UpkeepCostReport>> callback);

}
