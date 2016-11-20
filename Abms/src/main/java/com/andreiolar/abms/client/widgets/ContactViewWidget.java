package com.andreiolar.abms.client.widgets;

import java.util.Comparator;
import java.util.List;

import com.andreiolar.abms.client.constants.DialogBoxConstants;
import com.andreiolar.abms.client.constants.UserMenuConstants;
import com.andreiolar.abms.client.rpc.DBGetContactInfo;
import com.andreiolar.abms.client.rpc.DBGetContactInfoAsync;
import com.andreiolar.abms.client.rpc.DBSendMessage;
import com.andreiolar.abms.client.rpc.DBSendMessageAsync;
import com.andreiolar.abms.client.utils.DialogBoxCreator;
import com.andreiolar.abms.shared.ContactInfo;
import com.andreiolar.abms.shared.Conversation;
import com.andreiolar.abms.shared.UserInfo;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

public class ContactViewWidget extends Composite {

	private UserInfo userInfo;

	public ContactViewWidget(UserInfo userInfo) {
		this.userInfo = userInfo;

		initWidget(initializeWidget());
	}

	interface Resources extends ClientBundle {
		@Source("pm.png")
		ImageResource getImageResource();
	}

	private Widget initializeWidget() {
		// Create a CellTable
		final CellTable<ContactInfo> table = new CellTable<ContactInfo>();

		table.setWidth("100%", true);
		table.setAutoFooterRefreshDisabled(true);
		table.setAutoHeaderRefreshDisabled(true);

		final ListDataProvider<ContactInfo> dataProvider = new ListDataProvider<ContactInfo>();

		ListHandler<ContactInfo> listHandler = new ListHandler<ContactInfo>(dataProvider.getList());

		// Create a pager to control the table
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		SimplePager pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(table);
		pager.setPageSize(25);

		// Create the family name column.
		final TextColumn<ContactInfo> familyNameColumn = new TextColumn<ContactInfo>() {

			@Override
			public String getValue(ContactInfo object) {
				return object.getFamilyName();
			}
		};
		table.setColumnWidth(familyNameColumn, 20, Unit.PCT);

		// Make the family name sortable
		familyNameColumn.setSortable(true);
		listHandler.setComparator(familyNameColumn, new Comparator<ContactInfo>() {

			@Override
			public int compare(ContactInfo o1, ContactInfo o2) {
				return o1.getFamilyName().compareTo(o2.getFamilyName());
			}
		});

		// Create the family administrator column
		TextColumn<ContactInfo> familyAdministratorColumn = new TextColumn<ContactInfo>() {

			@Override
			public String getValue(ContactInfo object) {
				return object.getContactPerson();
			}
		};
		table.setColumnWidth(familyAdministratorColumn, 20, Unit.PCT);

		// Create the apartment number column
		TextColumn<ContactInfo> apartmentNuberColumn = new TextColumn<ContactInfo>() {

			@Override
			public String getValue(ContactInfo object) {
				return object.getApartmentNumber();
			}
		};
		table.setColumnWidth(apartmentNuberColumn, 10, Unit.PCT);

		// Make the apartment number sortable
		apartmentNuberColumn.setSortable(true);
		listHandler.setComparator(apartmentNuberColumn, new Comparator<ContactInfo>() {

			@Override
			public int compare(ContactInfo o1, ContactInfo o2) {
				Integer apt1 = Integer.valueOf(o1.getApartmentNumber());
				Integer apt2 = Integer.valueOf(o2.getApartmentNumber());
				return apt1.compareTo(apt2);
			}
		});
		table.addColumnSortHandler(listHandler);

		// Create the email column
		TextColumn<ContactInfo> emailColumn = new TextColumn<ContactInfo>() {

			@Override
			public String getValue(ContactInfo object) {
				return object.getEmail();
			}
		};
		table.setColumnWidth(emailColumn, 20, Unit.PCT);

		// Create the phone number column
		TextColumn<ContactInfo> phoneNumberColumn = new TextColumn<ContactInfo>() {

			@Override
			public String getValue(ContactInfo object) {
				return object.getPhoneNumber();
			}
		};
		table.setColumnWidth(phoneNumberColumn, 20, Unit.PCT);

		final Resources resources = GWT.create(Resources.class);

		Column<ContactInfo, ImageResource> pmButtonColumn = new Column<ContactInfo, ImageResource>(new ClickableImageResourceCell()) {

			@Override
			public ImageResource getValue(ContactInfo object) {
				return resources.getImageResource();
			}

			@Override
			public void render(Cell.Context context, ContactInfo object, SafeHtmlBuilder sb) {
				super.render(context, object, sb);
			}

			@Override
			public void onBrowserEvent(Context context, Element elem, ContactInfo object, NativeEvent event) {
				String destination = object.getContactPerson() + ", Apt. Number: " + object.getApartmentNumber();
				DialogBox messageDialogBox = createMessageDialogBox(userInfo.getUsername(), destination, object.getApartmentNumber());
				messageDialogBox.setGlassEnabled(true);
				messageDialogBox.setAnimationEnabled(true);
				messageDialogBox.center();
				messageDialogBox.show();
			}

		};

		// Add the columns
		table.addColumn(familyNameColumn, UserMenuConstants.FAMILY_NAME_COLUMN);
		table.addColumn(familyAdministratorColumn, UserMenuConstants.FAMILY_ADMINISTRATOR_COLUMN);
		table.addColumn(apartmentNuberColumn, UserMenuConstants.FAMILY_APARTMENT_NUMBER_COLUMN);
		table.addColumn(emailColumn, UserMenuConstants.EMAIL_ADDRESS_COLUMN);
		table.addColumn(phoneNumberColumn, UserMenuConstants.PHONE_NUMBER_COLUMN);
		table.addColumn(pmButtonColumn, UserMenuConstants.PM_COLUMN);

		DBGetContactInfoAsync rpcService = (DBGetContactInfoAsync) GWT.create(DBGetContactInfo.class);
		ServiceDefTarget target = (ServiceDefTarget) rpcService;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "DBGetContactInfoImpl";
		target.setServiceEntryPoint(moduleRelativeURL);

		rpcService.getContacts(new AsyncCallback<List<ContactInfo>>() {

			@Override
			public void onSuccess(List<ContactInfo> result) {

				table.setRowCount(result.size());

				dataProvider.addDataDisplay(table);

				List<ContactInfo> list = dataProvider.getList();
				for (ContactInfo contactInfo : result) {
					list.add(contactInfo);
				}

			}

			@Override
			public void onFailure(Throwable caught) {
				DialogBox dialogBox = DialogBoxCreator.createDialogBox(UserMenuConstants.DIALOG_BOX_FAILED_RETRIEVING_CONTACTS_TITLE,
						UserMenuConstants.DIALOG_BOX_FAILED_RETRIEVING_CONTACTS_MESSAGE + ": " + caught.getMessage(), DialogBoxConstants.CLOSE_BUTTON,
						false, false);
				dialogBox.setGlassEnabled(true);
				dialogBox.setAnimationEnabled(true);
				dialogBox.center();
				dialogBox.show();
			}
		});

		VerticalPanel vPanel = new VerticalPanel();
		vPanel.add(table);
		vPanel.add(pager);

		table.addStyleName("cellTable");
		pager.addStyleName("pager");

		return vPanel;
	}

