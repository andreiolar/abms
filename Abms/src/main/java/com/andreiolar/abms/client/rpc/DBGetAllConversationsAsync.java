package com.andreiolar.abms.client.rpc;

import java.util.List;

import com.andreiolar.abms.shared.Conversation;
import com.andreiolar.abms.shared.UserInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBGetAllConversationsAsync {

	public void getAllMessages(UserInfo userInfo, String conversationFilter, AsyncCallback<List<Conversation>> callback);

}
