package com.andreiolar.abms.client.widgets;

import java.util.ArrayList;
import java.util.List;

import com.andreiolar.abms.client.constants.DialogBoxConstants;
import com.andreiolar.abms.client.constants.UserMenuConstants;
import com.andreiolar.abms.client.rpc.DBGetVotingDescription;
import com.andreiolar.abms.client.rpc.DBGetVotingDescriptionAsync;
import com.andreiolar.abms.client.rpc.DBGetVotingOptions;
import com.andreiolar.abms.client.rpc.DBGetVotingOptionsAsync;
import com.andreiolar.abms.client.rpc.DBSubmitVote;
import com.andreiolar.abms.client.rpc.DBSubmitVoteAsync;
import com.andreiolar.abms.client.utils.DialogBoxCreator;
import com.andreiolar.abms.shared.UserInfo;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class VotingWidget extends Composite implements CustomWidget {

	private UserInfo userInfo;

	public VotingWidget(UserInfo userInfo) {
		this.userInfo = userInfo;

		initWidget(initializeWidget());
	}

	@Override
	public Widget initializeWidget() {
		final VerticalPanel panel = new VerticalPanel();
		panel.setSpacing(40);

		DOM.getElementById("loading").getStyle().setDisplay(Display.BLOCK);
		DBGetVotingDescriptionAsync rpc = (DBGetVotingDescriptionAsync) GWT.create(DBGetVotingDescription.class);
		ServiceDefTarget tar = (ServiceDefTarget) rpc;
		String moduleURL = GWT.getModuleBaseURL() + "DBGetVotingDescriptionImpl";
		tar.setServiceEntryPoint(moduleURL);

		rpc.getVotingDescription(new AsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
				panel.add(new HTML(result));
			}

			@Override
			public void onFailure(Throwable caught) {
				DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
				panel.add(new HTML("<p style=\"font-size:20px\"><b>" + caught.getMessage() + "</b></p>"));
			}
		});

		DBGetVotingOptionsAsync rpcService = (DBGetVotingOptionsAsync) GWT.create(DBGetVotingOptions.class);
		ServiceDefTarget target = (ServiceDefTarget) rpcService;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "DBGetVotingOptionsImpl";
		target.setServiceEntryPoint(moduleRelativeURL);

		rpcService.getVotingOptions(userInfo.getUsername(), new AsyncCallback<List<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
				panel.add(new HTML("<p style=\"font-size:20px\"><b>" + caught.getMessage() + "</b></p>"));
			}

			@Override
			public void onSuccess(List<String> result) {
				DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
				final List<RadioButton> radioButtons = new ArrayList<RadioButton>();
				final Label buttonSelected = new Label();

				ClickHandler radioButtonClickHandler = new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						buttonSelected.setText(((RadioButton) event.getSource()).getText());
					}
				};

				for (final String voteOption : result) {
					SafeHtml html = new SafeHtml() {

						private static final long serialVersionUID = 4171878527811261150L;

						@Override
						public String asString() {
							return "<label class=\"radio-button-label\">" + voteOption + "</label>";
						}
					};

					final RadioButton radioButton = new RadioButton("options", html);
					radioButton.ensureDebugId("radio-button-" + voteOption);
					radioButton.addClickHandler(radioButtonClickHandler);
					radioButton.setStyleName("radio-button-vote");
					radioButtons.add(radioButton);
					panel.add(radioButton);
				}

				Button submiButton = new Button();
				submiButton.setText("Vote");
				submiButton.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						String votedOption = buttonSelected.getText();

						if (votedOption != null && !votedOption.trim().equals("")) {
							DOM.getElementById("loading").getStyle().setDisplay(Display.BLOCK);
							DBSubmitVoteAsync rpcService = (DBSubmitVoteAsync) GWT.create(DBSubmitVote.class);
							ServiceDefTarget target = (ServiceDefTarget) rpcService;
							String moduleRelativeURL = GWT.getModuleBaseURL() + "DBSubmitVoteImpl";
							target.setServiceEntryPoint(moduleRelativeURL);

							rpcService.submitVoteToDB(votedOption, userInfo.getUsername(), new AsyncCallback<Void>() {

								@Override
								public void onFailure(Throwable caught) {
									DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
									DialogBox dialogBox = DialogBoxCreator.createDialogBox(UserMenuConstants.DIALOG_BOX_FAILED_SUBMIT_VOTE_TITLE,
											UserMenuConstants.DIALOG_BOX_FAILED_SUBMIT_VOTE_MESSAGE + ": " + caught.getMessage(),
											DialogBoxConstants.CLOSE_BUTTON, false, false);
									dialogBox.setGlassEnabled(true);
									dialogBox.setAnimationEnabled(true);
									dialogBox.center();
									dialogBox.show();
								}

								@Override
								public void onSuccess(Void result) {
									DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
									DialogBox dialogBox = DialogBoxCreator.createDialogBox(UserMenuConstants.DIALOG_BOX_SUCCESS_SUBMIT_VOTE_TITLE,
											UserMenuConstants.DIALOG_BOX_SUCCESS_SUBMIT_VOTE_MESSAGE, DialogBoxConstants.CLOSE_BUTTON, false, true);
									dialogBox.setGlassEnabled(true);
									dialogBox.setAnimationEnabled(true);
									dialogBox.center();
									dialogBox.show();
								}
							});

						} else {
							DialogBox dialogBox = DialogBoxCreator.createDialogBox(UserMenuConstants.DIALOG_BOX_FAILED_CHECK_SUBMIT_VOTE_TITLE,
									UserMenuConstants.DIALOG_BOX_FAILED_CHECK_SUBMIT_VOTE_MESSAGE, DialogBoxConstants.CLOSE_BUTTON, false, false);
							dialogBox.setGlassEnabled(true);
							dialogBox.setAnimationEnabled(true);
							dialogBox.center();
							dialogBox.show();
						}

					}

				});

				panel.add(submiButton);
			}

		});

		return panel;

	}

}
