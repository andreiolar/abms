package com.andreiolar.abms.client.widgets;

import java.util.List;

import com.andreiolar.abms.client.constants.ConversationWidgetConstants;
import com.andreiolar.abms.client.constants.DialogBoxConstants;
import com.andreiolar.abms.client.rpc.DBDeleteConversation;
import com.andreiolar.abms.client.rpc.DBDeleteConversationAsync;
import com.andreiolar.abms.client.rpc.DBGetAllConversations;
import com.andreiolar.abms.client.rpc.DBGetAllConversationsAsync;
import com.andreiolar.abms.client.rpc.DBGetAllSuggestions;
import com.andreiolar.abms.client.rpc.DBGetAllSuggestionsAsync;
import com.andreiolar.abms.client.rpc.DBGetMessagesForConversation;
import com.andreiolar.abms.client.rpc.DBGetMessagesForConversationAsync;
import com.andreiolar.abms.client.rpc.DBMoveConversation;
import com.andreiolar.abms.client.rpc.DBMoveConversationAsync;
import com.andreiolar.abms.client.rpc.DBReplyToConversation;
import com.andreiolar.abms.client.rpc.DBReplyToConversationAsync;
import com.andreiolar.abms.client.utils.DialogBoxCreator;
import com.andreiolar.abms.shared.Conversation;
import com.andreiolar.abms.shared.Message;
import com.andreiolar.abms.shared.ReplyMessage;
import com.andreiolar.abms.shared.UserInfo;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ConversationWidget extends Composite implements CustomWidget {

	private UserInfo userInfo;

	private boolean isConversationView;
	private boolean isMessageView;

	private int lastId;

	public ConversationWidget(UserInfo userInfo) {
		this.userInfo = userInfo;

		initWidget(initializeWidget());
	}

	@Override
	public Widget initializeWidget() {
		final VerticalPanel panel = new VerticalPanel();
		final SimplePanel convPanel = new SimplePanel();

		// Top layer
		HorizontalPanel topLayer = new HorizontalPanel();

		PushButton backButton = new PushButton(new Image("images/icons/back_button.png"));
		backButton.getElement().getStyle().setMarginTop(10.0, Unit.PX);
		backButton.getElement().getStyle().setMarginRight(30.0, Unit.PX);
		backButton.addStyleName("no-border-button");
		backButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (isMessageView) {
					panel.clear();
					Widget renewedWidget = initializeWidget();
					panel.add(renewedWidget);
				}
			}
		});

		PushButton refreshButton = new PushButton(new Image("images/icons/refresh_button.png"));
		refreshButton.getElement().getStyle().setMarginTop(6.0, Unit.PX);
		refreshButton.getElement().getStyle().setMarginRight(100.0, Unit.PX);
		refreshButton.addStyleName("no-border-button");
		refreshButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (isConversationView) {
					panel.clear();
					Widget renewedWidget = initializeWidget();
					panel.add(renewedWidget);
				} else {
					viewConversation(lastId, convPanel);
				}
			}
		});

		Label filterLabel = new Label();
		filterLabel.setText("Filter");
		filterLabel.getElement().getStyle().setFontSize(18.0, Unit.PX);
		filterLabel.getElement().getStyle().setMarginTop(15.0, Unit.PX);
		filterLabel.getElement().getStyle().setMarginRight(7.0, Unit.PX);

		final ListBox filter = new ListBox();
		filter.addItem("Inbox");
		filter.addItem("Important");
		filter.addItem("Archive");
		filter.getElement().getStyle().setMarginTop(17.0, Unit.PX);
		filter.getElement().getStyle().setMarginRight(1220.0, Unit.PX);

		filter.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (isConversationView) {
					String conversationFilter = filter.getSelectedItemText();
					convPanel.clear();
					fillConversationPanel(convPanel, conversationFilter);
				}
			}
		});

		Label searchLabel = new Label();
		searchLabel.setText("Search");
		searchLabel.getElement().getStyle().setFontSize(18.0, Unit.PX);
		searchLabel.getElement().getStyle().setMarginTop(15.0, Unit.PX);
		searchLabel.getElement().getStyle().setMarginRight(10.0, Unit.PX);

		MultiWordSuggestOracle oracle = getDBSuggestions();

		final CustomTexBox searchBox = new CustomTexBox();
		searchBox.getElement().getStyle().setMarginTop(15.0, Unit.PX);
		searchBox.getElement().getStyle().setMarginRight(10.0, Unit.PX);

		SuggestBox suggestBox = new SuggestBox(oracle, searchBox);

		suggestBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {

			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				String returnedString = event.getSelectedItem().getReplacementString();
				String id = returnedString.split("\\|")[0].trim();
				int convId = Integer.parseInt(id);

				viewConversation(convId, convPanel);
			}
		});

		topLayer.add(backButton);
		topLayer.add(refreshButton);
		topLayer.add(filterLabel);
		topLayer.add(filter);
		topLayer.add(searchLabel);
		topLayer.add(suggestBox);

		HTML separatorLine = new HTML("<hr class=\"width:100%;\" />");

		panel.add(topLayer);
		panel.add(separatorLine);

		// Start conversation panel
		String conversationFilter = filter.getSelectedItemText();
		fillConversationPanel(convPanel, conversationFilter);

		panel.add(convPanel);

		return panel;
	}

	private MultiWordSuggestOracle getDBSuggestions() {
		final MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();

		DBGetAllSuggestionsAsync rpc = (DBGetAllSuggestionsAsync) GWT.create(DBGetAllSuggestions.class);
		ServiceDefTarget tar = (ServiceDefTarget) rpc;
		String moduleURL = GWT.getModuleBaseURL() + "DBGetAllSuggestionsImpl";
		tar.setServiceEntryPoint(moduleURL);

		rpc.getAllSuggestions(userInfo, new AsyncCallback<List<String>>() {

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

	private void fillConversationPanel(final SimplePanel convPanel, final String conversationFilter) {
		final FlexTable flexTable = new FlexTable();
		flexTable.addStyleName("flexTable");
		CellFormatter cellFormatter = flexTable.getCellFormatter();

		flexTable.setHTML(0, 0, ConversationWidgetConstants.ID);
		flexTable.setHTML(0, 1, ConversationWidgetConstants.DATE);
		flexTable.setHTML(0, 2, ConversationWidgetConstants.CONVERSATION_WITH);
		flexTable.setHTML(0, 3, ConversationWidgetConstants.SUBJECT);
		flexTable.setHTML(0, 4, ConversationWidgetConstants.UNREAD_MESSAGES);
		flexTable.setHTML(0, 5, ConversationWidgetConstants.STATUS);

		DOM.getElementById("loading").getStyle().setDisplay(Display.BLOCK);
		DBGetAllConversationsAsync rpc = (DBGetAllConversationsAsync) GWT.create(DBGetAllConversations.class);
		ServiceDefTarget tar = (ServiceDefTarget) rpc;
		String moduleURL = GWT.getModuleBaseURL() + "DBGetAllConversationsImpl";
		tar.setServiceEntryPoint(moduleURL);

		rpc.getAllMessages(userInfo, conversationFilter, new AsyncCallback<List<Conversation>>() {

			@Override
			public void onSuccess(List<Conversation> result) {
				DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
				isConversationView = true;
				isMessageView = false;

				int start = 1;

				for (Conversation conversation : result) {
					final int id = conversation.getId();

					Anchor idAnchor = new Anchor(String.valueOf(id));
					idAnchor.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							viewConversation(id, convPanel);
						}

					});

					final PopupPanel contextMenu = new PopupPanel(true);
					Widget panel = populateContextMenu(id, convPanel, conversationFilter);

					contextMenu.add(panel);
					contextMenu.hide();

					idAnchor.sinkEvents(Event.ONCONTEXTMENU);
					idAnchor.addHandler(new ContextMenuHandler() {

						@Override
						public void onContextMenu(ContextMenuEvent event) {
							event.preventDefault();
							event.stopPropagation();

							int x = event.getNativeEvent().getClientX();
							int y = event.getNativeEvent().getClientY();

							contextMenu.setPopupPosition(x, y);
							contextMenu.show();
						}
					}, ContextMenuEvent.getType());

					flexTable.setWidget(start, 0, idAnchor);
					flexTable.setHTML(start, 1, conversation.getDate());

					String username = userInfo.getUsername();
					String source = conversation.getSource();
					String destination = conversation.getDestination();

					flexTable.setHTML(start, 2, source.equals(username) ? destination : source);

					Anchor subjectAnchor = new Anchor(String.valueOf(""));
					subjectAnchor.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							viewConversation(id, convPanel);
						}
					});

					subjectAnchor.sinkEvents(Event.ONCONTEXTMENU);
					subjectAnchor.addHandler(new ContextMenuHandler() {

						@Override
						public void onContextMenu(ContextMenuEvent event) {
							event.preventDefault();
							event.stopPropagation();

							int x = event.getNativeEvent().getClientX();
							int y = event.getNativeEvent().getClientY();

							contextMenu.setPopupPosition(x, y);
							contextMenu.show();
						}
					}, ContextMenuEvent.getType());

					flexTable.setWidget(start, 3, subjectAnchor);
					flexTable.setHTML(start, 4, "0");
					flexTable.setHTML(start, 5, "Unread");

					start++;
				}

			}

			@Override
			public void onFailure(Throwable caught) {
				DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
			}
		});

		cellFormatter.addStyleName(0, 0, "firstHeaderCell");
		cellFormatter.addStyleName(0, 5, "lastHeaderCell");
		cellFormatter.setWidth(0, 1, "200px");
		cellFormatter.setWidth(0, 2, "100px");
		cellFormatter.setWidth(0, 4, "150px");
		cellFormatter.setWidth(0, 5, "150px");
		flexTable.getRowFormatter().addStyleName(0, "headerRow");

		convPanel.add(flexTable);
	}

	protected Widget populateContextMenu(final int id, final SimplePanel convPanel, final String conversationFilter) {
		MenuBar menuBar = new MenuBar(true);

		MenuItem importantItem = new MenuItem(ConversationWidgetConstants.FILTER_IMPORTANT, new Command() {

			@Override
			public void execute() {
				moveConversationTo(id, ConversationWidgetConstants.FILTER_IMPORTANT, convPanel, conversationFilter);
			}
		});
		menuBar.addItem(importantItem);

		MenuItem archiveItem = new MenuItem(ConversationWidgetConstants.FILTER_ARCHIVE, new Command() {

			@Override
			public void execute() {
				moveConversationTo(id, ConversationWidgetConstants.FILTER_ARCHIVE, convPanel, conversationFilter);
			}
		});
		menuBar.addItem(archiveItem);

		MenuItem inboxItem = new MenuItem(ConversationWidgetConstants.FILTER_INBOX, new Command() {

			@Override
			public void execute() {
				moveConversationTo(id, ConversationWidgetConstants.FILTER_INBOX, convPanel, conversationFilter);
			}
		});
		menuBar.addItem(inboxItem);

		MenuItem moveToItem = new MenuItem("Move To", menuBar);

		MenuItem deleteItem = new MenuItem("Delete", new Command() {

			@Override
			public void execute() {
				deleteConversation(id, convPanel, conversationFilter);
			}
		});

		MenuBar menu = new MenuBar(true);
		menu.addItem(moveToItem);
		menu.addItem(deleteItem);
		menu.setAutoOpen(true);
		menu.setWidth("100%");
		menu.setAnimationEnabled(true);
		menu.addSeparator();

		menu.setStyleName("move-conversation-context-menu");
		moveToItem.addStyleName("move-conversation-context-menu-item-moveto");
		deleteItem.addStyleName("move-conversation-context-menu-item-moveto");
		archiveItem.addStyleName("move-conversation-context-menu-item-moveto");
		importantItem.addStyleName("move-conversation-context-menu-item-moveto");

		return menu;
	}

	protected void deleteConversation(int convId, final SimplePanel convPanel, final String conversationFilter) {
		DOM.getElementById("loading").getStyle().setDisplay(Display.BLOCK);
		DBDeleteConversationAsync rpc = (DBDeleteConversationAsync) GWT.create(DBDeleteConversation.class);
		ServiceDefTarget tar = (ServiceDefTarget) rpc;
		String moduleURL = GWT.getModuleBaseURL() + "DBDeleteConversationImpl";
		tar.setServiceEntryPoint(moduleURL);

		rpc.deleteConversation(convId, userInfo, new AsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
				convPanel.clear();
				fillConversationPanel(convPanel, conversationFilter);
			}

			@Override
			public void onFailure(Throwable caught) {
				DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
				DialogBox dialogBox = DialogBoxCreator.createDialogBox(ConversationWidgetConstants.DIALOG_BOX_FAILED_DELETING_CONVERSATION_TITLE,
						caught.getMessage(), DialogBoxConstants.CLOSE_BUTTON, false, false);
				dialogBox.setGlassEnabled(true);
				dialogBox.setAnimationEnabled(true);
				dialogBox.center();
				dialogBox.show();
			}
		});

	}

	private void moveConversationTo(int id, String filter, final SimplePanel convPanel, final String conversationFilter) {
		DOM.getElementById("loading").getStyle().setDisplay(Display.BLOCK);
		DBMoveConversationAsync rpc = (DBMoveConversationAsync) GWT.create(DBMoveConversation.class);
		ServiceDefTarget tar = (ServiceDefTarget) rpc;
		String moduleURL = GWT.getModuleBaseURL() + "DBMoveConversationImpl";
		tar.setServiceEntryPoint(moduleURL);

		rpc.moveConversation(id, filter, userInfo, new AsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
				convPanel.clear();
				fillConversationPanel(convPanel, conversationFilter);
			}

			@Override
			public void onFailure(Throwable caught) {
				DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
				DialogBox dialogBox = DialogBoxCreator.createDialogBox(ConversationWidgetConstants.DIALOG_BOX_FAILED_MOVING_CONVERSATION_TITLE,
						caught.getMessage(), DialogBoxConstants.CLOSE_BUTTON, false, false);
				dialogBox.setGlassEnabled(true);
				dialogBox.setAnimationEnabled(true);
				dialogBox.center();
				dialogBox.show();
			}
		});
	}

	private void viewConversation(int id, final SimplePanel convPanel) {
		convPanel.clear();

		lastId = id;

		VerticalPanel vPanel = new VerticalPanel();

		final FlexTable flexTable = new FlexTable();
		flexTable.addStyleName("flexTable");
		CellFormatter cellFormatter = flexTable.getCellFormatter();

		flexTable.setHTML(0, 0, ConversationWidgetConstants.NR);
		flexTable.setHTML(0, 1, ConversationWidgetConstants.DATE);
		flexTable.setHTML(0, 2, ConversationWidgetConstants.FROM);
		flexTable.setHTML(0, 3, ConversationWidgetConstants.MESSAGE);

		DOM.getElementById("loading").getStyle().setDisplay(Display.BLOCK);
		DBGetMessagesForConversationAsync rpc = (DBGetMessagesForConversationAsync) GWT.create(DBGetMessagesForConversation.class);
		ServiceDefTarget tar = (ServiceDefTarget) rpc;
		String moduleURL = GWT.getModuleBaseURL() + "DBGetMessagesForConversationImpl";
		tar.setServiceEntryPoint(moduleURL);

		rpc.getAllMessagesForConversation(id, new AsyncCallback<List<Message>>() {

			@Override
			public void onSuccess(List<Message> result) {
				DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);

				isConversationView = false;
				isMessageView = true;

				int start = 1;

				for (Message message : result) {

					flexTable.setHTML(start, 0, String.valueOf(message.getNumber()));
					flexTable.setHTML(start, 1, message.getDate());
					flexTable.setHTML(start, 2,
							message.getFrom().equals(userInfo.getUsername()) ? message.getFrom() : "<b>" + message.getFrom() + "</b>");
					flexTable.setHTML(start, 3, message.getMessage());

					start++;
				}

			}

			@Override
			public void onFailure(Throwable caught) {
				DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
				DialogBox dialogBox = DialogBoxCreator.createDialogBox(ConversationWidgetConstants.DIALOG_BOX_FAILED_GETTING_MESSAGES_TITLE,
						caught.getMessage(), DialogBoxConstants.CLOSE_BUTTON, false, false);
				dialogBox.setGlassEnabled(true);
				dialogBox.setAnimationEnabled(true);
				dialogBox.center();
				dialogBox.show();
			}
		});

		cellFormatter.addStyleName(0, 0, "firstHeaderCell");
		cellFormatter.addStyleName(0, 3, "lastHeaderCell");
		cellFormatter.setWidth(0, 1, "200px");
		cellFormatter.setWidth(0, 2, "200px");
		cellFormatter.setWidth(0, 3, "800px");
		flexTable.getRowFormatter().addStyleName(0, "headerRow");

		// Reply Box
		VerticalPanel replyPanel = new VerticalPanel();

		HTML replyText = new HTML("<br><br><b>Respond to this conversation:</b>");

		final TextArea replyBox = new TextArea();
		replyBox.setVisibleLines(8);
		replyBox.setWidth("400px");
		replyBox.addStyleName("reply-box");

		Button replyButton = new Button();
		replyButton.setText("Send");
		replyButton.addStyleName("reply-button");

		replyButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String text = replyBox.getText();

				if (text != null && !text.trim().equals("")) {
					ReplyMessage message = new ReplyMessage(text, userInfo.getUsername(), lastId);

					DOM.getElementById("loading").getStyle().setDisplay(Display.BLOCK);
					DBReplyToConversationAsync rpc = (DBReplyToConversationAsync) GWT.create(DBReplyToConversation.class);
					ServiceDefTarget tar = (ServiceDefTarget) rpc;
					String moduleURL = GWT.getModuleBaseURL() + "DBReplyToConversationImpl";
					tar.setServiceEntryPoint(moduleURL);

					// rpc.replyToConversation(message, new AsyncCallback<Void>() {
					//
					// @Override
					// public void onFailure(Throwable caught) {
					// DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
					//
					// DialogBox dialogBox = DialogBoxCreator.createDialogBox(ConversationWidgetConstants.DIALOG_BOX_EMPTY_REPLY_TITLE,
					// caught.getMessage(), DialogBoxConstants.CLOSE_BUTTON, false, false);
					// dialogBox.setGlassEnabled(true);
					// dialogBox.setAnimationEnabled(true);
					// dialogBox.center();
					// dialogBox.show();
					//
					// viewConversation(lastId, convPanel);
					// }
					//
					// @Override
					// public void onSuccess(Void result) {
					// DOM.getElementById("loading").getStyle().setDisplay(Display.NONE);
					// viewConversation(lastId, convPanel);
					// }
					// });
				} else {
					DialogBox dialogBox = DialogBoxCreator.createDialogBox(ConversationWidgetConstants.DIALOG_BOX_EMPTY_REPLY_TITLE,
							ConversationWidgetConstants.DIALOG_BOX_EMPTY_REPLY_MESSAGE, DialogBoxConstants.CLOSE_BUTTON, false, false);
					dialogBox.setGlassEnabled(true);
					dialogBox.setAnimationEnabled(true);
					dialogBox.center();
					dialogBox.show();
				}
			}
		});

		replyPanel.add(replyText);
		replyPanel.add(replyBox);
		replyPanel.add(replyButton);

		vPanel.add(flexTable);
		vPanel.add(replyPanel);
		convPanel.add(vPanel);

	}
}
