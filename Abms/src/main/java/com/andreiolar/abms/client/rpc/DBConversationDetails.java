package com.andreiolar.abms.client.rpc;

import java.util.List;

import com.andreiolar.abms.shared.ConversationDetails;
import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBConversationDetails extends RemoteService {

	public List<ConversationDetails> getConversationDetails(UserDetails userDetails) throws Exception;
}
