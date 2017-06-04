package com.andreiolar.abms.client.widgets;

import java.util.Date;

import com.andreiolar.abms.client.exception.ConsumptionReportNotFoundException;
import com.andreiolar.abms.client.rpc.ConsumptionPayment;
import com.andreiolar.abms.client.rpc.ConsumptionPaymentAsync;
import com.andreiolar.abms.client.rpc.DBSearchForConsumptionReport;
import com.andreiolar.abms.client.rpc.DBSearchForConsumptionReportAsync;
import com.andreiolar.abms.client.rpc.DBSelfReading;
import com.andreiolar.abms.client.rpc.DBSelfReadingAsync;
import com.andreiolar.abms.client.utils.DateUtil;
import com.andreiolar.abms.shared.ConsumptionCost;
import com.andreiolar.abms.shared.ConsumptionCostReport;
import com.andreiolar.abms.shared.SelfReading;
import com.andreiolar.abms.shared.UserDetails;
import com.arcbees.stripe.client.CreditCard;
import com.arcbees.stripe.client.CreditCardResponseHandler;
import com.arcbees.stripe.client.Stripe;
import com.arcbees.stripe.client.StripeFactory;
import com.arcbees.stripe.client.jso.CreditCardResponse;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.addins.client.stepper.MaterialStep;
import gwt.material.design.addins.client.stepper.MaterialStepper;
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

public class ConsumptionWidget extends Composite implements CustomWidget {

	private UserDetails userDetails;
	private String previousMonth;
	private String year;

	public ConsumptionWidget(UserDetails userDetails) {
		this.userDetails = userDetails;
		this.previousMonth = DateUtil.getPreviousMonthAsString(new Date());
		this.year = DateUtil.getYearForPreviousMonth(new Date());

		initWidget(initializeWidget());
	}

