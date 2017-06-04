package com.andreiolar.abms.client.widgets;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import com.andreiolar.abms.client.exception.ClientCardException;
import com.andreiolar.abms.client.exception.PersonalUpkeepInformationNotFoundException;
import com.andreiolar.abms.client.jso.ClientCreditCardResponse;
import com.andreiolar.abms.client.jso.ClientCreditCardResponseHandler;
import com.andreiolar.abms.client.jso.ClientStripe;
import com.andreiolar.abms.client.jso.ClientStripeFactory;
import com.andreiolar.abms.client.rpc.DBPersonalCosts;
import com.andreiolar.abms.client.rpc.DBPersonalCostsAsync;
import com.andreiolar.abms.client.rpc.UpkeepPayment;
import com.andreiolar.abms.client.rpc.UpkeepPaymentAsync;
import com.andreiolar.abms.client.utils.DateUtil;
import com.andreiolar.abms.shared.PersonalUpkeepInformationWrapper;
import com.andreiolar.abms.shared.UserDetails;
import com.arcbees.stripe.client.CreditCard;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.addins.client.popupmenu.MaterialPopupMenu;
import gwt.material.design.client.constants.ButtonType;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.constants.ModalType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialLoader;
import gwt.material.design.client.ui.MaterialModal;
import gwt.material.design.client.ui.MaterialModalContent;
import gwt.material.design.client.ui.MaterialModalFooter;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialTitle;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Hr;

public class PersonalCostsWidget extends Composite implements CustomWidget {

	private UserDetails userDetails;

	private String previousMonth;
	private String year;

	public PersonalCostsWidget(UserDetails userDetails) {
		this.userDetails = userDetails;
		this.previousMonth = DateUtil.getPreviousMonthAsString(new Date());
		this.year = DateUtil.getYearForPreviousMonth(new Date());

		initWidget(initializeWidget());
	}

