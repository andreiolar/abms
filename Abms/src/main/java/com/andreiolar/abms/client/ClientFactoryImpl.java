package com.andreiolar.abms.client;

import com.andreiolar.abms.client.view.AdminView;
import com.andreiolar.abms.client.view.LoginView;
import com.andreiolar.abms.client.view.UserView;
import com.andreiolar.abms.client.view.impl.AdminViewImpl;
import com.andreiolar.abms.client.view.impl.LoginViewImpl;
import com.andreiolar.abms.client.view.impl.UserPanel;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.PlaceController;

@SuppressWarnings("deprecation")
public class ClientFactoryImpl implements ClientFactory {

	private final EventBus eventBus = new SimpleEventBus();

	private final PlaceController placeController = new PlaceController(eventBus);
	// private final LoginView loginView = new LoginViewImpl();
	// private final RegisterView registerView = new RegisterViewImpl();
	// private final UserView userView = new UserViewImpl();
	// private final AdminView adminView = new AdminViewImpl();

	@Override
	public EventBus getEventBus() {
		return eventBus;
	}

	@Override
	public PlaceController getPlaceController() {
		return placeController;
	}

	@Override
	public LoginView getLoginView() {
		return new LoginViewImpl();
	}

	@Override
	public UserView getUserView() {
		return new UserPanel();
	}

	@Override
	public AdminView getAdminView() {
		return new AdminViewImpl();
	}

}
