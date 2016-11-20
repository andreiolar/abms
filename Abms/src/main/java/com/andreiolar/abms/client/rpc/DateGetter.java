package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;

public interface DateGetter extends RemoteService {

	public String getCurrentMonth() throws Exception;

	public String getPreviousMonth() throws Exception;

	public String getCurrentYear() throws Exception;

	public Integer getPreviousMonthInt() throws Exception;

}
