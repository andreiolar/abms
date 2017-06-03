package com.andreiolar.abms.client.exception;

public class NoUpkeepReportsFoundForDateException extends Exception {

	private static final long serialVersionUID = -4013740936523331145L;

	public NoUpkeepReportsFoundForDateException() {
		super();
	}

	public NoUpkeepReportsFoundForDateException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoUpkeepReportsFoundForDateException(String message) {
		super(message);
	}

	public NoUpkeepReportsFoundForDateException(Throwable cause) {
		super(cause);
	}
}
