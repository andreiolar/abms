package com.andreiolar.abms.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtils {

	public static String getCurrentMonth() throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, 0);
		String month = new SimpleDateFormat("MMM").format(calendar.getTime());

		return month;
	}

	public static String getPreviousMonth() throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		String month = new SimpleDateFormat("MMM").format(calendar.getTime());

		return month;
	}

	public static String getCurrentYear() throws Exception {
		Calendar calendar = Calendar.getInstance();

		return String.valueOf(calendar.get(Calendar.YEAR));
	}

	public static int getPreviousMonthInt() throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		int month = calendar.get(Calendar.MONTH);

		return month;
	}

}
