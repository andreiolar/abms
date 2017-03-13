package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBStartConversationAsync {

	void startConversation(String source, String destination, String message, AsyncCallback<Void> callback);

}
