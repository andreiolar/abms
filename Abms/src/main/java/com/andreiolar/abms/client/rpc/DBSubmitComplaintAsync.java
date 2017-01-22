package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBSubmitComplaintAsync {

	void registerComplaint(UserDetails userInfo, String phoneNumber, String complaintTo, String complaint, AsyncCallback<Boolean> callback);

}
