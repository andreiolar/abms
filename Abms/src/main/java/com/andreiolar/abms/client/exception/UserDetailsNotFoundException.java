package com.andreiolar.abms.client.exception;

public class UserDetailsNotFoundException extends Exception {

	private static final long serialVersionUID = -200937765465791184L;

	public UserDetailsNotFoundException() {
		super();
	}

	public UserDetailsNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserDetailsNotFoundException(String message) {
		super(message);
	}

	public UserDetailsNotFoundException(Throwable cause) {
		super(cause);
	}
}
