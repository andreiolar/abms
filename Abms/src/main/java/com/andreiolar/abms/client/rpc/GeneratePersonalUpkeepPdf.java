package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.client.rpc.RemoteService;

public interface GeneratePersonalUpkeepPdf extends RemoteService {

	public void generatePdf(UserDetails userDetails, String date, String htmlContent) throws Exception;
}
