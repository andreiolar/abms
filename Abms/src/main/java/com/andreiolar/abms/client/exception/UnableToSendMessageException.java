package com.andreiolar.abms.client.exception;

public class UnableToSendMessageException extends Exception {

	private static final long serialVersionUID = -4897528264827992443L;

	public UnableToSendMessageException() {
		super();
	}

	public UnableToSendMessageException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnableToSendMessageException(String message) {
		super(message);
	}

	public UnableToSendMessageException(Throwable cause) {
		super(cause);
	}
}
