package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.Email;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBCheckForEmailAsync {

	public void checkForEmail(String emailAddress, AsyncCallback<Email> callback);
}
