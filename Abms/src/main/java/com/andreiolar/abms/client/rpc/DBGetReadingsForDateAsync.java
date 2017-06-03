package com.andreiolar.abms.client.rpc;

import java.util.List;

import com.andreiolar.abms.shared.SelfReadingCostWrapper;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBGetReadingsForDateAsync {

	void getReadingsForDate(String date, AsyncCallback<List<SelfReadingCostWrapper>> callback);

}
