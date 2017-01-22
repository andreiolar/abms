package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBSubmitComplaint extends RemoteService {

	public Boolean registerComplaint(UserDetails userInfo, String phoneNumber, String complaintTo, String complaint) throws Exception;
}
