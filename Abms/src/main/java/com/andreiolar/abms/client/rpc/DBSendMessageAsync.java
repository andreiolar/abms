package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.Conversation;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBSendMessageAsync {

	public void sendMessage(Conversation conversation, AsyncCallback<Boolean> callback);

}
