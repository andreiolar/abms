package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DateGetterAsync {

	public void getCurrentMonth(AsyncCallback<String> callback);

	public void getPreviousMonth(AsyncCallback<String> callback);

	public void getCurrentYear(AsyncCallback<String> callback);

	public void getPreviousMonthInt(AsyncCallback<Integer> callback);

}
