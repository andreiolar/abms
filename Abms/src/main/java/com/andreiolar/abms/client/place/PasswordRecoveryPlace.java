package com.andreiolar.abms.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class PasswordRecoveryPlace extends Place {

	private String token;

	public PasswordRecoveryPlace(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public static class Tokenizer implements PlaceTokenizer<PasswordRecoveryPlace> {

		@Override
		public PasswordRecoveryPlace getPlace(String token) {
			return new PasswordRecoveryPlace(token);
		}

		@Override
		public String getToken(PasswordRecoveryPlace place) {
			return place.getToken();
		}
	}

}
