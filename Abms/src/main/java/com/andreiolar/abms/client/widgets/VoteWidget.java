package com.andreiolar.abms.client.widgets;

import com.andreiolar.abms.client.exception.VoteOptionsNotFoundException;
import com.andreiolar.abms.client.exception.VoteSessionNotActiveException;
import com.andreiolar.abms.client.exception.VoteSubmissionException;
import com.andreiolar.abms.client.rpc.DBGetVotingSession;
import com.andreiolar.abms.client.rpc.DBGetVotingSessionAsync;
import com.andreiolar.abms.client.rpc.DBSubmitVote;
import com.andreiolar.abms.client.rpc.DBSubmitVoteAsync;
import com.andreiolar.abms.shared.UserDetails;
import com.andreiolar.abms.shared.Vote;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.ButtonType;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.ModalType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLoader;
import gwt.material.design.client.ui.MaterialModal;
import gwt.material.design.client.ui.MaterialModalContent;
import gwt.material.design.client.ui.MaterialModalFooter;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTitle;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.client.ui.html.Div;
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

		MaterialLabel label = new MaterialLabel();
		label.setText("Active voting session will be displayed below where you can vote on different topics asked by the Administration.");
		label.setMarginTop(25.0);
		label.setMarginLeft(25.0);
		label.setFontSize("18px");
		panel.add(label);

		DBGetVotingSessionAsync rpc = (DBGetVotingSessionAsync) GWT.create(DBGetVotingSession.class);
		ServiceDefTarget tar = (ServiceDefTarget) rpc;
		String moduleURL = GWT.getModuleBaseURL() + "DBGetVotingSessionImpl";
		tar.setServiceEntryPoint(moduleURL);

		MaterialLoader.showLoading(true);

		rpc.getVotingSession(userDetails, new AsyncCallback<Vote>() {

			@Override
			public void onSuccess(Vote result) {
				MaterialLoader.showLoading(false);

				if (result.getVoteId() == null) {
					MaterialPanel voteDescriptionPanel = new MaterialPanel();
					voteDescriptionPanel.setShadow(2);
					voteDescriptionPanel.setStyleName("vote-description-panel");

					MaterialLabel text = new MaterialLabel();
					text.getElement().setInnerHTML(
							"You have already voted for this voting session.<br/><br/>Your voting option: <span style=\"color: #2196f3;\">"
									+ result.getTitle() + "</span>");
					text.setFontSize("24px");
					text.setMarginTop(25.0);
					text.setMarginLeft(25.0);
					text.setPaddingBottom(25.0);

					voteDescriptionPanel.add(text);
					panel.add(voteDescriptionPanel);
				} else {

					MaterialPanel voteDescriptionPanel = new MaterialPanel();
					voteDescriptionPanel.setShadow(2);
					voteDescriptionPanel.setStyleName("vote-description-panel");

					MaterialLabel title = new MaterialLabel();
					title.setText(result.getTitle());
					title.setMarginTop(25.0);
					title.setMarginLeft(25.0);
					title.setTextColor(Color.BLUE);
					title.setFontSize("18px");
					voteDescriptionPanel.add(title);

					MaterialLabel description = new MaterialLabel();
					description.setText(result.getDescription());
					description.setMarginTop(50.0);
					description.setMarginLeft(25.0);
					description.setPaddingBottom(25.0);
					voteDescriptionPanel.add(description);

					Div buttonsDiv = new Div();
					buttonsDiv.setStyleName("vote-descriptions-buttons");
					for (int i = 0; i < result.getVoteOptions().size(); i++) {
						String voteOption = result.getVoteOptions().get(i);

						MaterialButton voteButton = new MaterialButton();
						voteButton.setWaves(WavesType.LIGHT);

						if ((i + 1) % 2 != 0) {
							if (result.getVoteOptions().indexOf(voteOption) == (result.getVoteOptions().size() - 1)) {
								voteButton.setWidth("100%");
							} else {
								voteButton.setWidth("48%");
								voteButton.addStyleName("margin-right-percent");
							}
						} else {
							voteButton.setWidth("48%");
						}

						voteButton.setHeight("50px");
						voteButton.setText(voteOption);
						voteButton.setBackgroundColor(Color.WHITE);
						voteButton.setTextColor(Color.BLUE);
						voteButton.setMarginBottom(25.0);
						voteButton.addStyleName("blue-border");
						voteButton.addClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {
								MaterialModal materialModal = new MaterialModal();
								materialModal.setType(ModalType.DEFAULT);
								materialModal.setDismissible(false);
								materialModal.setInDuration(500);
								materialModal.setOutDuration(500);

								MaterialModalContent materialModalContent = new MaterialModalContent();
								MaterialTitle materialTitle = new MaterialTitle("Your selected option");
								materialTitle.setTextColor(Color.BLUE);
								materialTitle.setTextAlign(TextAlign.CENTER);

								materialModalContent.add(materialTitle);

								MaterialLabel label = new MaterialLabel();
								label.getElement().setInnerHTML(
										"Selected option: <span style=\"color: #2196f3;\">" + voteOption + "</span><br/><br/>Are you sure?");
								label.setFontSize("18px");
								materialModalContent.add(label);

								MaterialModalFooter materialModalFooter = new MaterialModalFooter();
								MaterialButton submitButton = new MaterialButton();
								submitButton.setText("Yes");
								submitButton.addClickHandler(h -> {
									DBSubmitVoteAsync submitVote = (DBSubmitVoteAsync) GWT.create(DBSubmitVote.class);
									ServiceDefTarget submitVoteTar = (ServiceDefTarget) submitVote;
									String moduleURL = GWT.getModuleBaseURL() + "DBSubmitVoteImpl";
									submitVoteTar.setServiceEntryPoint(moduleURL);

									MaterialLoader.showLoading(true);

									materialModal.close();
									RootPanel.get().remove(materialModal);

									submitVote.submitVoteToDB(result.getVoteId(), voteOption, result.getTitle(), result.getDescription(), userDetails,
											new AsyncCallback<Void>() {

												@Override
												public void onFailure(Throwable caught) {
													MaterialLoader.showLoading(false);
													if (caught instanceof VoteSubmissionException) {
														MaterialToast.fireToast(caught.getMessage(), "rounded");
													} else {
														MaterialModal materialModal = ModalCreator.createErrorModal("Something went wrong", caught);
														RootPanel.get().add(materialModal);
														materialModal.open();
													}
												}

												@Override
												public void onSuccess(Void result) {
													MaterialLoader.showLoading(false);
													panel.clear();
													panel.add(new VoteWidget(userDetails));
													MaterialToast.fireToast("Vote submitted successfully.", "rounded");
												}
											});
								});

								materialModalFooter.add(submitButton);

								MaterialButton closeButton = new MaterialButton();
								closeButton.setText("No");
								closeButton.setType(ButtonType.FLAT);
								closeButton.addClickHandler(h -> {
									materialModal.close();
									RootPanel.get().remove(materialModal);
								});

								materialModalFooter.add(closeButton);
								materialModal.add(materialModalContent);
								materialModal.add(materialModalFooter);

								RootPanel.get().add(materialModal);
								materialModal.open();
							}
						});

						voteButton.addMouseOverHandler(new MouseOverHandler() {

							@Override
							public void onMouseOver(MouseOverEvent event) {
								voteButton.setBackgroundColor(Color.BLUE);
								voteButton.setTextColor(Color.WHITE);
							}
						});

						voteButton.addMouseOutHandler(new MouseOutHandler() {

							@Override
							public void onMouseOut(MouseOutEvent event) {
								voteButton.setBackgroundColor(Color.WHITE);
								voteButton.setTextColor(Color.BLUE);
							}
						});

						buttonsDiv.add(voteButton);
					}

					panel.add(voteDescriptionPanel);
					panel.add(buttonsDiv);
				}

			}

			@Override
			public void onFailure(Throwable caught) {
				MaterialLoader.showLoading(false);
				if (caught instanceof VoteSessionNotActiveException) {
					MaterialPanel voteDescriptionPanel = new MaterialPanel();
					voteDescriptionPanel.setShadow(2);
					voteDescriptionPanel.setStyleName("vote-description-panel");

					MaterialLabel text = new MaterialLabel();
					text.setText(
							"Sorry, no voting session is available at the moment. Please come back as soon as your Administrator starts a voting session. If you would like to see results for the previous voting sessions plese visit the 'View Vote Results' page.");
					text.setFontSize("24px");
					text.setMarginTop(25.0);
					text.setMarginLeft(25.0);
					text.setPaddingBottom(25.0);

					voteDescriptionPanel.add(text);
					panel.add(voteDescriptionPanel);
				} else if (caught instanceof VoteOptionsNotFoundException) {
					MaterialToast.fireToast("Voting session is available, but voting options could not be loaded. Please trey again.", "rounded");
				} else {
					MaterialModal materialModal = ModalCreator.createErrorModal("Something went wrong", caught);
					RootPanel.get().add(materialModal);
					materialModal.open();
				}
			}
		});

		return panel;
	}
}
