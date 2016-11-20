package com.andreiolar.abms.client.rpc;

import java.util.List;

import com.andreiolar.abms.shared.ComplaintInfo;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBGetComplaintInfo extends RemoteService {

	public List<ComplaintInfo> getComplaintInfo() throws Exception;
}
