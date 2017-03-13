package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;

public interface DBSendMessage extends RemoteService {

	public Boolean sendMessage(String source, String destination, String message) throws Exception;
}
