package com.andreiolar.abms.client.exception;

public class ComplaintSubmissionException extends Exception {

	private static final long serialVersionUID = -2147831648612385171L;

	public ComplaintSubmissionException() {
		super();
	}

	public ComplaintSubmissionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ComplaintSubmissionException(String message) {
		super(message);
	}

	public ComplaintSubmissionException(Throwable cause) {
		super(cause);
	}
}
