package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBSubmitVote extends RemoteService {

	public void submitVoteToDB(String voteId, String option, String title, String description, UserDetails userDetails) throws Exception;
}
