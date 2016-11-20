package com.andreiolar.abms.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class RegisterPlace extends Place {

	private String placeName;

	public RegisterPlace(String token) {
		this.placeName = token;
	}

	public String getPlaceName() {
		return placeName;
	}

	public static class Tokenizer implements PlaceTokenizer<RegisterPlace> {

		@Override
		public RegisterPlace getPlace(String token) {
			return new RegisterPlace(token);
		}

		@Override
		public String getToken(RegisterPlace place) {
			return place.getPlaceName();
		}

	}
}
