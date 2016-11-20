package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;

public interface DBUpdateUser extends RemoteService {

	public Boolean updateUser(String firstName, String lastName, String email, String mobileNumber, String address, String city, String country,
			String idSeries, String personalNumber) throws Exception;
}
