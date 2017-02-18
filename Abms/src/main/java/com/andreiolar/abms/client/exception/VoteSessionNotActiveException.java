package com.andreiolar.abms.client.exception;

public class VoteSessionNotActiveException extends Exception {

	private static final long serialVersionUID = -4023128861314946349L;

	public VoteSessionNotActiveException() {
		super();
	}

	public VoteSessionNotActiveException(String message, Throwable cause) {
		super(message, cause);
	}

	public VoteSessionNotActiveException(String message) {
		super(message);
	}

	public VoteSessionNotActiveException(Throwable cause) {
		super(cause);
	}
}
