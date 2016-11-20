package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.UserInfo;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBMoveConversation extends RemoteService {

	public void moveConversation(int convId, String filter, UserInfo userInfo) throws Exception;
}
