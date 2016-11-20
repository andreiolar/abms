package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.Countries;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBGetCountriesAsync {

	public void getCountries(AsyncCallback<Countries> callback);
}
