package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.ReplyMessage;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBReplyToConversationAsync {

	public void replyToConversation(ReplyMessage message, AsyncCallback<Void> callback);

}
