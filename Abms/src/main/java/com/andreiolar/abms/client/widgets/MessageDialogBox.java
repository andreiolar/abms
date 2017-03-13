package com.andreiolar.abms.client.widgets;

import java.util.List;

import com.andreiolar.abms.client.constants.DialogBoxConstants;
import com.andreiolar.abms.client.constants.UserMenuConstants;
import com.andreiolar.abms.client.rpc.DBGetTenantSuggestions;
import com.andreiolar.abms.client.rpc.DBGetTenantSuggestionsAsync;
import com.andreiolar.abms.client.rpc.DBSendMessage;
import com.andreiolar.abms.client.rpc.DBSendMessageAsync;
import com.andreiolar.abms.client.utils.DialogBoxCreator;
import com.andreiolar.abms.shared.Conversation;
import com.andreiolar.abms.shared.UserInfo;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MessageDialogBox {

	private UserInfo userInfo;

	public MessageDialogBox(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public DialogBox initializeDialogBox() {
		final DialogBox messageDialogBox = new DialogBox();
		messageDialogBox.setText(UserMenuConstants.MESSAGE_DIALOG_BOX_TITLE);
		messageDialogBox.setWidth("500px");

		VerticalPanel panel = new VerticalPanel();
		panel.setSpacing(6);
		messageDialogBox.setWidget(panel);

		Grid grid = new Grid(4, 2);

		Label toLabel = new Label();
		toLabel.setText("To");

		MultiWordSuggestOracle oracle = getDBSuggestions();

		final CustomTexBox toBox = new CustomTexBox();

		final SuggestBox suggestBox = new SuggestBox(oracle, toBox);

		Label subjectLabel = new Label();
		subjectLabel.setText("Subject");

		final TextBox subjectBox = new TextBox();
		subjectBox.setMaxLength(15);

		Label messageLabel = new Label();
		messageLabel.setText("Message");

		final TextArea messageBox = new TextArea();
		messageBox.setSize("350px", "150px");
		messageBox.getElement().setAttribute("maxlength", "500");

		final Label charsRemainingLabel = new Label();
		charsRemainingLabel.setText("500 characters remaining.");
		charsRemainingLabel.getElement().getStyle().setFontWeight(FontWeight.LIGHTER);
		charsRemainingLabel.getElement().getStyle().setFontStyle(FontStyle.ITALIC);

		messageBox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				String text = messageBox.getText();
				int length = text.length();
				int remaining = 500 - length;
				charsRemainingLabel.setText(remaining + " characters remaining.");
			}
		});

		grid.setWidget(0, 0, toLabel);
		grid.setWidget(0, 1, suggestBox);

		grid.setWidget(1, 0, subjectLabel);
		grid.setWidget(1, 1, subjectBox);

		grid.setWidget(2, 0, messageLabel);
		grid.setWidget(2, 1, messageBox);

		grid.setWidget(3, 1, charsRemainingLabel);

		grid.getWidget(0, 0).getElement().getStyle().setMarginBottom(5.0, Unit.PX);
		grid.getWidget(0, 1).getElement().getStyle().setMarginBottom(5.0, Unit.PX);
		grid.getWidget(1, 0).getElement().getStyle().setMarginBottom(5.0, Unit.PX);
		grid.getWidget(1, 1).getElement().getStyle().setMarginBottom(5.0, Unit.PX);

		Button sendButton = new Button();
		sendButton.setText("Send");
		sendButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String destination = suggestBox.getText();
				String subject = subjectBox.getText();
				String messageText = messageBox.getText();
				String error = "";

				boolean isDestination = true;
				boolean isSubject = true;
				boolean isMessage = true;
				boolean isValidAptNumber = true;

				if (destination == null || destination.trim().equals("")) {
					error += "<p>Please select destination!</p>";
					isDestination = false;
				}

				if (subject == null || subject.trim().equals("")) {
					error += "<p>Please type in a subject!</p>";
					isSubject = false;
				}

				if (messageText == null || messageText.trim().equals("")) {
					error += "<p>Please write a message!</p>";
					isMessage = false;
				}

				String aptNumber = getAptNumberFromSuggestion(destination);

				if (aptNumber == null || aptNumber.trim().equals("")) {
					error += "<p>Destination was not found!</p>";
					isValidAptNumber = false;
				}

				if (isDestination && isSubject && isMessage && isValidAptNumber) {
					DBSendMessageAsync rpc = (DBSendMessageAsync) GWT.create(DBSendMessage.class);
					ServiceDefTarget tar = (ServiceDefTarget) rpc;
					String moduleURL = GWT.getModuleBaseURL() + "DBSendMessageImpl";
					tar.setServiceEntryPoint(moduleURL);

					Conversation conversation = new Conversation(userInfo.getUsername(), aptNumber, messageText);

					rpc.sendMessage(userInfo.getUsername(), "", "", new AsyncCallback<Boolean>() {

						@Override
						public void onSuccess(Boolean result) {
							messageDialogBox.hide();
							DialogBox dialogBox = DialogBoxCreator.createDialogBox(UserMenuConstants.SUCCESS_SEND_MESSAGE,
									UserMenuConstants.SUCCESS_SEND_MESSAGE_MESSAGE, DialogBoxConstants.CLOSE_BUTTON, false, false);
							dialogBox.setGlassEnabled(true);
							dialogBox.setAnimationEnabled(true);
							dialogBox.center();
							dialogBox.show();
						}

						@Override
						public void onFailure(Throwable caught) {
							DialogBox dialogBox = DialogBoxCreator.createDialogBox(UserMenuConstants.FAILED_SEND_MESSAGE, caught.getMessage(),
									DialogBoxConstants.CLOSE_BUTTON, false, false);
							dialogBox.setGlassEnabled(true);
							dialogBox.setAnimationEnabled(true);
							dialogBox.center();
							dialogBox.show();
						}

					});
				} else {
					DialogBox dialogBox = DialogBoxCreator.createDialogBox(UserMenuConstants.FAILED_SEND_MESSAGE, error,
							DialogBoxConstants.CLOSE_BUTTON, false, false);
					dialogBox.setGlassEnabled(true);
					dialogBox.setAnimationEnabled(true);
					dialogBox.center();
					dialogBox.show();
				}
			}
		});

		Button cancelButton = new Button();
		cancelButton.setText("Cancel");
		cancelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				messageDialogBox.hide();
			}
		});

		FlowPanel flowPanel = new FlowPanel();
		flowPanel.add(sendButton);
		flowPanel.add(cancelButton);

		flowPanel.getWidget(0).getElement().getStyle().setMarginRight(5.0, Unit.PX);

		panel.add(grid);
		panel.add(flowPanel);

		panel.setCellHorizontalAlignment(flowPanel, HasHorizontalAlignment.ALIGN_RIGHT);

		return messageDialogBox;
	}

	protected String getAptNumberFromSuggestion(String destination) {
		String match = null;

		RegExp regExp = RegExp.compile("Apt. Number: [0-9]{1,3}");
		MatchResult matcher = regExp.exec(destination);

		if (regExp.test(destination)) {
			match = matcher.getGroup(0);
		}

		if (match != null) {
			regExp = RegExp.compile("[0-9]{1,3}");
			matcher = regExp.exec(match);

			if (regExp.test(match)) {
				match = matcher.getGroup(0);
			}
		}

		return match;
	}

	private MultiWordSuggestOracle getDBSuggestions() {
		final MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();

		DBGetTenantSuggestionsAsync rpc = (DBGetTenantSuggestionsAsync) GWT.create(DBGetTenantSuggestions.class);
		ServiceDefTarget tar = (ServiceDefTarget) rpc;
		String moduleURL = GWT.getModuleBaseURL() + "DBGetTenantSuggestionsImpl";
		tar.setServiceEntryPoint(moduleURL);

		rpc.getTenantSuggestions(userInfo, new AsyncCallback<List<String>>() {

			@Override
			public void onSuccess(List<String> result) {
				oracle.addAll(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				// Do nothing
			}
		});

		return oracle;
	}

}
