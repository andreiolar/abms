package com.andreiolar.abms.shared;

public abstract class Validator {

	public String errorMessage;

	public abstract boolean validate(String value);

	public abstract String getErrorMessage();
}
