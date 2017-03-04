package com.andreiolar.abms.client.widgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.andreiolar.abms.client.exception.MessagesNotFoundException;
import com.andreiolar.abms.client.exception.NoConversationsFoundException;
import com.andreiolar.abms.client.exception.UserDetailsNotFoundException;
import com.andreiolar.abms.client.rpc.DBConversationDetails;
import com.andreiolar.abms.client.rpc.DBConversationDetailsAsync;
import com.andreiolar.abms.client.rpc.DBGetConversationMessages;
import com.andreiolar.abms.client.rpc.DBGetConversationMessagesAsync;
import com.andreiolar.abms.client.rpc.DBGetUserDetails;
import com.andreiolar.abms.client.rpc.DBGetUserDetailsAsync;
import com.andreiolar.abms.client.rpc.DBReplyToConversation;
import com.andreiolar.abms.client.rpc.DBReplyToConversationAsync;
import com.andreiolar.abms.client.utils.DateUtil;
import com.andreiolar.abms.shared.ConversationDetails;
import com.andreiolar.abms.shared.ConversationMessage;
import com.andreiolar.abms.shared.ReplyMessage;
import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
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
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.html.Hr;

public class MessengerWidget extends Composite implements CustomWidget {

	private UserDetails userDetails;

	private List<ConversationDetail> conversationDetailWidgets = new ArrayList<ConversationDetail>();

	private ConversationDetails selectedConversation;

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
		MaterialPanel conversationDisplayPanel = new MaterialPanel();
		MaterialPanel conversationMessagePanel = new MaterialPanel();
		MaterialPanel conversationWithPanel = new MaterialPanel();

		conversationDetailPanel.setWidth("22%");
		conversationDetailPanel.setHeight("700px");
		conversationDetailPanel.setBackgroundColor(Color.BLUE);

		conversationPanel.setWidth("58%");
		conversationPanel.setHeight("700px");
		conversationPanel.setBackgroundColor(Color.GREY_LIGHTEN_5);
		conversationDisplayPanel.setBackgroundColor(Color.GREY_LIGHTEN_4);

		MaterialLabel noConversationSelectedLabel = new MaterialLabel();
		noConversationSelectedLabel.setText("No conversation selected.");
		noConversationSelectedLabel.setFontSize("18px");
		noConversationSelectedLabel.setPaddingTop(25.0);

		conversationDisplayPanel.setHeight("90%");
		conversationDisplayPanel.setTextAlign(TextAlign.CENTER);
		conversationDisplayPanel.add(noConversationSelectedLabel);

		conversationMessagePanel.setHeight("8%");
		MaterialTextBox textArea = new MaterialTextBox();
		textArea.setPlaceholder("Type a message...");
		textArea.setPaddingTop(10.0);
		textArea.setPaddingRight(20.0);
		textArea.setPaddingLeft(20.0);
		textArea.setEnabled(false);

		conversationMessagePanel.setBackgroundColor(Color.GREY_LIGHTEN_5);
		conversationMessagePanel.add(textArea);

		conversationWithPanel.setWidth("20%");
		conversationWithPanel.setHeight("700px");

		MaterialLabel noConversationSelectedLabel2 = new MaterialLabel();
		noConversationSelectedLabel2.setText("No conversation selected.");
		noConversationSelectedLabel2.setFontSize("18px");
		noConversationSelectedLabel2.setMarginTop(25.0);

		conversationWithPanel.setTextAlign(TextAlign.CENTER);
		conversationWithPanel.add(noConversationSelectedLabel2);

		DBConversationDetailsAsync rpc = (DBConversationDetailsAsync) GWT.create(DBConversationDetails.class);
		ServiceDefTarget tar = (ServiceDefTarget) rpc;
		String moduleURL = GWT.getModuleBaseURL() + "DBConversationDetailsImpl";
		tar.setServiceEntryPoint(moduleURL);

