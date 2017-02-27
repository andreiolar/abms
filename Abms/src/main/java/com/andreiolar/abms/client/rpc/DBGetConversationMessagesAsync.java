package com.andreiolar.abms.client.rpc;

import java.util.List;

import com.andreiolar.abms.shared.ConversationMessage;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBGetConversationMessagesAsync {

	void getConversationMessages(String id, AsyncCallback<List<ConversationMessage>> callback);

}
