package com.andreiolar.abms.client.activity;

import com.andreiolar.abms.client.ClientFactory;
import com.andreiolar.abms.client.place.AdminPlace;
import com.andreiolar.abms.client.view.AdminView;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class AdminActivity extends AbstractActivity implements AdminView.Presenter {

	private ClientFactory clientFactory;
	private String name;

	public AdminActivity(AdminPlace place, ClientFactory clientFactory) {
		this.name = place.getUsername();
		this.clientFactory = clientFactory;
	}

	@Override
	public void start(final AcceptsOneWidget containerWidget, EventBus eventBus) {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				AdminView adminView = clientFactory.getAdminView();
				adminView.setName(name);
				adminView.setPresenter(AdminActivity.this);
				containerWidget.setWidget(adminView.asWidget());

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
