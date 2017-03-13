package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBSendMessageAsync {

	void sendMessage(String source, String destination, String message, AsyncCallback<Boolean> callback);

}
