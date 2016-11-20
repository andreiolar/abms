package com.andreiolar.abms.client.rpc;

import java.util.List;

import com.andreiolar.abms.shared.Message;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBGetMessagesForConversationAsync {

	public void getAllMessagesForConversation(int conversationId, AsyncCallback<List<Message>> callback);

}
