package com.andreiolar.abms.client.view;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface RegisterView extends IsWidget {

	void setName(String placeName);

	void setPresenter(Presenter presenter);

	public interface Presenter {
		void goTo(Place place);
	}
}
