package com.andreiolar.abms.client.rpc;

import java.util.List;

import com.andreiolar.abms.shared.ConversationMessage;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBGetConversationMessages extends RemoteService {

	public List<ConversationMessage> getConversationMessages(String id) throws Exception;
}
