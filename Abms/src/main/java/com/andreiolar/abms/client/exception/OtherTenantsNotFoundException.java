package com.andreiolar.abms.client.exception;

public class OtherTenantsNotFoundException extends Exception {

	private static final long serialVersionUID = -5040864695334293851L;

	public OtherTenantsNotFoundException() {
		super();
	}

	public OtherTenantsNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public OtherTenantsNotFoundException(String message) {
		super(message);
	}

	public OtherTenantsNotFoundException(Throwable cause) {
		super(cause);
	}
}
