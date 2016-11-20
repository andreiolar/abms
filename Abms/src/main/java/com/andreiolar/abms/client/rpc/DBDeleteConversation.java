package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.UserInfo;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBDeleteConversation extends RemoteService {

	public void deleteConversation(int convId, UserInfo userInfo) throws Exception;

}
