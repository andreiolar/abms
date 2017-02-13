package com.andreiolar.abms.client.exception;

public class PersonalUpkeepInformationNotFoundException extends Exception {

	private static final long serialVersionUID = -6432693640689354355L;

	public PersonalUpkeepInformationNotFoundException() {
		super();
	}

	public PersonalUpkeepInformationNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public PersonalUpkeepInformationNotFoundException(String message) {
		super(message);
	}

	public PersonalUpkeepInformationNotFoundException(Throwable cause) {
		super(cause);
	}
}
