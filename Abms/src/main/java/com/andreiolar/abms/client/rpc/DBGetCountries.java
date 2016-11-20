package com.andreiolar.abms.client.rpc;

import com.andreiolar.abms.shared.Countries;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBGetCountries extends RemoteService {

	public Countries getCountries() throws Exception;

}
