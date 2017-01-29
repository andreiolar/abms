package com.andreiolar.abms.client.widgets;

import java.util.Date;

import com.andreiolar.abms.client.utils.DateUtil;
import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.addins.client.stepper.MaterialStep;
import gwt.material.design.addins.client.stepper.MaterialStepper;
import gwt.material.design.client.constants.ButtonType;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLoader;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
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
		// stepper.setAxis(Axis.HORIZONTAL);
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

					// TODO

					MaterialLoader.showLoading(true);

					panel.clear();
					panel.add(new ConsumptionWidget(userDetails));
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

		// Else

		return panel;
	}
}
