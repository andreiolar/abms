package com.andreiolar.abms.client.exception;

public class MailException extends Exception {

	private static final long serialVersionUID = 92950276369139806L;

	public MailException() {
		super();
	}

	public MailException(String message, Throwable cause) {
		super(message, cause);
	}

	public MailException(String message) {
		super(message);
	}

	public MailException(Throwable cause) {
		super(cause);
	}
}
