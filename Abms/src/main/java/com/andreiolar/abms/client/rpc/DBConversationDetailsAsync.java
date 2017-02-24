package com.andreiolar.abms.client.rpc;

import java.util.List;

import com.andreiolar.abms.shared.ConversationDetails;
import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBConversationDetailsAsync {

	void getConversationDetails(UserDetails userDetails, AsyncCallback<List<ConversationDetails>> callback);

}
