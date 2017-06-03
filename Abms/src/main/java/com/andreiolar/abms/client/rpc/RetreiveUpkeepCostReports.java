package com.andreiolar.abms.client.rpc;

import java.util.List;

import com.andreiolar.abms.shared.UpkeepCostReport;
import com.google.gwt.user.client.rpc.RemoteService;

public interface RetreiveUpkeepCostReports extends RemoteService {

	public List<UpkeepCostReport> retreiveUpkeepCostReports(String date) throws Exception;

}
