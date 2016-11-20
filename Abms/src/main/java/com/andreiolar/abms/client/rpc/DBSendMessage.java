package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.Conversation;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBSendMessage extends RemoteService {

	public Boolean sendMessage(Conversation conversation) throws Exception;
}
