package com.andreiolar.abms.shared;

public class NameValidator extends Validator {

	@Override
	public boolean validate(String value) {
		if (value.matches("[a-zA-z]+([ -][a-zA-Z]+)*")) {
			errorMessage = "";
			return true;
		} else {
			errorMessage = "Enter a valid name";
			return false;
		}
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}
}