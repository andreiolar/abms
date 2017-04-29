package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;

public interface DBCheckUpkeepReportExists extends RemoteService {

	public void checkUpkeepReport(String previousMonthAndYear) throws Exception;
}
