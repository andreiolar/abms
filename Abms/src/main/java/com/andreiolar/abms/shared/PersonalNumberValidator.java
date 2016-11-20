package com.andreiolar.abms.shared;

public class PersonalNumberValidator extends Validator {

	@Override
	public boolean validate(String value) {
		if (value.matches("[0-9]{13}")) {
			errorMessage = "";
			return true;
		} else {
			errorMessage = "Enter a valid personal number";
			return false;
		}
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

}
