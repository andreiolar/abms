package com.andreiolar.abms.client.widgets;

import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Hr;

public class VoteResultsWidget extends Composite implements CustomWidget {

	private UserDetails userDetails;

	public VoteResultsWidget(UserDetails userDetails) {
		this.userDetails = userDetails;

		initWidget(initializeWidget());
	}

	@Override
	public Widget initializeWidget() {
		MaterialPanel panel = new MaterialPanel();

		MaterialLabel title = new MaterialLabel("View Vote Results");
		title.setTextColor(Color.BLUE);
		title.setTextAlign(TextAlign.CENTER);
		title.setFontSize("36px");
		title.setFontWeight(FontWeight.BOLD);
		panel.add(title);

		panel.add(new Hr());

		MaterialLabel label = new MaterialLabel();
		label.setText("Vote results for finished voting sessions can be seen here by selecting the desired voting session from the drop-down below.");
		label.setMarginTop(25.0);
		label.setMarginLeft(25.0);
		label.setFontSize("18px");
		panel.add(label);

		return panel;
	}

}
