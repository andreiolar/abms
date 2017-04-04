package com.andreiolar.abms.client.rpc;

import java.util.List;

import com.andreiolar.abms.shared.SelfReading;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DBGetReadingsForDate extends RemoteService {

	public List<SelfReading> getReadingsForDate(String date) throws Exception;
}
