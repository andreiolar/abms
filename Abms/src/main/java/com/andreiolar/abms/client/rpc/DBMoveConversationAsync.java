package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.UserInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBMoveConversationAsync {

	public void moveConversation(int convId, String filter, UserInfo userInfo, AsyncCallback<Void> callback);

}
