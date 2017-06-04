package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.client.rpc.RemoteService;

public interface UpkeepPayment extends RemoteService {

	public void pay(String token, String amount, String description, String month, UserDetails userDetails) throws Exception;

}
