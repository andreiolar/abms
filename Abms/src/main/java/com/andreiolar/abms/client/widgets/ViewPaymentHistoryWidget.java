package com.andreiolar.abms.client.widgets;

import java.util.List;

import com.andreiolar.abms.client.rpc.PaymentHistoryService;
import com.andreiolar.abms.client.rpc.PaymentHistoryServiceAsync;
import com.andreiolar.abms.shared.PaymentEntry;
import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialModal;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Hr;

public class ViewPaymentHistoryWidget extends Composite implements CustomWidget {

	private UserDetails userDetails;

	public ViewPaymentHistoryWidget(UserDetails userDetails) {
		this.userDetails = userDetails;

		initWidget(initializeWidget());
	}

	@Override
	public Widget initializeWidget() {
		MaterialPanel panel = new MaterialPanel();

		MaterialLabel title = new MaterialLabel("View Payment History");
		title.setTextColor(Color.BLUE);
		title.setTextAlign(TextAlign.CENTER);
		title.setFontSize("36px");
		title.setFontWeight(FontWeight.BOLD);
		panel.add(title);

		panel.add(new Hr());

		PaymentHistoryServiceAsync service = (PaymentHistoryServiceAsync) GWT.create(PaymentHistoryService.class);
		ServiceDefTarget target = (ServiceDefTarget) service;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "PaymentHistoryServiceImpl";
		target.setServiceEntryPoint(moduleRelativeURL);

		service.listPaymentEntries(userDetails.getApartmentNumber(), new AsyncCallback<List<PaymentEntry>>() {

			@Override
			public void onSuccess(List<PaymentEntry> result) {
				Div mainDiv = new Div();
				mainDiv.addStyleName("payment-history-main-div");

				Div boxDiv = new Div();
				boxDiv.addStyleName("payment-history-box-div");

				Div headerDiv = new Div();
				headerDiv.addStyleName("payment-history-header");

				MaterialLabel transactionIdHeader = new MaterialLabel("Transaction ID");
				transactionIdHeader.setWidth("20%");
				transactionIdHeader.setTextColor(Color.GREY_DARKEN_2);
				transactionIdHeader.setFontSize("1.3em");

				MaterialLabel dateHeader = new MaterialLabel("Date");
				dateHeader.setWidth("20%");
				dateHeader.setTextColor(Color.GREY_DARKEN_2);
				dateHeader.setFontSize("1.3em");

				MaterialLabel paymentStatusHeader = new MaterialLabel("Payment Status");
				paymentStatusHeader.setWidth("15%");
				paymentStatusHeader.setTextColor(Color.GREY_DARKEN_2);
				paymentStatusHeader.setFontSize("1.3em");

				MaterialLabel amountHeader = new MaterialLabel("Amount");
				amountHeader.setWidth("20%");
				amountHeader.setTextColor(Color.GREY_DARKEN_2);
				amountHeader.setFontSize("1.3em");

				MaterialLabel descriptionHeader = new MaterialLabel("Description");
				descriptionHeader.setWidth("25%");
				descriptionHeader.setTextColor(Color.GREY_DARKEN_2);
				descriptionHeader.setFontSize("1.3em");

				headerDiv.add(transactionIdHeader);
				headerDiv.add(dateHeader);
				headerDiv.add(paymentStatusHeader);
				headerDiv.add(amountHeader);
				headerDiv.add(descriptionHeader);

				boxDiv.add(headerDiv);

				if (!result.isEmpty()) {
					for (PaymentEntry paymentEntry : result) {
						Div entryDiv = new Div();
						entryDiv.addStyleName("payment-history-entry-div");

						MaterialLabel transactionId = new MaterialLabel(paymentEntry.getTrasactionId());
						transactionId.setWidth("20%");
						transactionId.setTextColor(Color.GREY_DARKEN_1);
						transactionId.setFontSize("1.1em");

						MaterialLabel date = new MaterialLabel(paymentEntry.getDate());
						date.setWidth("20%");
						date.setTextColor(Color.GREY_DARKEN_1);
						date.setFontSize("1.1em");

						MaterialLabel paymentStatus = new MaterialLabel("Paid");
						paymentStatus.setWidth("15%");
						paymentStatus.setTextColor(Color.GREY_DARKEN_1);
						paymentStatus.setFontSize("1.1em");

						MaterialLabel amount = new MaterialLabel(paymentEntry.getCost() + " RON");
						amount.setWidth("20%");
						amount.setTextColor(Color.GREY_DARKEN_1);
						amount.setFontSize("1.1em");

						MaterialLabel description = new MaterialLabel(paymentEntry.getDescription());
						description.setWidth("25%");
						description.setTextColor(Color.GREY_DARKEN_1);
						description.setFontSize("1.1em");

						entryDiv.add(transactionId);
						entryDiv.add(date);
						entryDiv.add(paymentStatus);
						entryDiv.add(amount);
						entryDiv.add(description);

						boxDiv.add(entryDiv);
					}
				} else {
					Div noRecordsFoundDiv = new Div();
					noRecordsFoundDiv.setWidth("100%");
					noRecordsFoundDiv.setPaddingTop(15.0);

					MaterialLabel label = new MaterialLabel();
					label.getElement().setInnerHTML(
							"No payments processed by this account, therefore no records have been found.<br>Your billings can be visualized and paid under:<br>"
									+ "&nbsp; - Administration -> Consumption Reading (for Consumption related costs)<br>"
									+ "&nbsp; - Administration -> Personal Costs View (for Upkeep related costs)");
					label.setTextColor(Color.GREY_DARKEN_1);
					label.setFontSize("1.1em");

					noRecordsFoundDiv.add(label);

					boxDiv.add(noRecordsFoundDiv);
				}

				mainDiv.add(boxDiv);

				panel.add(mainDiv);
			}

			@Override
			public void onFailure(Throwable caught) {
				MaterialModal materialModal = ModalCreator.createErrorModal("Something went wrong", caught);
				RootPanel.get().add(materialModal);
				materialModal.open();
			}
		});

		return panel;
	}
}
