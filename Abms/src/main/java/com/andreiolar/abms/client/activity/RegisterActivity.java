package com.andreiolar.abms.client.activity;

import com.andreiolar.abms.client.ClientFactory;
import com.andreiolar.abms.client.place.RegisterPlace;
import com.andreiolar.abms.client.view.RegisterView;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class RegisterActivity extends AbstractActivity implements RegisterView.Presenter {

	private ClientFactory clientFactory;
	private String name;

	public RegisterActivity(RegisterPlace palce, ClientFactory clientFactory) {
		this.name = palce.getPlaceName();
		this.clientFactory = clientFactory;
	}

	@Override
	public void start(final AcceptsOneWidget containerWidget, EventBus eventBus) {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				RegisterView registerView = clientFactory.getRegisterView();
				registerView.setName(name);
				registerView.setPresenter(RegisterActivity.this);
				containerWidget.setWidget(registerView.asWidget());

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
