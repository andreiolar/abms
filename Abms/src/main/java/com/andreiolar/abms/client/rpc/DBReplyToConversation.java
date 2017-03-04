package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.ConversationMessage;
import com.andreiolar.abms.shared.ReplyMessage;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBReplyToConversation extends RemoteService {

	public ConversationMessage replyToConversation(ReplyMessage message) throws Exception;
}
