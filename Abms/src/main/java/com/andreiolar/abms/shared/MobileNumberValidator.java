package com.andreiolar.abms.shared;

public class MobileNumberValidator extends Validator {

	@Override
	public boolean validate(String value) {
		if (value.matches("[0-9]{10}")) {
			errorMessage = "";
			return true;
		} else {
			errorMessage = "Enter a valid phone number";
			return false;
		}
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

}
