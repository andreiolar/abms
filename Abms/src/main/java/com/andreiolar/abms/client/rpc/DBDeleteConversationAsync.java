package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.UserInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBDeleteConversationAsync {

	public void deleteConversation(int convId, UserInfo userInfo, AsyncCallback<Void> callback);

}
