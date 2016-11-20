package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBSubmitComplaintAsync {

	void addComplaint(String username, String phoneNumber, String complaintTo, String firstName, String lastName, String personalId, String idSeries,
			String message, AsyncCallback<Boolean> callback);

}