	@Override
	public Widget initializeWidget() {
		MaterialLoader.showLoading(false);

		MaterialPanel panel = new MaterialPanel();

		MaterialLabel title = new MaterialLabel("Consumption Reading");
		title.setTextColor(Color.BLUE);
		title.setTextAlign(TextAlign.CENTER);
		title.setFontSize("36px");
		title.setFontWeight(FontWeight.BOLD);
		panel.add(title);

		panel.add(new Hr());

		DBSearchForConsumptionReportAsync service = (DBSearchForConsumptionReportAsync) GWT.create(DBSearchForConsumptionReport.class);
		ServiceDefTarget target = (ServiceDefTarget) service;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "DBSearchForConsumptionReportImpl";
		target.setServiceEntryPoint(moduleRelativeURL);

		MaterialLoader.showLoading(true);

		// On success means consumption report is found.
		service.searchForConsumptionReport(userDetails, previousMonth + " " + year, new AsyncCallback<ConsumptionCostReport>() {

			@Override
			public void onSuccess(ConsumptionCostReport result) {
				MaterialLoader.showLoading(false);

				ConsumptionCost consumptionCost = new ConsumptionCost(new SelfReading(result.getElectricity(), result.getGas()));

				MaterialLabel label = new MaterialLabel("You have already submitted the consuption report for " + previousMonth + " " + year
						+ ". Below you will find all information from your consumption report.");
				label.setMarginTop(25.0);
				label.setMarginLeft(25.0);
				label.setFontSize("18px");
				panel.add(label);

				MaterialPanel moneyDescriptionPanel = new MaterialPanel();
				moneyDescriptionPanel.setShadow(2);
				moneyDescriptionPanel.setStyleName("pay-consumption-description-panel");

				MaterialPanel leftMoneyDescriptionPanel = new MaterialPanel();
				leftMoneyDescriptionPanel.setWidth("40%");
				leftMoneyDescriptionPanel.setFloat(Float.LEFT);

				MaterialLabel sumLabel = new MaterialLabel();
				sumLabel.getElement().setInnerHTML("<b>" + result.getCost() + "</b>" + " <sup>RON</sup>");
				sumLabel.setFontSize("60px");
				sumLabel.setTextColor(Color.BLUE);
				sumLabel.setMarginLeft(25.0);
				sumLabel.setMarginTop(25.0);
				leftMoneyDescriptionPanel.add(sumLabel);

				MaterialLabel sumTextLabel = new MaterialLabel();
				sumTextLabel.setText("Total to be paid");
				sumTextLabel.setTextColor(Color.GREY);
				sumTextLabel.setMarginLeft(25.0);
				leftMoneyDescriptionPanel.add(sumTextLabel);

				MaterialPanel rightMoneyDescriptionPanel = new MaterialPanel();
				rightMoneyDescriptionPanel.setWidth("40%");
				rightMoneyDescriptionPanel.setFloat(Float.RIGHT);

				MaterialLabel descriptionLabel = new MaterialLabel();
				descriptionLabel.getElement()
						.setInnerHTML("Payment description:<br />Consumption payment for " + userDetails.getFirstName() + " "
								+ userDetails.getLastName() + "<br /><br />Payment includes:<br />Electricity: "
								+ consumptionCost.getElectricityCost() + " RON<br />Gas: " + consumptionCost.getGasCost() + " RON");
				descriptionLabel.setTextColor(Color.GREY);
				descriptionLabel.setMarginTop(25.0);
				descriptionLabel.setMarginLeft(25.0);
				rightMoneyDescriptionPanel.add(descriptionLabel);

				moneyDescriptionPanel.add(leftMoneyDescriptionPanel);
				moneyDescriptionPanel.add(rightMoneyDescriptionPanel);

				panel.add(moneyDescriptionPanel);

				MaterialLabel noteLabel = new MaterialLabel(
						"Note: Cold and Hot Water costs are not included here. They are payed separately with the standard monthly bill.");
				noteLabel.setTextColor(Color.GREY);
				noteLabel.setStyleName("pay-consumption-note-label");
				panel.add(noteLabel);

				Div buttonDiv = new Div();
				buttonDiv.setStyleName("consumption-pay-center-button");

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
							MaterialModal paymentModal = createPaymentModal(result.getCost(), panel);
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
				if (caught instanceof ConsumptionReportNotFoundException) {
					// If no consumption submitted.
					MaterialLabel label = new MaterialLabel("Please submit the monthly consumption for " + previousMonth + " " + year + ".");
					label.setMarginTop(25.0);
					label.setMarginLeft(25.0);
					label.setFontSize("18px");
					panel.add(label);

					MaterialPanel stepperPanel = new MaterialPanel();
					stepperPanel.setShadow(3);
					stepperPanel.setStyleName("consumption-stepper");

					MaterialStepper stepper = new MaterialStepper();
					stepper.setShadow(1);

					MaterialStep coldWaterStep = new MaterialStep();
					MaterialStep hotWaterStep = new MaterialStep();
					hotWaterStep.setEnabled(false);
					MaterialStep electricityStep = new MaterialStep();
					electricityStep.setEnabled(false);
					MaterialStep gasStep = new MaterialStep();
					gasStep.setEnabled(false);

					/** Step 1: Cold Water */
					coldWaterStep.setStep(1);
					coldWaterStep.setTitle("Step 1");
					coldWaterStep.setDescription("Please enter your cold water consumption.");

					MaterialPanel coldWaterPanel = new MaterialPanel();
					coldWaterPanel.setWidth("100%");

					MaterialTextBox coldWaterTextBox = new MaterialTextBox();
					coldWaterTextBox.setType(InputType.NUMBER);
					coldWaterTextBox.setPlaceholder("Cold Water Consumption");
					coldWaterTextBox.setIconType(IconType.ASSIGNMENT);
					coldWaterPanel.add(coldWaterTextBox);

					coldWaterStep.add(coldWaterPanel);

					MaterialButton continueStepOneButton = new MaterialButton();
					continueStepOneButton.setText("Proceed to next step");
					continueStepOneButton.setGrid("l4");
					continueStepOneButton.setMarginTop(12.0);
					continueStepOneButton.setTextColor(Color.WHITE);
					continueStepOneButton.setWaves(WavesType.DEFAULT);
					continueStepOneButton.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							String coldWater = coldWaterTextBox.getText();

							if (coldWater.matches("^[1-9][0-9]*$")) {
								coldWaterTextBox.setSuccess("");
								stepper.setSuccess("");

								hotWaterStep.setEnabled(true);
								stepper.nextStep();
								coldWaterStep.setEnabled(false);
							} else {
								stepper.setError("Some errors occured!");
								coldWaterTextBox.setError("Not a valid input. Only non-zero starting numbers are allowed.");
							}
						}
					});
					coldWaterStep.add(continueStepOneButton);

					/** Step 2: Hot Water */
					hotWaterStep.setStep(2);
					hotWaterStep.setTitle("Step 2");
					hotWaterStep.setDescription("Please enter your hot water consumption.");

					MaterialPanel hotWaterPanel = new MaterialPanel();
					hotWaterPanel.setWidth("100%");

					MaterialTextBox hotWaterTextBox = new MaterialTextBox();
					hotWaterTextBox.setType(InputType.NUMBER);
					hotWaterTextBox.setPlaceholder("Hot Water Consumption");
					hotWaterTextBox.setIconType(IconType.ASSIGNMENT);
					hotWaterPanel.add(hotWaterTextBox);

					hotWaterStep.add(hotWaterPanel);

					MaterialButton continueStepTwoButton = new MaterialButton();
					continueStepTwoButton.setText("Proceed to next step");
					continueStepTwoButton.setGrid("l4");
					continueStepTwoButton.setMarginTop(12.0);
					continueStepTwoButton.setTextColor(Color.WHITE);
					continueStepTwoButton.setWaves(WavesType.DEFAULT);
					continueStepTwoButton.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							String hotWater = hotWaterTextBox.getText();

							if (hotWater.matches("^[1-9][0-9]*$")) {
								hotWaterTextBox.setSuccess("");
								stepper.setSuccess("");

								electricityStep.setEnabled(true);
								stepper.nextStep();
								hotWaterStep.setEnabled(false);
							} else {
								stepper.setError("Some errors occured!");
								hotWaterTextBox.setError("Not a valid input. Only non-zero starting numbers are allowed.");
							}
						}
					});
					hotWaterStep.add(continueStepTwoButton);

					MaterialButton previousStepTwoButton = new MaterialButton();
					previousStepTwoButton.setText("Go to previous step");
					previousStepTwoButton.setGrid("l4");
					previousStepTwoButton.setMarginTop(12.0);
					previousStepTwoButton.setType(ButtonType.FLAT);
					previousStepTwoButton.setWaves(WavesType.DEFAULT);
					previousStepTwoButton.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							coldWaterStep.setEnabled(true);
							stepper.prevStep();
							hotWaterStep.setEnabled(false);
						}
					});
					hotWaterStep.add(previousStepTwoButton);

					/** Step 3: Electricity */
					electricityStep.setStep(3);
					electricityStep.setTitle("Step 3");
					electricityStep.setDescription("Please enter your electricity consumption.");

					MaterialPanel electricityPanel = new MaterialPanel();
					electricityPanel.setWidth("100%");

					MaterialTextBox electricityTextBox = new MaterialTextBox();
					electricityTextBox.setType(InputType.NUMBER);
					electricityTextBox.setPlaceholder("Electricity Consumption");
					electricityTextBox.setIconType(IconType.ASSIGNMENT);
					electricityPanel.add(electricityTextBox);

					electricityStep.add(electricityPanel);

					MaterialButton continueStepThreeButton = new MaterialButton();
					continueStepThreeButton.setText("Proceed to next step");
					continueStepThreeButton.setGrid("l4");
					continueStepThreeButton.setMarginTop(12.0);
					continueStepThreeButton.setTextColor(Color.WHITE);
					continueStepThreeButton.setWaves(WavesType.DEFAULT);
					continueStepThreeButton.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							String electricity = electricityTextBox.getText();

							if (electricity.matches("^[1-9][0-9]*$")) {
								electricityTextBox.setSuccess("");
								stepper.setSuccess("");

								gasStep.setEnabled(true);
								stepper.nextStep();
								electricityStep.setEnabled(false);
							} else {
								stepper.setError("Some errors occured!");
								electricityTextBox.setError("Not a valid input. Only non-zero starting numbers are allowed.");
							}
						}
					});
					electricityStep.add(continueStepThreeButton);

					MaterialButton previousStepThreeButton = new MaterialButton();
					previousStepThreeButton.setText("Go to previous step");
					previousStepThreeButton.setGrid("l4");
					previousStepThreeButton.setMarginTop(12.0);
					previousStepThreeButton.setType(ButtonType.FLAT);
					previousStepThreeButton.setWaves(WavesType.DEFAULT);
					previousStepThreeButton.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							hotWaterStep.setEnabled(true);
							stepper.prevStep();
							electricityStep.setEnabled(false);
						}
					});
					electricityStep.add(previousStepThreeButton);

					/** Step 4: Gas */
					gasStep.setStep(4);
					gasStep.setTitle("Step 4");
					gasStep.setDescription("Please enter your gas consumption.");

					MaterialPanel gasPanel = new MaterialPanel();
					gasPanel.setWidth("100%");

					MaterialTextBox gasTextBox = new MaterialTextBox();
					gasTextBox.setType(InputType.NUMBER);
					gasTextBox.setPlaceholder("Gas Consumption");
					gasTextBox.setIconType(IconType.ASSIGNMENT);
					gasPanel.add(gasTextBox);

					gasStep.add(gasPanel);

					MaterialButton finishButton = new MaterialButton();
					finishButton.setText("Submit consumption");
					finishButton.setGrid("l4");
					finishButton.setMarginTop(12.0);
					finishButton.setTextColor(Color.WHITE);
					finishButton.setWaves(WavesType.DEFAULT);
					finishButton.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							String gas = gasTextBox.getText();

							if (gas.matches("^[1-9][0-9]*$")) {
								gasTextBox.setSuccess("");
								stepper.setSuccess("");

								String electricity = electricityTextBox.getText();
								String hotWater = hotWaterTextBox.getText();
								String coldWater = coldWaterTextBox.getText();

								SelfReading reading = new SelfReading(null, coldWater, hotWater, electricity, gas, previousMonth + " " + year);

								DBSelfReadingAsync rpcService = (DBSelfReadingAsync) GWT.create(DBSelfReading.class);
								ServiceDefTarget target = (ServiceDefTarget) rpcService;
								String moduleRelativeURL = GWT.getModuleBaseURL() + "DBSelfReadingImpl";
								target.setServiceEntryPoint(moduleRelativeURL);
								MaterialLoader.showLoading(true);

								rpcService.insertReading(userDetails, reading, new AsyncCallback<Boolean>() {

									@Override
									public void onSuccess(Boolean result) {
										MaterialLoader.showLoading(false);
										MaterialToast.fireToast("Consumption Report successfully submitted.", "rounded");
										panel.clear();
										panel.add(new ConsumptionWidget(userDetails));
									}

									@Override
									public void onFailure(Throwable caught) {
										MaterialLoader.showLoading(false);
										if (caught instanceof RuntimeException) {
											MaterialModal materialModal = ModalCreator.createErrorModal("Something went wrong", caught);
											RootPanel.get().add(materialModal);
											materialModal.open();
										} else {
											MaterialToast.fireToast(caught.getMessage(), "rounded");
										}

										stepper.reset();
									}
								});
							} else {
								stepper.setError("Some errors occured!");
								gasTextBox.setError("Not a valid input. Only non-zero starting numbers are allowed.");
							}
						}
					});
					gasStep.add(finishButton);

					MaterialButton previousStepFourButton = new MaterialButton();
					previousStepFourButton.setText("Go to previous step");
					previousStepFourButton.setGrid("l4");
					previousStepFourButton.setMarginTop(12.0);
					previousStepFourButton.setType(ButtonType.FLAT);
					previousStepFourButton.setWaves(WavesType.DEFAULT);
					previousStepFourButton.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							electricityStep.setEnabled(true);
							stepper.prevStep();
							gasPanel.setEnabled(false);
						}
					});
					gasStep.add(previousStepFourButton);

					stepper.add(coldWaterStep);
					stepper.add(hotWaterStep);
					stepper.add(electricityStep);
					stepper.add(gasStep);

					stepperPanel.add(stepper);
					panel.add(stepperPanel);
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
		MaterialTitle title = new MaterialTitle("Consumption Payment");
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

		Div div = new Div();
		div.setMarginTop(30.0);
		div.setDisplay(Display.FLEX);

		MaterialTextBox monthBox = new MaterialTextBox();
		monthBox.setPlaceholder("Expiry Month");
		monthBox.setType(InputType.TEXT);
		monthBox.setWidth("30%");
		monthBox.getElement().getStyle().setMarginRight(5, Unit.PCT);

		MaterialTextBox yearBox = new MaterialTextBox();
		yearBox.setPlaceholder("Expiry Year");
		yearBox.setType(InputType.TEXT);
		yearBox.setWidth("30%");
		yearBox.getElement().getStyle().setMarginRight(5, Unit.PCT);

		MaterialTextBox cvcBox = new MaterialTextBox();
		cvcBox.setPlaceholder("CVC");
		cvcBox.setType(InputType.TEXT);
		cvcBox.setWidth("30%");

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
				payButton.setEnabled(false);

				String name = nameBox.getText();
				String cardNumber = cardNumberBox.getText();
				int month = Integer.parseInt(monthBox.getText());
				int expiryYear = Integer.parseInt(yearBox.getText());
				String cvc = cvcBox.getText();

				final Stripe stripe = StripeFactory.get();

				stripe.inject(new Callback<Void, Exception>() {

					@Override
					public void onSuccess(Void result) {
						stripe.setPublishableKey("pk_test_7XjMYfSUJljmavIx7z5zChB0");

						final CreditCard creditCard = new CreditCard.Builder().creditCardNumber(cardNumber).cvc(cvc).expirationMonth(month)
								.expirationYear(expiryYear).name(name).build();

						stripe.getCreditCardToken(creditCard, new CreditCardResponseHandler() {

							@Override
							public void onCreditCardReceived(int status, CreditCardResponse creditCardResponse) {
								ConsumptionPaymentAsync payRpc = (ConsumptionPaymentAsync) GWT.create(ConsumptionPayment.class);
								ServiceDefTarget payTarget = (ServiceDefTarget) payRpc;
								String payUrl = GWT.getModuleBaseURL() + "ConsumptionPaymentImpl";
								payTarget.setServiceEntryPoint(payUrl);

								if (creditCardResponse.getId() != null) {
									String description = previousMonth + " " + year + " Consumption Payment from " + userDetails.getFirstName() + " "
											+ userDetails.getLastName() + " for Apt. Number " + userDetails.getApartmentNumber();

									payRpc.pay(creditCardResponse.getId(), cost, description, previousMonth + " " + year, userDetails,
											new AsyncCallback<Void>() {

												@Override
												public void onFailure(Throwable caught) {
													payButton.setEnabled(true);
													MaterialToast.fireToast(caught.getMessage(), "rounded");

												}

												@Override
												public void onSuccess(Void result) {
													payButton.setEnabled(true);

													modal.close();
													RootPanel.get().remove(modal);

													MaterialToast.fireToast("Consumption costs successfully paid!", "rounded");

													panel.clear();
													panel.add(new ConsumptionWidget(userDetails));
												}
											});
								} else {

								}
							}
						});
					}

					@Override
					public void onFailure(Exception reason) {
						modal.close();
						RootPanel.get().remove(modal);
						Window.alert(reason.getMessage());
					}
				});

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
}
