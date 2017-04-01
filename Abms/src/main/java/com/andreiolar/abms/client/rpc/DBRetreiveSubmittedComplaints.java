package com.andreiolar.abms.client.rpc;

import java.util.List;

import com.andreiolar.abms.shared.SubmittedComplaint;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBRetreiveSubmittedComplaints extends RemoteService {

	public List<SubmittedComplaint> retreiveSubmittedComplaints() throws Exception;
}
