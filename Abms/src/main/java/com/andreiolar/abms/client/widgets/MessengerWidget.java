package com.andreiolar.abms.client.widgets;

import java.util.List;

import com.andreiolar.abms.client.exception.NoConversationsFoundException;
import com.andreiolar.abms.client.rpc.DBConversationDetails;
import com.andreiolar.abms.client.rpc.DBConversationDetailsAsync;
import com.andreiolar.abms.shared.ConversationDetails;
import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialModal;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Hr;

public class MessengerWidget extends Composite implements CustomWidget {

	private UserDetails userDetails;

	public MessengerWidget(UserDetails userDetails) {
		this.userDetails = userDetails;

		initWidget(initializeWidget());
	}

	@Override
	public Widget initializeWidget() {
		MaterialPanel panel = new MaterialPanel();

		MaterialLabel title = new MaterialLabel("Messenger");
		title.setTextColor(Color.BLUE);
		title.setTextAlign(TextAlign.CENTER);
		title.setFontSize("36px");
		title.setFontWeight(FontWeight.BOLD);
		panel.add(title);

		panel.add(new Hr());

		MaterialPanel mainPanel = new MaterialPanel();
		mainPanel.setDisplay(Display.FLEX);

		MaterialPanel conversationDetailPanel = new MaterialPanel();
		MaterialPanel conversationPanel = new MaterialPanel();
		MaterialPanel conversationWithPanel = new MaterialPanel();

		conversationDetailPanel.setWidth("20%");
		conversationDetailPanel.setHeight("500px");
		conversationDetailPanel.setBackgroundColor(Color.BLUE);

		conversationPanel.setWidth("60%");
		conversationPanel.setHeight("500px");
		conversationPanel.setBackgroundColor(Color.GREY_LIGHTEN_3);
		conversationPanel.addStyleName("right-border");

		conversationWithPanel.setWidth("20%");
		conversationWithPanel.setHeight("500px");

		DBConversationDetailsAsync rpc = (DBConversationDetailsAsync) GWT.create(DBConversationDetails.class);
		ServiceDefTarget tar = (ServiceDefTarget) rpc;
		String moduleURL = GWT.getModuleBaseURL() + "DBConversationDetailsImpl";
		tar.setServiceEntryPoint(moduleURL);

		rpc.getConversationDetails(userDetails, new AsyncCallback<List<ConversationDetails>>() {

			@Override
			public void onSuccess(List<ConversationDetails> result) {
				conversationDetailPanel.setTextAlign(TextAlign.LEFT);

				for (ConversationDetails conversationDetails : result) {
					ConversationDetail conversationDetail = new ConversationDetail(conversationDetails);
					conversationDetailPanel.add(conversationDetail);
				}

			}

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof NoConversationsFoundException) {
					MaterialLabel errorLabel = new MaterialLabel();
					errorLabel.setText(caught.getMessage());
					errorLabel.setFontSize("18px");
					errorLabel.setMarginTop(25.0);

					conversationDetailPanel.setTextAlign(TextAlign.CENTER);
					conversationDetailPanel.add(errorLabel);
				} else {
					MaterialModal errorModal = ModalCreator.createModal(caught);
					RootPanel.get().add(errorModal);
					errorModal.open();
				}
			}
		});

		mainPanel.add(conversationDetailPanel);
		mainPanel.add(conversationPanel);
		mainPanel.add(conversationWithPanel);

		panel.add(mainPanel);

		return panel;
	}

}
