package com.andreiolar.abms.client.exception;

public class MessagesNotFoundException extends Exception {

	public MessagesNotFoundException() {
		super();
	}

	public MessagesNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public MessagesNotFoundException(String message) {
		super(message);
	}

	public MessagesNotFoundException(Throwable cause) {
		super(cause);
	}
}
