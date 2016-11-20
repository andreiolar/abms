package com.andreiolar.abms.client.activity;

import com.andreiolar.abms.client.ClientFactory;
import com.andreiolar.abms.client.place.LoginPlace;
import com.andreiolar.abms.client.view.LoginView;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class LoginActivity extends AbstractActivity implements LoginView.Presenter {

	private ClientFactory clientFactory;
	private String name;

	public LoginActivity(LoginPlace place, ClientFactory clientFactory) {
		this.name = place.getPlaceName();
		this.clientFactory = clientFactory;
	}

	@Override
	public void start(final AcceptsOneWidget containerWidget, EventBus eventBus) {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				LoginView loginView = clientFactory.getLoginView();
				loginView.setName(name);
				loginView.setPresenter(LoginActivity.this);
				containerWidget.setWidget(loginView.asWidget());

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
