package com.andreiolar.abms.client.exception;

public class NoReadingsFoundForDateException extends Exception {

	private static final long serialVersionUID = -2658356863426037968L;

	public NoReadingsFoundForDateException() {
		super();
	}

	public NoReadingsFoundForDateException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoReadingsFoundForDateException(String message) {
		super(message);
	}

	public NoReadingsFoundForDateException(Throwable cause) {
		super(cause);
	}
}
