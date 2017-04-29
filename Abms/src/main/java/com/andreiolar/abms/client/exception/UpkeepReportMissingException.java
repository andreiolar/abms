package com.andreiolar.abms.client.exception;

public class UpkeepReportMissingException extends Exception {

	private static final long serialVersionUID = -5197051585874918393L;

	public UpkeepReportMissingException() {
		super();
	}

	public UpkeepReportMissingException(String message, Throwable cause) {
		super(message, cause);
	}

	public UpkeepReportMissingException(String message) {
		super(message);
	}

	public UpkeepReportMissingException(Throwable cause) {
		super(cause);
	}
}
