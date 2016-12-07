package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.client.exception.EmailNotFoundException;
import com.andreiolar.abms.shared.Email;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBCheckForEmail extends RemoteService {

	public Email checkForEmail(String emailAddress) throws EmailNotFoundException;
}
