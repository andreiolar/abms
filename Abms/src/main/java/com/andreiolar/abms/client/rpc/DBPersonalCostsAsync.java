package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.PersonalUpkeepInformation;
import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBPersonalCostsAsync {

	void getPersonalUpkeepInformation(UserDetails userDetails, String month, AsyncCallback<PersonalUpkeepInformation> callback);

}