	@Override
	public Widget initializeWidget() {
		MaterialPanel panel = new MaterialPanel();

		MaterialLabel title = new MaterialLabel("Personal Costs View");
		title.setTextColor(Color.BLUE);
		title.setTextAlign(TextAlign.CENTER);
		title.setFontSize("36px");
		title.setFontWeight(FontWeight.BOLD);
		panel.add(title);

		panel.add(new Hr());

		DBPersonalCostsAsync service = (DBPersonalCostsAsync) GWT.create(DBPersonalCosts.class);
		ServiceDefTarget target = (ServiceDefTarget) service;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "DBPersonalCostsImpl";
		target.setServiceEntryPoint(moduleRelativeURL);

		MaterialLoader.showLoading(true);

		service.getPersonalUpkeepInformation(userDetails, previousMonth + " " + year, new AsyncCallback<PersonalUpkeepInformationWrapper>() {

			@Override
			public void onSuccess(PersonalUpkeepInformationWrapper result) {
				MaterialLoader.showLoading(false);

				MaterialLabel label = new MaterialLabel();
				label.getElement()
						.setInnerHTML("Below you will find all informations needed about your personal upkeep costs for " + previousMonth + " " + year
								+ "." + "<br/><br/>"
								+ "Note: This view can be exported as PDF by right clicking anywere in the view and selecting Export as PDF");
				label.setMarginTop(25.0);
				label.setMarginLeft(25.0);
				label.setFontSize("18px");
				panel.add(label);

				MaterialPanel costsDescriptionPanel = new MaterialPanel();
				costsDescriptionPanel.setShadow(2);
				costsDescriptionPanel.setStyleName("pay-personal-costs-view");

				MaterialLabel month = new MaterialLabel();
				month.getElement().setInnerHTML("Personalized upkeep report for <span style=\"color: #9e9e9e\">"
						+ result.getPersonalUpkeepInformation().getLuna() + "</span>");
				month.setMarginTop(25.0);
				month.setMarginLeft(25.0);
				costsDescriptionPanel.add(month);

				MaterialLabel nameAndAptNumber = new MaterialLabel();
				nameAndAptNumber.getElement()
						.setInnerHTML("Upkeep report for apartment: <span style=\"color: #9e9e9e\">"
								+ result.getPersonalUpkeepInformation().getAptNumber() + "</span>" + "<br/>"
								+ "Responsible person: <span style=\"color: #9e9e9e\">" + result.getPersonalUpkeepInformation().getNume() + "</span>"
								+ "<br/>" + "Number of persons: <span style=\"color: #9e9e9e\">"
								+ result.getPersonalUpkeepInformation().getNumarPersoane() + "</span>");
				nameAndAptNumber.setMarginTop(25.0);
				nameAndAptNumber.setMarginLeft(25.0);
				costsDescriptionPanel.add(nameAndAptNumber);

				MaterialLabel spatiuComun = new MaterialLabel();
				spatiuComun.getElement()
						.setInnerHTML("Common space: <span style=\"color: #9e9e9e\">" + result.getPersonalUpkeepInformation().getSpatiuComun()
								+ " mp</span>" + "<br/>Note: It will be reflected within the heating costs.");
				spatiuComun.setMarginTop(25.0);
				spatiuComun.setMarginLeft(25.0);
				costsDescriptionPanel.add(spatiuComun);

				MaterialLabel suprafataApt = new MaterialLabel();
				suprafataApt.getElement()
						.setInnerHTML("Apartment surface: <span style=\"color: #9e9e9e\">" + result.getPersonalUpkeepInformation().getSuprafataApt()
								+ " mp</span>" + "<br/>" + "Note: It will be reflected within the heating costs." + "<br/>"
								+ "Note: It will be 0 for tenants with own heating plants, otherwise the whole apartment surface will be displayed.");
				suprafataApt.setMarginTop(25.0);
				suprafataApt.setMarginLeft(25.0);
				costsDescriptionPanel.add(suprafataApt);

				MaterialLabel incalzire = new MaterialLabel();
				incalzire.getElement().setInnerHTML("Heating: <span style=\"color: #9e9e9e\">" + result.getPersonalUpkeepInformation().getIncalzire()
						+ " RON</span>" + "<br/>" + "Note: It is calculated based on the common space and the apartment surface.");
				incalzire.setMarginTop(25.0);
				incalzire.setMarginLeft(25.0);
				costsDescriptionPanel.add(incalzire);

				MaterialLabel apaCaldaMenajera = new MaterialLabel();
				apaCaldaMenajera.getElement().setInnerHTML("Hot water: <span style=\"color: #9e9e9e\">"
						+ result.getPersonalUpkeepInformation().getApaCaldaMenajera()
						+ " RON</span><br>Note: It is calculated as following: TERMO-ACM + AR din ACM.<br>Note: It will be 0 for tenants with own heating plants.");
				apaCaldaMenajera.setMarginTop(25.0);
				apaCaldaMenajera.setMarginLeft(25.0);
				costsDescriptionPanel.add(apaCaldaMenajera);

				MaterialLabel apaReceSiCanalizare = new MaterialLabel();
				apaReceSiCanalizare.getElement().setInnerHTML("Cold water and sewerage: <span style=\"color: #9e9e9e\">"
						+ result.getPersonalUpkeepInformation().getApaReceSiCanalizare() + " RON</span>");
				apaReceSiCanalizare.setMarginTop(25.0);
				apaReceSiCanalizare.setMarginLeft(25.0);
				costsDescriptionPanel.add(apaReceSiCanalizare);

				MaterialLabel gunoi = new MaterialLabel();
				gunoi.getElement()
						.setInnerHTML("Garbage: <span style=\"color: #9e9e9e\">" + result.getPersonalUpkeepInformation().getGunoi() + " RON</span>"
								+ "<br/>"
								+ "Note: It is calculated as following: <span style=\"color: #9e9e9e\">10.43 RON * Number of persons</span>.");
				gunoi.setMarginTop(25.0);
				gunoi.setMarginLeft(25.0);
				costsDescriptionPanel.add(gunoi);

				MaterialLabel curent = new MaterialLabel();
				curent.getElement()
						.setInnerHTML("Electricity: <span style=\"color: #9e9e9e\">" + result.getPersonalUpkeepInformation().getCurent()
								+ " RON</span>" + "<br/>" + "Note: Common apartment building consumpion. <br/>"
								+ "Note: It is calculated as following: <span style=\"color: #9e9e9e\">1 RON * Number of persons</span>.");
				curent.setMarginTop(25.0);
				curent.setMarginLeft(25.0);
				costsDescriptionPanel.add(curent);

				MaterialLabel gaz = new MaterialLabel();
				gaz.getElement()
						.setInnerHTML("Gas: <span style=\"color: #9e9e9e\">" + result.getPersonalUpkeepInformation().getGaz() + " RON</span>");
				gaz.setMarginTop(25.0);
				gaz.setMarginLeft(25.0);
				costsDescriptionPanel.add(gaz);

				MaterialLabel servicii = new MaterialLabel();
				servicii.getElement()
						.setInnerHTML("Other services: <span style=\"color: #9e9e9e\">" + result.getPersonalUpkeepInformation().getServicii()
								+ " RON</span>" + "<br/>" + "Note: Common services for the apartment building. For example: Cleaning.");
				servicii.setMarginTop(25.0);
				servicii.setMarginLeft(25.0);
				costsDescriptionPanel.add(servicii);

				MaterialLabel gospodaresti = new MaterialLabel();
				gospodaresti.getElement().setInnerHTML("Other apartment costs: <span style=\"color: #9e9e9e\">"
						+ result.getPersonalUpkeepInformation().getGospodaresti() + " RON</span>");
				gospodaresti.setMarginTop(25.0);
				gospodaresti.setMarginLeft(25.0);
				costsDescriptionPanel.add(gospodaresti);

				MaterialPanel moneyPanel = new MaterialPanel();
				moneyPanel.setWidth("40%");
				moneyPanel.setMarginTop(50.0);
				moneyPanel.setMarginLeft(25.0);
				moneyPanel.setPaddingBottom(25.0);

				BigDecimal cost = new BigDecimal(result.getPersonalUpkeepInformation().getCostTotal());
				BigDecimal costScaled = cost.setScale(2, RoundingMode.CEILING);

				MaterialLabel sumLabel = new MaterialLabel();
				sumLabel.getElement().setInnerHTML("<b>" + costScaled + "</b>" + " <sup>RON</sup>");
				sumLabel.setFontSize("60px");
				sumLabel.setTextColor(Color.BLUE);
				moneyPanel.add(sumLabel);

				MaterialLabel sumTextLabel = new MaterialLabel();
				sumTextLabel.setText("Total to be paid");
				sumTextLabel.setTextColor(Color.GREY);
				moneyPanel.add(sumTextLabel);

				costsDescriptionPanel.add(moneyPanel);
				panel.add(costsDescriptionPanel);

				MaterialPopupMenu contextMenu = new MaterialPopupMenu();

				MaterialLink pdfLink = new MaterialLink();
				pdfLink.setPadding(12.0);
				pdfLink.setDisplay(Display.BLOCK);
				pdfLink.setText("Export as PDF");
				pdfLink.setIconType(IconType.PICTURE_AS_PDF);
				pdfLink.setTextColor(Color.BLUE);
				pdfLink.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						String url = GWT.getModuleBaseURL() + "pdfGenerator?username=" + userDetails.getUsername() + "&month=" + previousMonth + " "
								+ year;
						Window.open(url, "_blank", "status=0,toolbar=0,menubar=0,location=0");
					}

				});

				contextMenu.add(pdfLink);

				costsDescriptionPanel.sinkEvents(Event.ONCONTEXTMENU);
				costsDescriptionPanel.addHandler(new ContextMenuHandler() {

					@Override
					public void onContextMenu(ContextMenuEvent event) {
						event.preventDefault();
						event.stopPropagation();

						int x = event.getNativeEvent().getClientX();
						int y = event.getNativeEvent().getClientY();

						RootPanel.get().add(contextMenu);
						contextMenu.setPopupPosition(x, y);
						contextMenu.open();
					}
				}, ContextMenuEvent.getType());

				Div buttonDiv = new Div();
				buttonDiv.setStyleName("personal-costs-pay-center-button");

				if (result.isStatus()) {
					MaterialLabel alreadyPaidLabel = new MaterialLabel();
					alreadyPaidLabel.setText("Consumption costs already paid.");
					alreadyPaidLabel.setFontWeight(FontWeight.BOLD);
					alreadyPaidLabel.setTextColor(Color.GREEN);
					alreadyPaidLabel.setTextAlign(TextAlign.CENTER);
					alreadyPaidLabel.setFontSize("22px");
					buttonDiv.add(alreadyPaidLabel);
				} else {
					MaterialButton payNowButton = new MaterialButton();
					payNowButton.setWaves(WavesType.LIGHT);
					payNowButton.setWidth("100%");
					payNowButton.setHeight("50px");
					payNowButton.setText("PAY NOW");
					payNowButton.setTextColor(Color.WHITE);
					payNowButton.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							MaterialModal paymentModal = createPaymentModal(result.getPersonalUpkeepInformation().getCostTotal(), panel);
							RootPanel.get().add(paymentModal);
							paymentModal.open();
						}
					});

					buttonDiv.add(payNowButton);
				}

				panel.add(buttonDiv);
			}

			@Override
			public void onFailure(Throwable caught) {
				MaterialLoader.showLoading(false);
				if (caught instanceof PersonalUpkeepInformationNotFoundException) {

				} else {
					MaterialModal materialModal = ModalCreator.createErrorModal("Something went wrong", caught);
					RootPanel.get().add(materialModal);
					materialModal.open();
				}
			}
		});

		return panel;
	}

	private MaterialModal createPaymentModal(String cost, MaterialPanel panel) {
		MaterialModal modal = new MaterialModal();
		modal.setWidth("500px");
		modal.setType(ModalType.DEFAULT);
		modal.setDismissible(false);
		modal.setInDuration(500);
		modal.setOutDuration(500);

		MaterialModalContent materialModalContent = new MaterialModalContent();
		MaterialTitle title = new MaterialTitle("Upkeep Payment");
		title.setTextAlign(TextAlign.CENTER);
		title.setTextColor(Color.BLUE);

		MaterialTextBox nameBox = new MaterialTextBox();
		nameBox.setPlaceholder("Name on Card");
		nameBox.setMarginTop(30.0);
		nameBox.setType(InputType.TEXT);

		MaterialTextBox cardNumberBox = new MaterialTextBox();
		cardNumberBox.setPlaceholder("Card Number");
		cardNumberBox.setMarginTop(30.0);
		cardNumberBox.setType(InputType.TEXT);
		cardNumberBox.setMaxLength(16);

		Div div = new Div();
		div.setMarginTop(30.0);
		div.setDisplay(Display.FLEX);

		MaterialTextBox monthBox = new MaterialTextBox();
		monthBox.setPlaceholder("Expiry Month");
		monthBox.setType(InputType.TEXT);
		monthBox.setWidth("30%");
		monthBox.getElement().getStyle().setMarginRight(5, Unit.PCT);
		monthBox.setMaxLength(2);

		MaterialTextBox yearBox = new MaterialTextBox();
		yearBox.setPlaceholder("Expiry Year");
		yearBox.setType(InputType.TEXT);
		yearBox.setWidth("30%");
		yearBox.getElement().getStyle().setMarginRight(5, Unit.PCT);
		yearBox.setMaxLength(4);

		MaterialTextBox cvcBox = new MaterialTextBox();
		cvcBox.setPlaceholder("CVC");
		cvcBox.setType(InputType.TEXT);
		cvcBox.setWidth("30%");
		cvcBox.setMaxLength(3);

		div.add(monthBox);
		div.add(yearBox);
		div.add(cvcBox);

		MaterialLabel label = new MaterialLabel();
		label.setMarginTop(30.0);
		label.getElement().setInnerHTML("Total Cost: <span style=\"color: #2196f3;\">" + cost + " RON</span>");

		MaterialButton payButton = new MaterialButton();
		payButton.setMarginTop(30.0);
		payButton.setText("PAY NOW");
		payButton.setWidth("100%");
		payButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				nameBox.clearErrorOrSuccess();
				cardNumberBox.clearErrorOrSuccess();
				monthBox.clearErrorOrSuccess();
				yearBox.clearErrorOrSuccess();
				cvcBox.clearErrorOrSuccess();

				String name = nameBox.getText();
				String cardNumber = cardNumberBox.getText();
				String cvc = cvcBox.getText();
				String month = monthBox.getText();
				String expiryYear = yearBox.getText();

				boolean canProcced = true;

				if (name == null || name.trim().equals("")) {
					canProcced = false;
					nameBox.setError("Name cannot be empty");
				}

				if (cardNumber == null || cardNumber.trim().equals("")) {
					canProcced = false;
					cardNumberBox.setError("Card Number cannot be empty");
				}

				if (monthBox.getText() == null || monthBox.getText().trim().equals("")) {
					canProcced = false;
					monthBox.setError("Expiry Month cannot be empty");
				}

				if (yearBox.getText() == null || yearBox.getText().trim().equals("")) {
					canProcced = false;
					yearBox.setError("Expiry Year cannot be empty");
				}

				if (cvc == null || cvc.trim().equals("")) {
					canProcced = false;
					cvcBox.setError("CVC cannot be empty");
				}

				if (canProcced) {
					payButton.setEnabled(false);

					final ClientStripe stripe = ClientStripeFactory.get();

					if (stripe.isInjected()) {
						doPayment(cost, panel, modal, cardNumberBox, monthBox, yearBox, cvcBox, payButton, name, cardNumber, month, expiryYear, cvc,
								stripe);
					} else {
						stripe.inject(new Callback<Void, Exception>() {

							@Override
							public void onSuccess(Void result) {
								doPayment(cost, panel, modal, cardNumberBox, monthBox, yearBox, cvcBox, payButton, name, cardNumber, month,
										expiryYear, cvc, stripe);
							}

							@Override
							public void onFailure(Exception reason) {
								modal.close();
								RootPanel.get().remove(modal);
								Window.alert(reason.getMessage());
							}
						});
					}
				}
			}
		});

		materialModalContent.add(title);
		materialModalContent.add(nameBox);
		materialModalContent.add(cardNumberBox);
		materialModalContent.add(div);
		materialModalContent.add(label);
		materialModalContent.add(payButton);

		modal.add(materialModalContent);

		MaterialModalFooter materialModalFooter = new MaterialModalFooter();
		MaterialButton closeButton = new MaterialButton();
		closeButton.setText("Close");
		closeButton.setType(ButtonType.FLAT);
		closeButton.addClickHandler(h -> {
			modal.close();
			RootPanel.get().remove(modal);
		});

		materialModalFooter.add(closeButton);

		modal.add(materialModalFooter);

		return modal;
	}

	private void doPayment(String cost, MaterialPanel panel, MaterialModal modal, MaterialTextBox cardNumberBox, MaterialTextBox monthBox,
			MaterialTextBox yearBox, MaterialTextBox cvcBox, MaterialButton payButton, String name, String cardNumber, String month,
			String expiryYear, String cvc, final ClientStripe stripe) {
		stripe.setPublishableKey("pk_test_7XjMYfSUJljmavIx7z5zChB0");

		int intMonth = 0;
		int intYear = 0;

		try {
			intMonth = Integer.parseInt(month);
		} catch (NumberFormatException nfe) {
			intMonth = 0;
		}

		try {
			intYear = Integer.parseInt(expiryYear);
		} catch (NumberFormatException nfe) {
			intYear = 0;
		}

		final CreditCard creditCard = new CreditCard.Builder().creditCardNumber(cardNumber).cvc(cvc).expirationMonth(intMonth).expirationYear(intYear)
				.name(name).build();

		stripe.getCreditCardToken(creditCard, new ClientCreditCardResponseHandler() {

			@Override
			public void onCreditCardReceived(int status, ClientCreditCardResponse creditCardResponse) {
				UpkeepPaymentAsync payRpc = (UpkeepPaymentAsync) GWT.create(UpkeepPayment.class);
				ServiceDefTarget payTarget = (ServiceDefTarget) payRpc;
				String payUrl = GWT.getModuleBaseURL() + "UpkeepPaymentImpl";
				payTarget.setServiceEntryPoint(payUrl);

				if (creditCardResponse.getId() != null) {
					String description = previousMonth + " " + year + " Upkeep Payment from " + userDetails.getFirstName() + " "
							+ userDetails.getLastName() + " for Apt. Number " + userDetails.getApartmentNumber();

					payRpc.pay(creditCardResponse.getId(), cost, description, previousMonth + " " + year, userDetails, new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							payButton.setEnabled(true);

							if (caught instanceof ClientCardException) {
								displayErrors(cardNumberBox, monthBox, yearBox, cvcBox, caught.getMessage());
							} else {
								MaterialToast.fireToast(caught.getMessage(), "rounded");
							}

						}

						@Override
						public void onSuccess(Void result) {
							payButton.setEnabled(true);

							modal.close();
							RootPanel.get().remove(modal);

							MaterialToast.fireToast("Upkeep costs successfully paid!", "rounded");

							panel.clear();
							panel.add(new PersonalCostsWidget(userDetails));
						}
					});
				} else {
					displayErrors(cardNumberBox, monthBox, yearBox, cvcBox, creditCardResponse.getError().getCode());
					payButton.setEnabled(true);
				}
			}
		});
	}

	private void displayErrors(MaterialTextBox cardNumberBox, MaterialTextBox monthBox, MaterialTextBox yearBox, MaterialTextBox cvcBox,
			String code) {
		switch (code) {
			case "invalid_number" :
				cardNumberBox.setError("The card number is not a valid credit card number.");
				break;
			case "invalid_expiry_month" :
				monthBox.setError("The card's expiration month is invalid.");
				break;
			case "invalid_expiry_year" :
				yearBox.setError("The card's expiration year is invalid.");
				break;
			case "invalid_cvc" :
				cvcBox.setError("The card's security code is invalid.");
				break;
			case "incorrect_number" :
				cardNumberBox.setError("The card number is incorrect.");
				break;
			case "incorrect_cvc" :
				cvcBox.setError("The card's security code is incorrect.");
				break;
			case "expired_card" :
				cardNumberBox.setError("The card has expired.");
				break;
			case "card_declined" :
				cardNumberBox.setError("The card was declined.");
				break;
			case "processing_error" :
				MaterialToast.fireToast("An error occurred while processing the card. Please try again.", "rounded");
				break;
		}
	}

}
