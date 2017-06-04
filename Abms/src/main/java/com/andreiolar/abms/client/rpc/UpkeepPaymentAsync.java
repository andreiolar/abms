package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UpkeepPaymentAsync {

	void pay(String token, String amount, String description, String month, UserDetails userDetails, AsyncCallback<Void> callback);

}
