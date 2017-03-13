package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;

public interface DBStartConversation extends RemoteService {

	public void startConversation(String source, String destination, String message) throws Exception;
}
