package com.andreiolar.abms.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class AdminPlace extends Place {

	private String username;

	public AdminPlace(String token) {
		this.username = token;
	}

	public String getUsername() {
		return username;
	}

	public static class Tokenizer implements PlaceTokenizer<AdminPlace> {

		@Override
		public AdminPlace getPlace(String token) {
			return new AdminPlace(token);
		}

		@Override
		public String getToken(AdminPlace place) {
			return place.getUsername();
		}

	}
}
