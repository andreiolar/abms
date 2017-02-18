package com.andreiolar.abms.client.exception;

public class VoteSubmissionException extends Exception {

	private static final long serialVersionUID = -683959542154184016L;

	public VoteSubmissionException() {
		super();
	}

	public VoteSubmissionException(String message, Throwable cause) {
		super(message, cause);
	}

	public VoteSubmissionException(String message) {
		super(message);
	}

	public VoteSubmissionException(Throwable cause) {
		super(cause);
	}
}
