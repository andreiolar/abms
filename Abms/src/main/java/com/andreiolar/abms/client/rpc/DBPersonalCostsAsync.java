package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.PersonalUpkeepInformation;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBPersonalCostsAsync {

	void getPersonalUpkeepInformation(String username, String month, AsyncCallback<PersonalUpkeepInformation> callback);

}
