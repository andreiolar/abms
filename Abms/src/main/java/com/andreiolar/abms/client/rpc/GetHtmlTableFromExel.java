package com.andreiolar.abms.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;

public interface GetHtmlTableFromExel extends RemoteService {

	String getTableFromExcel() throws Exception;
}
