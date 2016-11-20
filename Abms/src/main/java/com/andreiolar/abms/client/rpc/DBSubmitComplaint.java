package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;

public interface DBSubmitComplaint extends RemoteService {

	public Boolean addComplaint(String username, String phoneNumber, String complaintTo, String firstName, String lastName, String personalId,
			String idSeries, String message) throws Exception;
}