		rpc.getConversationDetails(userDetails, new AsyncCallback<List<ConversationDetails>>() {

			@Override
			public void onSuccess(List<ConversationDetails> result) {
				conversationDetailPanel.setTextAlign(TextAlign.LEFT);

				Collections.sort(result, new Comparator<ConversationDetails>() {

					@Override
					public int compare(ConversationDetails o1, ConversationDetails o2) {
						Date conversationDate1 = DateUtil.getConversationDate(o1.getLastMessageDate());
						Date conversationDate2 = DateUtil.getConversationDate(o2.getLastMessageDate());

						return -Long.compare(conversationDate1.getTime(), conversationDate2.getTime());
					}
				});

				for (ConversationDetails conversationDetails : result) {
					ConversationDetail conversationDetail = new ConversationDetail(conversationDetails, false);
					conversationDetailWidgets.add(conversationDetail);
					conversationDetail.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							conversationDetailWidgets.stream().forEach(cdw -> cdw.setSelected(false));
							conversationDetail.setSelected(true);
							selectedConversation = conversationDetails;

							conversationDisplayPanel.clear();
							conversationDisplayPanel.setTextAlign(TextAlign.DEFAULT);
							textArea.setEnabled(true);

							DBGetConversationMessagesAsync conversationMessageRpc = (DBGetConversationMessagesAsync) GWT
									.create(DBGetConversationMessages.class);
							ServiceDefTarget conversationMessageTar = (ServiceDefTarget) conversationMessageRpc;
							String moduleURL = GWT.getModuleBaseURL() + "DBGetConversationMessagesImpl";
							conversationMessageTar.setServiceEntryPoint(moduleURL);

							conversationMessageRpc.getConversationMessages(String.valueOf(conversationDetails.getId()),
									new AsyncCallback<List<ConversationMessage>>() {

										@Override
										public void onFailure(Throwable caught) {
											if (caught instanceof MessagesNotFoundException) {
												MaterialLabel errorLabel = new MaterialLabel();
												errorLabel.setText("No messages to display. Send a message instead.");
												errorLabel.setFontSize("18px");
												errorLabel.setPaddingTop(25.0);

												conversationDisplayPanel.setTextAlign(TextAlign.CENTER);
												conversationDisplayPanel.add(errorLabel);
											} else {
												MaterialModal errorModal = ModalCreator.createModal(caught);
												RootPanel.get().add(errorModal);
												errorModal.open();
											}
										}

										@Override
										public void onSuccess(List<ConversationMessage> result) {
											for (ConversationMessage message : result) {
												Message messageWidget = new Message(message, userDetails.getUsername());
												conversationDisplayPanel.add(messageWidget);
											}
										}
									});

							conversationWithPanel.clear();
							conversationWithPanel.setTextAlign(TextAlign.DEFAULT);

							DBGetUserDetailsAsync userDetailsRpc = (DBGetUserDetailsAsync) GWT.create(DBGetUserDetails.class);
							ServiceDefTarget userDetailsTar = (ServiceDefTarget) userDetailsRpc;
							String userDeatilsURL = GWT.getModuleBaseURL() + "DBGetUserDetailsImpl";
							userDetailsTar.setServiceEntryPoint(userDeatilsURL);

							userDetailsRpc.geUserDetails(conversationDetails.getConversationWith(), new AsyncCallback<UserDetails>() {

								@Override
								public void onFailure(Throwable caught) {
									if (caught instanceof UserDetailsNotFoundException) {
										MaterialLabel errorLabel = new MaterialLabel();
										errorLabel.setText(caught.getMessage());
										errorLabel.setFontSize("18px");
										errorLabel.setMarginTop(25.0);

										conversationWithPanel.setTextAlign(TextAlign.CENTER);
										conversationWithPanel.add(errorLabel);
									} else {
										MaterialModal errorModal = ModalCreator.createModal(caught);
										RootPanel.get().add(errorModal);
										errorModal.open();
									}
								}

								@Override
								public void onSuccess(UserDetails result) {
									ConversationWithWidget conversationWithWidget = new ConversationWithWidget(result);
									conversationWithPanel.add(conversationWithWidget);
								}
							});
						}
					});

					conversationDetailPanel.add(conversationDetail);
				}

				textArea.addKeyDownHandler(new KeyDownHandler() {

					@Override
					public void onKeyDown(KeyDownEvent event) {
						if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
							String messageText = textArea.getText();

							ReplyMessage replyMessage = new ReplyMessage(messageText, userDetails.getUsername(), selectedConversation.getId());

							DBReplyToConversationAsync replyToConvRpc = (DBReplyToConversationAsync) GWT.create(DBReplyToConversation.class);
							ServiceDefTarget replyToConvTar = (ServiceDefTarget) replyToConvRpc;
							String replyToConvUrl = GWT.getModuleBaseURL() + "DBReplyToConversationImpl";
							replyToConvTar.setServiceEntryPoint(replyToConvUrl);

							replyToConvRpc.replyToConversation(replyMessage, new AsyncCallback<Void>() {

								@Override
								public void onSuccess(Void result) {
									textArea.clear();
								}

								@Override
								public void onFailure(Throwable caught) {
									textArea.clear();
									MaterialModal errorModal = ModalCreator.createModal(caught);
									RootPanel.get().add(errorModal);
									errorModal.open();
								}
							});
						}
					}
				});

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

		conversationPanel.add(conversationDisplayPanel);
		conversationPanel.add(conversationMessagePanel);

		mainPanel.add(conversationDetailPanel);
		mainPanel.add(conversationPanel);
		mainPanel.add(conversationWithPanel);

		panel.add(mainPanel);

		return panel;
	}
}
