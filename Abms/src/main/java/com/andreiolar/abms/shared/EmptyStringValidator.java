package com.andreiolar.abms.shared;

public class EmptyStringValidator extends Validator {

	@Override
	public boolean validate(String value) {
		if (!value.matches("^$")) {
			errorMessage = "";
			return true;
		} else {
			errorMessage = "Can't be empty";
			return false;
		}
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}
}
