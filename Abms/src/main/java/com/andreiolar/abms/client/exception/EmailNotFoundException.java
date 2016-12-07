package com.andreiolar.abms.client.exception;

public class EmailNotFoundException extends Exception {

	private static final long serialVersionUID = 1988614004211835823L;

	public EmailNotFoundException() {
		super();
	}

	public EmailNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public EmailNotFoundException(String message) {
		super(message);
	}

	public EmailNotFoundException(Throwable cause) {
		super(cause);
	}

}
