package com.andreiolar.abms.client.widgets;

import com.andreiolar.abms.client.rpc.DBGetVotingSession;
import com.andreiolar.abms.client.rpc.DBGetVotingSessionAsync;
import com.andreiolar.abms.shared.UserDetails;
import com.andreiolar.abms.shared.Vote;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Hr;

public class VoteWidget extends Composite implements CustomWidget {

	private UserDetails userDetails;

	public VoteWidget(UserDetails userDetails) {
		this.userDetails = userDetails;

		initWidget(initializeWidget());
	}

	@Override
	public Widget initializeWidget() {
		MaterialPanel panel = new MaterialPanel();

		MaterialLabel title = new MaterialLabel("Vote");
		title.setTextColor(Color.BLUE);
		title.setTextAlign(TextAlign.CENTER);
		title.setFontSize("36px");
		title.setFontWeight(FontWeight.BOLD);
		panel.add(title);

		panel.add(new Hr());

		DBGetVotingSessionAsync rpc = (DBGetVotingSessionAsync) GWT.create(DBGetVotingSession.class);
		ServiceDefTarget tar = (ServiceDefTarget) rpc;
		String moduleURL = GWT.getModuleBaseURL() + "DBGetVotingSessionImpl";
		tar.setServiceEntryPoint(moduleURL);

		rpc.getVotingSession(new AsyncCallback<Vote>() {

			@Override
			public void onSuccess(Vote result) {
				Window.alert(result.toString());
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		});

		return panel;
	}

}
