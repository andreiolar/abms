package com.andreiolar.abms.client.rpc;

import java.util.List;

import com.andreiolar.abms.shared.SubmittedComplaint;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBRetreiveSubmittedComplaintsAsync {

	void retreiveSubmittedComplaints(AsyncCallback<List<SubmittedComplaint>> callback);

}
