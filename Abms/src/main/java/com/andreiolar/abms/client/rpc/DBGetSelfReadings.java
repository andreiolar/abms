package com.andreiolar.abms.client.rpc;

import java.util.List;

import com.andreiolar.abms.shared.SelfReading;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBGetSelfReadings extends RemoteService {

	public List<SelfReading> getSelfReadings(String date) throws Exception;
}