	private static DialogBox createMessageDialogBox(final String username, String destination, final String apartmentNumber) {
		final DialogBox messageDialogBox = new DialogBox();
		messageDialogBox.setText(UserMenuConstants.MESSAGE_DIALOG_BOX_TITLE);
		messageDialogBox.setWidth("500px");

		VerticalPanel panel = new VerticalPanel();
		panel.setSpacing(6);
		messageDialogBox.setWidget(panel);

		Grid grid = new Grid(4, 2);

		Label toLabel = new Label();
		toLabel.setText("To");

		CustomTexBox toBox = new CustomTexBox();
		toBox.setEnabled(false);
		toBox.setText(destination);

		Label subjectLabel = new Label();
		subjectLabel.setText("Subject");

		final CustomTexBox subjectBox = new CustomTexBox();
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
		grid.setWidget(0, 1, toBox);

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
				String subject = subjectBox.getText();
				String messageText = messageBox.getText();
				String error = "";

				boolean isSubject = true;
				boolean isMessage = true;

				if (subject == null || subject.trim().equals("")) {
					error += "<p>Please type in a subject!</p>";
					isSubject = false;
				}

				if (messageText == null || messageText.trim().equals("")) {
					error += "<p>Please write a message!</p>";
					isMessage = false;
				}

				if (isSubject && isMessage) {
					DBSendMessageAsync rpc = (DBSendMessageAsync) GWT.create(DBSendMessage.class);
					ServiceDefTarget tar = (ServiceDefTarget) rpc;
					String moduleURL = GWT.getModuleBaseURL() + "DBSendMessageImpl";
					tar.setServiceEntryPoint(moduleURL);

					Conversation conversation = new Conversation(username, apartmentNumber, subject, messageText);

					rpc.sendMessage(conversation, new AsyncCallback<Boolean>() {

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
}
