package com.andreiolar.abms.client;

import com.andreiolar.abms.client.view.AdminView;
import com.andreiolar.abms.client.view.LoginView;
import com.andreiolar.abms.client.view.PasswordRecoveryView;
import com.andreiolar.abms.client.view.RegisterView;
import com.andreiolar.abms.client.view.UserView;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;

public interface ClientFactory {
	EventBus getEventBus();

	PlaceController getPlaceController();

	LoginView getLoginView();

	RegisterView getRegisterView();

	UserView getUserView();

	AdminView getAdminView();

	PasswordRecoveryView getPasswordRecoveryView();

}
