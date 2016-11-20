package com.andreiolar.abms.client.view;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface UserView extends IsWidget {

	void setName(String name);

	void setPresenter(Presenter presenter);

	public interface Presenter {
		void goTo(Place place);
	}
}
