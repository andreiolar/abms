package com.andreiolar.abms.client.utils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * Utility class used for client Date manipulation.
 * 
 * @author Andrei Olar
 **/
public final class DateUtil {

	private static final String DATE_FORMAT = "yyyy-M-dd";
	private static final String DATE_SEPARATOR = "-";

	private static List<String> months = Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September",
			"October", "November", "December");

	private DateUtil() {
		// Avoid instantiation
	}

	/**
	 * Used to create a date based on a given day, month and year.
	 * 
	 * @param dd
	 *            The given day.
	 * 
	 * @param mm
	 *            The given month.
	 * 
	 * @param yyyy
	 *            The given year.
	 * 
	 * @return The created {@link Date}.
	 **/
	public static Date getDate(Integer dd, Integer mm, Integer yyyy) {
		if (dd == null || mm == null || yyyy == null)
			return null;

		Date retVal = null;
		try {
			retVal = DateTimeFormat.getFormat(DATE_FORMAT).parse(yyyy + DATE_SEPARATOR + mm + DATE_SEPARATOR + dd);
		} catch (Exception e) {
			retVal = null;
		}

		return retVal;
	}

	/**
	 * Used to get the year as String based on a date.
	 * 
	 * @param date
	 *            The date.
	 * 
	 * @return A String representation of the year.
	 **/
	public static String getYearAsString(Date date) {
		return (date == null) ? null : DateTimeFormat.getFormat(DATE_FORMAT).format(date).split(DATE_SEPARATOR)[0];
	}

	/**
	 * Used to get the month as String based on a date.
	 * 
	 * @param date
	 *            The date.
	 * 
	 * @return A String representation of the month.
	 **/
	public static String getMonthAsString(Date date) {
		return (date == null) ? null : DateTimeFormat.getFormat("yyyy-MMMM-dd").format(date).split(DATE_SEPARATOR)[1];
	}

	/**
	 * Used to get the day as String based on a date.
	 * 
	 * @param date
	 *            The date.
	 * 
	 * @return A String representation of the day.
	 **/
	public static String getDayAsString(Date date) {
		return (date == null) ? null : DateTimeFormat.getFormat(DATE_FORMAT).format(date).split(DATE_SEPARATOR)[2];
	}

	/**
	 * Used to get the previous month as String based on a date.
	 * 
	 * @param date
	 *            The date.
	 * 
	 * @return A String representation of the month.
	 **/
	public static String getPreviousMonthAsString(Date date) {
		String monthAsString = getMonthAsString(date);

		int index = months.indexOf(monthAsString);
		int returnIndex = index - 1;

		if (index == 0) {
			returnIndex = 11;
		}

		return months.get(returnIndex);
	}

	/**
	 * Used to get the year for the previous month as String.
	 * 
	 * @param date
	 *            The date.
	 * 
	 * @return A String representation of the year.
	 **/
	public static String getYearForPreviousMonth(Date date) {
		String previousMonthAsString = getPreviousMonthAsString(date);
		String yearAsString = getYearAsString(date);
		int year = Integer.parseInt(yearAsString);

		if (previousMonthAsString.equals("December")) {
			year -= 1;
		}

		return String.valueOf(year);
	}
}
