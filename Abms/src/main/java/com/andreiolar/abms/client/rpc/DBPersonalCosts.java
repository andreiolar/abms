package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.PersonalUpkeepInformationWrapper;
import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBPersonalCosts extends RemoteService {

	public PersonalUpkeepInformationWrapper getPersonalUpkeepInformation(UserDetails userDetails, String month) throws Exception;
}
