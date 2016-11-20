package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBUpdateUserAsync {

	void updateUser(String firstName, String lastName, String email, String mobileNumber, String address, String city, String country,
			String idSeries, String personalNumber, AsyncCallback<Boolean> callback);

}
