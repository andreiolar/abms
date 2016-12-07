package com.andreiolar.abms.client;

import com.andreiolar.abms.client.activity.AdminActivity;
import com.andreiolar.abms.client.activity.LoginActivity;
import com.andreiolar.abms.client.activity.PasswordRecoveryActivity;
import com.andreiolar.abms.client.activity.UserActivity;
import com.andreiolar.abms.client.place.AdminPlace;
import com.andreiolar.abms.client.place.LoginPlace;
import com.andreiolar.abms.client.place.PasswordRecoveryPlace;
import com.andreiolar.abms.client.place.UserPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;

public class AppActivityMapper implements ActivityMapper {

	private ClientFactory clientFactory;

	public AppActivityMapper(ClientFactory clientFactory) {
		super();
		this.clientFactory = clientFactory;
	}

	@Override
	public Activity getActivity(Place place) {
		if (place instanceof LoginPlace) {
			return new LoginActivity((LoginPlace) place, clientFactory);
		} else if (place instanceof UserPlace) {
			return new UserActivity((UserPlace) place, clientFactory);
		} else if (place instanceof AdminPlace) {
			return new AdminActivity((AdminPlace) place, clientFactory);
		} else if (place instanceof PasswordRecoveryPlace) {
			return new PasswordRecoveryActivity((PasswordRecoveryPlace) place, clientFactory);
		}

		return null;
	}

}
