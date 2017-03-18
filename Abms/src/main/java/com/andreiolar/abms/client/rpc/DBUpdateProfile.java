package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBUpdateProfile extends RemoteService {

	public boolean updateProfile(UserDetails newDetails, String updatedUsername) throws Exception;
}
