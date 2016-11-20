package com.andreiolar.abms.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class UserPlace extends Place {

	private String username;

	public UserPlace(String username) {
		this.username = username;
	}

	public String getPlaceName() {
		return username;
	}

	public static class Tokenizer implements PlaceTokenizer<UserPlace> {

		@Override
		public UserPlace getPlace(String token) {
			return new UserPlace(token);
		}

		@Override
		public String getToken(UserPlace place) {
			return place.getPlaceName();
		}

	}

}
