package com.andreiolar.abms.client.activity;

import com.andreiolar.abms.client.ClientFactory;
import com.andreiolar.abms.client.place.UserPlace;
import com.andreiolar.abms.client.view.UserView;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class UserActivity extends AbstractActivity implements UserView.Presenter {

	private ClientFactory clientFactory;
	private String username;

	public UserActivity(UserPlace place, ClientFactory clientFactory) {
		this.username = place.getUsername();
		this.clientFactory = clientFactory;
	}

	@Override
	public void start(final AcceptsOneWidget containerWidget, EventBus eventBus) {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				UserView userView = clientFactory.getUserView();
				userView.setUsername(username);
				userView.setPresenter(UserActivity.this);
				containerWidget.setWidget(userView.asWidget());

			}

			@Override
			public void onFailure(Throwable reason) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	public void goTo(final Place place) {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				clientFactory.getPlaceController().goTo(place);

			}

			@Override
			public void onFailure(Throwable reason) {
				// TODO Auto-generated method stub

			}
		});

	}

}
