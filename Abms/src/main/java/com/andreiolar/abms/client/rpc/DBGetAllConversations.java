package com.andreiolar.abms.client.rpc;

import java.util.List;

import com.andreiolar.abms.shared.Conversation;
import com.andreiolar.abms.shared.UserInfo;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBGetAllConversations extends RemoteService {

	public List<Conversation> getAllMessages(UserInfo userInfo, String conversationFilter) throws Exception;

}
