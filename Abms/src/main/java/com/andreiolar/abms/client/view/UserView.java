package com.andreiolar.abms.client.view;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface UserView extends IsWidget {

	void setUsername(String username);

	void setPresenter(Presenter presenter);

	public interface Presenter {
		void goTo(Place place);
	}
}
