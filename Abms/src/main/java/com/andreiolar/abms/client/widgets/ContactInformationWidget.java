package com.andreiolar.abms.client.widgets;

import java.util.Comparator;
import java.util.List;

import com.andreiolar.abms.client.rpc.DBGetContactInfo;
import com.andreiolar.abms.client.rpc.DBGetContactInfoAsync;
import com.andreiolar.abms.shared.ContactInfo;
import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import gwt.material.design.client.constants.ButtonType;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.data.SelectionType;
import gwt.material.design.client.data.component.RowComponent;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialContainer;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLoader;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.client.ui.html.Hr;
import gwt.material.design.client.ui.table.MaterialDataTable;
import gwt.material.design.client.ui.table.cell.TextColumn;
import gwt.material.design.client.ui.table.cell.WidgetColumn;

public final class ContactInformationWidget {

	private ContactInformationWidget() {
	}

	/**
	 * Used to construct the {@link MaterialDataTable} representing the Contact Information Widget.
	 **/
	public static void constructContactInformationWidget(MaterialContainer container, UserDetails userDetails) {
		container.clear();

		MaterialPanel panel = new MaterialPanel();

		MaterialLabel title = new MaterialLabel("Contact Information");
		title.setTextColor(Color.BLUE);
		title.setTextAlign(TextAlign.CENTER);
		title.setFontSize("36px");
		title.setFontWeight(FontWeight.BOLD);
		panel.add(title);

		panel.add(new Hr());

		MaterialDataTable<ContactInfo> table = new MaterialDataTable<ContactInfo>();
		table.setUseStickyHeader(true);
		table.setUseCategories(false);
		table.setUseRowExpansion(false);
		table.setSelectionType(SelectionType.NONE);
		table.setRedraw(true);
		table.setStyleName("contact-info-table");

		panel.add(table);

		container.add(panel);

		table.getTableTitle().setText("Neighbors Contact Information");

		table.addColumn(new WidgetColumn<ContactInfo, MaterialImage>() {

			@Override
			public MaterialImage getValue(ContactInfo object) {
				String profilePictureUsername = object.getUsername().replaceAll("\\.", "");

				MaterialImage materialImage = new MaterialImage();
				materialImage.setUrl("http://res.cloudinary.com/andreiolar/image/upload/" + profilePictureUsername + ".png");
				materialImage.addErrorHandler(new ErrorHandler() {

					@Override
					public void onError(ErrorEvent event) {
						if (object.getGender().equals("Female")) {
							materialImage.setUrl("images/icons/female.png");
						} else {
							materialImage.setUrl("images/icons/male.png");
						}

					}
				});

				materialImage.setWidth("40px");
				materialImage.setHeight("40px");
				materialImage.setPadding(4);
				materialImage.setMarginTop(8);
				materialImage.setBackgroundColor(Color.GREY_LIGHTEN_2);
				materialImage.setCircle(true);

				return materialImage;
			}

			@Override
			public String getHeaderWidth() {
				return "10%";
			}

		});

		table.addColumn(new TextColumn<ContactInfo>() {

			@Override
			public String getHeaderWidth() {
				return "10%";
			}

			@Override
			public Comparator<? super RowComponent<ContactInfo>> getSortComparator() {
				return (o1, o2) -> o1.getData().getFamilyName().compareTo(o2.getData().getFamilyName());
			}

			@Override
			public String getValue(ContactInfo object) {
				return object.getFamilyName();
			}
		}, "Family Name");

		table.addColumn(new TextColumn<ContactInfo>() {

			@Override
			public String getHeaderWidth() {
				return "20%";
			}

			@Override
			public Comparator<? super RowComponent<ContactInfo>> getSortComparator() {
				return (o1, o2) -> o1.getData().getContactPerson().compareTo(o2.getData().getContactPerson());
			}

			@Override
			public String getValue(ContactInfo object) {
				return object.getContactPerson();
			}
		}, "Contact Person");

		table.addColumn(new TextColumn<ContactInfo>() {

			@Override
			public String getHeaderWidth() {
				return "10%";
			}

			@Override
			public Comparator<? super RowComponent<ContactInfo>> getSortComparator() {
				return (o1, o2) -> Integer.compare(Integer.parseInt(o1.getData().getApartmentNumber()),
						Integer.parseInt(o2.getData().getApartmentNumber()));
			}

			@Override
			public String getValue(ContactInfo object) {
				return object.getApartmentNumber();
			}
		}, "Apt. Number");

		table.addColumn(new TextColumn<ContactInfo>() {

			@Override
			public String getHeaderWidth() {
				return "20%";
			}

			@Override
			public Comparator<? super RowComponent<ContactInfo>> getSortComparator() {
				return (o1, o2) -> o1.getData().getEmail().compareTo(o2.getData().getEmail());
			}

			@Override
			public String getValue(ContactInfo object) {
				return object.getEmail();
			}
		}, "E-Mail Address");

		table.addColumn(new TextColumn<ContactInfo>() {

			@Override
			public String getHeaderWidth() {
				return "10%";
			}

			@Override
			public Comparator<? super RowComponent<ContactInfo>> getSortComparator() {
				return (o1, o2) -> o1.getData().getPhoneNumber().compareTo(o2.getData().getPhoneNumber());
			}

			@Override
			public String getValue(ContactInfo object) {
				return object.getPhoneNumber();
			}
		}, "Phone Number");

		table.addColumn(new WidgetColumn<ContactInfo, MaterialButton>() {

			@Override
			public String getHeaderWidth() {
				return "20%";
			}

			@Override
			public MaterialButton getValue(ContactInfo object) {
				if (object.getUsername().equals(userDetails.getUsername())) {
					MaterialButton sendMessageButton = new MaterialButton();
					sendMessageButton.setType(ButtonType.FLAT);

					return sendMessageButton;
				}

				MaterialButton sendMessageButton = new MaterialButton();
				sendMessageButton.setText("SEND MESSAGE");
				sendMessageButton.setTextColor(Color.BLUE);
				sendMessageButton.setType(ButtonType.FLAT);
				sendMessageButton.getElement().getStyle().setTextDecoration(TextDecoration.UNDERLINE);

				sendMessageButton.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						Window.alert("Test: " + object.getContactPerson());
					}
				});

				return sendMessageButton;
			}
		});

		MaterialLoader.showLoading(true);
		DBGetContactInfoAsync rpcService = (DBGetContactInfoAsync) GWT.create(DBGetContactInfo.class);
		ServiceDefTarget target = (ServiceDefTarget) rpcService;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "DBGetContactInfoImpl";
		target.setServiceEntryPoint(moduleRelativeURL);

		rpcService.getContacts(new AsyncCallback<List<ContactInfo>>() {

			@Override
			public void onSuccess(List<ContactInfo> result) {
				MaterialLoader.showLoading(false);
				table.setRowData(0, result);
				table.setRowCount(result.size());
				table.refreshView();
			}

			@Override
			public void onFailure(Throwable caught) {
				MaterialLoader.showLoading(false);
				MaterialToast.fireToast("Unable to retrieve neighbors contact information.", "rounded");
			}
		});
	}
}
