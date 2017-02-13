package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.PersonalUpkeepInformation;
import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBPersonalCosts extends RemoteService {

	public PersonalUpkeepInformation getPersonalUpkeepInformation(UserDetails userDetails, String month) throws Exception;
}
