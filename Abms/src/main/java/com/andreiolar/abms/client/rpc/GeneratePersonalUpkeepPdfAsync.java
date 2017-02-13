package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GeneratePersonalUpkeepPdfAsync {

	void generatePdf(UserDetails userDetails, String date, String htmlContent, AsyncCallback<Void> callback);

}
