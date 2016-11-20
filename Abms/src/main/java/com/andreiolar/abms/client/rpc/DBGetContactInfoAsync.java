package com.andreiolar.abms.client.rpc;

import java.util.List;

import com.andreiolar.abms.shared.ContactInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBGetContactInfoAsync {

	public void getContacts(AsyncCallback<List<ContactInfo>> callback);
}
