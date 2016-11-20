package com.andreiolar.abms.client.rpc;

import java.util.List;

import com.andreiolar.abms.shared.ComplaintInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBGetComplaintInfoAsync {

	public void getComplaintInfo(AsyncCallback<List<ComplaintInfo>> callback);

}
