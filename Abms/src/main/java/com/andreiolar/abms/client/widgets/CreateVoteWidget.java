package com.andreiolar.abms.client.widgets;

import java.util.HashSet;
import java.util.Set;

import com.andreiolar.abms.client.exception.NoActiveVoteException;
import com.andreiolar.abms.client.rpc.CreateVotingSession;
import com.andreiolar.abms.client.rpc.CreateVotingSessionAsync;
import com.andreiolar.abms.client.rpc.DBActiveVoteSession;
import com.andreiolar.abms.client.rpc.DBActiveVoteSessionAsync;
import com.andreiolar.abms.client.rpc.DBDisableVotingSession;
import com.andreiolar.abms.client.rpc.DBDisableVotingSessionAsync;
import com.andreiolar.abms.shared.VoteSession;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.IconSize;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialLoader;
import gwt.material.design.client.ui.MaterialModal;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextArea;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Hr;

public class CreateVoteWidget extends Composite implements CustomWidget {

	private Set<String> votingOptions;
	private boolean buttonAdded = false;

	public CreateVoteWidget() {
		votingOptions = new HashSet<String>();

		initWidget(initializeWidget());
	}

	@Override
	public Widget initializeWidget() {
		MaterialPanel panel = new MaterialPanel();

		MaterialLabel title = new MaterialLabel("Create/Disable Voting Session");
		title.setTextColor(Color.BLUE);
		title.setTextAlign(TextAlign.CENTER);
		title.setFontSize("36px");
		title.setFontWeight(FontWeight.BOLD);
		panel.add(title);

		panel.add(new Hr());

		MaterialLabel descriptionLabel = new MaterialLabel();
		descriptionLabel.getElement().setInnerHTML(
				"New Voting Session can be created using the form below. Existing Voting Session needs to be deactivated before creating a new one.");
		descriptionLabel.setFontSize("18px");
		descriptionLabel.setMarginTop(25.0);
		descriptionLabel.setMarginLeft(25.0);

		panel.add(descriptionLabel);

		MaterialLoader.showLoading(true);
		DBActiveVoteSessionAsync activeVoteSessionRpc = (DBActiveVoteSessionAsync) GWT.create(DBActiveVoteSession.class);
		ServiceDefTarget activeVoteSessionTarget = (ServiceDefTarget) activeVoteSessionRpc;
		String activeVoteSessionURL = GWT.getModuleBaseURL() + "DBActiveVoteSessionImpl";
		activeVoteSessionTarget.setServiceEntryPoint(activeVoteSessionURL);

		activeVoteSessionRpc.getActiveVoteSession(new AsyncCallback<VoteSession>() {

			@Override
			public void onSuccess(VoteSession result) {
				MaterialLoader.showLoading(false);

				MaterialPanel messagePanel = new MaterialPanel();
				messagePanel.setShadow(2);
				messagePanel.addStyleName("voting-message-panel");

				MaterialLabel label = new MaterialLabel();
				label.setFontSize("16px");
				label.setPaddingTop(10.0);
				label.setPaddingLeft(10.0);
				label.getElement().setInnerHTML(
						"There is already one active Voting Session. In order to create a new one please disable the current one by using the button below. <br/><br/>Active Voting Session:</br> <span style=\"color: #1777cb;\">"
								+ result.getVoteId() + ". " + result.getTitle() + "</span>");
				messagePanel.add(label);

				Div buttonDiv = new Div();
				buttonDiv.setStyleName("voting-message-panel-center-button");
				MaterialButton deactivateButton = new MaterialButton();
				deactivateButton.setWaves(WavesType.LIGHT);
				deactivateButton.setWidth("100%");
				deactivateButton.setHeight("45px");
				deactivateButton.setText("DISABLE ACTIVE VOTING SESSION");
				deactivateButton.setTextColor(Color.WHITE);
				deactivateButton.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						MaterialLoader.showLoading(true);
						DBDisableVotingSessionAsync deactivateVoteSessionRpc = (DBDisableVotingSessionAsync) GWT.create(DBDisableVotingSession.class);
						ServiceDefTarget deactivateVoteSessionTarget = (ServiceDefTarget) deactivateVoteSessionRpc;
						String deactivateVoteSessionURL = GWT.getModuleBaseURL() + "DBDisableVotingSessionImpl";
						deactivateVoteSessionTarget.setServiceEntryPoint(deactivateVoteSessionURL);

						deactivateVoteSessionRpc.disableActiveVotingSession(new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								MaterialLoader.showLoading(false);

								MaterialModal materialModal = ModalCreator.createErrorModal("Something went wrong", caught);
								RootPanel.get().add(materialModal);
								materialModal.open();
							}

							@Override
							public void onSuccess(Void result) {
								MaterialLoader.showLoading(false);

								panel.clear();
								panel.add(new CreateVoteWidget());
							}
						});
					}
				});

				panel.add(messagePanel);

				buttonDiv.add(deactivateButton);

				panel.add(buttonDiv);
			}

			@Override
			public void onFailure(Throwable caught) {
				MaterialLoader.showLoading(false);

				if (caught instanceof NoActiveVoteException) {
					MaterialTextBox titleBox = new MaterialTextBox();
					titleBox.setWidth("30%");
					titleBox.setMarginTop(25.0);
					titleBox.setMarginLeft(25.0);
					titleBox.setMaxLength(50);
					titleBox.setType(InputType.TEXT);
					titleBox.setPlaceholder("Title");

					panel.add(titleBox);

					MaterialTextArea descriptionBox = new MaterialTextArea();
					descriptionBox.setMarginLeft(25.0);
					descriptionBox.setMarginTop(25.0);
					descriptionBox.setWidth("30%");
					descriptionBox.setPlaceholder("Description");
					descriptionBox.setLength(500);

					panel.add(descriptionBox);

					Div insertDiv = new Div();
					insertDiv.setWidth("100%");
					insertDiv.setMarginLeft(25.0);
					insertDiv.setMarginTop(25.0);
					insertDiv.setDisplay(Display.NONE);

					MaterialTextBox addOptionBox = new MaterialTextBox();
					addOptionBox.setWidth("15%");
					addOptionBox.setType(InputType.TEXT);
					addOptionBox.setPlaceholder("Option");
					insertDiv.add(addOptionBox);

					MaterialLink addOptionButton = new MaterialLink();
					MaterialButton createButton = new MaterialButton();
					Div buttonsDiv = new Div();

					MaterialLink addButton = new MaterialLink();
					addButton.setText("");
					addButton.setFontWeight(FontWeight.BOLD);
					addButton.setTextColor(Color.BLUE);
					addButton.setMarginLeft(30.0);
					addButton.setIconType(IconType.ADD);
					addButton.setIconSize(IconSize.MEDIUM);
					addButton.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							addOptionBox.clearErrorOrSuccess();

							String option = addOptionBox.getText();

							if (option != null && !option.trim().equals("")) {
								if (votingOptions.add(option)) {
									addOptionBox.setText("");
									addOptionBox.clearErrorOrSuccess();

									if (votingOptions.size() > 1 && !buttonAdded) {
										createButton.setDisplay(Display.BLOCK);

										createButton.setText("CREATE VOTING SESSION");
										createButton.setMarginTop(50);
										createButton.setHeight("50px");
										createButton.setWidth("15%");
										createButton.setMarginBottom(75);
										createButton.setMarginLeft(25);
										createButton.addClickHandler(new ClickHandler() {

											@Override
											public void onClick(ClickEvent event) {
												String title = titleBox.getText();
												String description = descriptionBox.getText();

												boolean canProceed = true;

												if (title == null || title.trim().equals("")) {
													canProceed = false;
													titleBox.setError("Title is required to create a voting session.");
												} else {
													titleBox.clearErrorOrSuccess();
												}

												if (description == null || description.trim().equals("")) {
													canProceed = false;
													descriptionBox.setError("Description is required to create a voting session.");
												} else {
													descriptionBox.clearErrorOrSuccess();
												}

												if (description.length() > 500) {
													canProceed = false;
													descriptionBox.setError("Description cannot have more than 500 characters.");
												} else {
													if (description != null && !description.trim().equals("")) {
														descriptionBox.clearErrorOrSuccess();
													}
												}

												if (canProceed) {
													MaterialLoader.showLoading(true);
													CreateVotingSessionAsync createVoteSessionRpc = (CreateVotingSessionAsync) GWT
															.create(CreateVotingSession.class);
													ServiceDefTarget createVoteSessionTarget = (ServiceDefTarget) createVoteSessionRpc;
													String createVoteSessionURL = GWT.getModuleBaseURL() + "CreateVotingSessionImpl";
													createVoteSessionTarget.setServiceEntryPoint(createVoteSessionURL);

													VoteSession voteSession = new VoteSession();
													voteSession.setTitle(title);
													voteSession.setDescription(description);

													createVoteSessionRpc.createVotingSession(voteSession, votingOptions, new AsyncCallback<Void>() {

														@Override
														public void onFailure(Throwable caught) {
															MaterialLoader.showLoading(false);

															MaterialModal materialModal = ModalCreator.createErrorModal("Something went wrong",
																	caught);
															RootPanel.get().add(materialModal);
															materialModal.open();
														}

														@Override
														public void onSuccess(Void result) {
															MaterialLoader.showLoading(false);

															MaterialToast.fireToast("Voting Session created successfully.", "rounded");
															panel.clear();
															panel.add(new CreateVoteWidget());
														}
													});
												}
											}
										});
										panel.add(createButton);
										buttonAdded = true;
									}

									// Create and add display button
									MaterialButton optionButton = new MaterialButton();
									optionButton.setWaves(WavesType.LIGHT);
									optionButton.setHeight("50px");
									optionButton.setText(option);
									optionButton.setBackgroundColor(Color.WHITE);
									optionButton.setTextColor(Color.BLUE);
									optionButton.setMarginTop(30.0);
									optionButton.setMarginLeft(25.0);
									optionButton.addStyleName("blue-border");
									buttonsDiv.add(optionButton);

									insertDiv.setDisplay(Display.NONE);
									addOptionButton.setDisplay(Display.BLOCK);
								} else {
									addOptionBox.setError("Option already exists.");
								}
							} else {
								addOptionBox.setError("Option is mandatory.");
							}
						}
					});
					insertDiv.add(addButton);

					Div flexDiv = new Div();
					flexDiv.setWidth("100%");
					flexDiv.setMarginLeft(25.0);
					flexDiv.setMarginTop(40.0);
					flexDiv.setDisplay(Display.FLEX);

					MaterialLabel label = new MaterialLabel();
					label.setText("Voting Options: ");
					label.setFontSize("16px");

					addOptionButton.setText("ADD OPTION");
					addOptionButton.setFontSize("16px");
					addOptionButton.setFontWeight(FontWeight.BOLD);
					addOptionButton.setTextColor(Color.BLUE);
					addOptionButton.setMarginLeft(50.0);
					addOptionButton.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							addOptionButton.setDisplay(Display.NONE);
							insertDiv.setDisplay(Display.FLEX);
						}
					});

					flexDiv.add(label);
					flexDiv.add(addOptionButton);

					panel.add(flexDiv);
					panel.add(insertDiv);
					panel.add(buttonsDiv);
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
