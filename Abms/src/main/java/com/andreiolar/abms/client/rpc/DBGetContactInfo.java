package com.andreiolar.abms.client.rpc;

import java.util.List;

import com.andreiolar.abms.shared.ContactInfo;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBGetContactInfo extends RemoteService {

	public List<ContactInfo> getContacts() throws Exception;
}
