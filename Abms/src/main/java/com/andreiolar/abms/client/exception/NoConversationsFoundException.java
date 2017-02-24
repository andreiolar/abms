package com.andreiolar.abms.client.exception;

public class NoConversationsFoundException extends Exception {

	private static final long serialVersionUID = 752275170517564296L;

	public NoConversationsFoundException() {
		super();
	}

	public NoConversationsFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoConversationsFoundException(String message) {
		super(message);
	}

	public NoConversationsFoundException(Throwable cause) {
		super(cause);
	}
}
