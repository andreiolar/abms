package com.andreiolar.abms.client.exception;

public class VoteOptionsNotFoundException extends Exception {

	private static final long serialVersionUID = -618465814749932899L;

	public VoteOptionsNotFoundException() {
		super();
	}

	public VoteOptionsNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public VoteOptionsNotFoundException(String message) {
		super(message);
	}

	public VoteOptionsNotFoundException(Throwable cause) {
		super(cause);
	}
}
