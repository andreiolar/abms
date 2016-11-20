package com.andreiolar.abms.server;

import com.andreiolar.abms.client.rpc.DateGetter;
import com.andreiolar.abms.utils.DateUtils;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DateGetterImpl extends RemoteServiceServlet implements DateGetter {

	private static final long serialVersionUID = 1753463254005531998L;

	@Override
	public String getCurrentMonth() throws Exception {
		String currentMonth = DateUtils.getCurrentMonth();

		return currentMonth;
	}

	@Override
	public String getPreviousMonth() throws Exception {
		String previousMonth = DateUtils.getPreviousMonth();

		return previousMonth;
	}

	@Override
	public String getCurrentYear() throws Exception {
		String currentYear = DateUtils.getCurrentYear();

		return currentYear;
	}

	@Override
	public Integer getPreviousMonthInt() throws Exception {
		int previousMonth = DateUtils.getPreviousMonthInt();

		return previousMonth;
	}

}
