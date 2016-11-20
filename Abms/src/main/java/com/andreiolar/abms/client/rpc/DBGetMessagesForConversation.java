package com.andreiolar.abms.client.rpc;

import java.util.List;

import com.andreiolar.abms.shared.Message;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBGetMessagesForConversation extends RemoteService {

	public List<Message> getAllMessagesForConversation(int conversationId) throws Exception;
}
