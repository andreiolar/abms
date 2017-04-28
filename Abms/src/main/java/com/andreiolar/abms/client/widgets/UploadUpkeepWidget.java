package com.andreiolar.abms.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.addins.client.fileuploader.MaterialFileUploader;
import gwt.material.design.addins.client.fileuploader.MaterialUploadLabel;
import gwt.material.design.addins.client.fileuploader.base.UploadFile;
import gwt.material.design.addins.client.fileuploader.events.ErrorEvent;
import gwt.material.design.addins.client.fileuploader.events.ErrorEvent.ErrorHandler;
import gwt.material.design.addins.client.fileuploader.events.SuccessEvent;
import gwt.material.design.addins.client.fileuploader.events.SuccessEvent.SuccessHandler;
import gwt.material.design.addins.client.stepper.MaterialStep;
import gwt.material.design.addins.client.stepper.MaterialStepper;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.client.ui.html.Hr;

public class UploadUpkeepWidget extends Composite implements CustomWidget {

	public UploadUpkeepWidget() {
		initWidget(initializeWidget());
	}

	@Override
	public Widget initializeWidget() {
		MaterialPanel panel = new MaterialPanel();

		MaterialLabel title = new MaterialLabel("Upload Upkeep Report");
		title.setTextColor(Color.BLUE);
		title.setTextAlign(TextAlign.CENTER);
		title.setFontSize("36px");
		title.setFontWeight(FontWeight.BOLD);
		panel.add(title);

		panel.add(new Hr());

		MaterialStepper stepper = new MaterialStepper();
		stepper.setMarginTop(25.0);

		MaterialStep stepOne = new MaterialStep();
		MaterialStep stepTwo = new MaterialStep();
		stepTwo.setEnabled(false);
		MaterialStep stepThree = new MaterialStep();
		stepThree.setEnabled(false);

		// Step 1 Excel
		stepOne.setStep(1);
		stepOne.setTitle("Excel File Upload");
		stepOne.setDescription("Please select the Excel File you want to upload.");

		MaterialPanel stepOnePanel = new MaterialPanel();
		stepOnePanel.setWidth("100%");

		MaterialFileUploader excelFileUploader = new MaterialFileUploader();
		excelFileUploader.setUrl(GWT.getModuleBaseURL() + "excelUploader");
		excelFileUploader.setMaxFileSize(10);
		excelFileUploader.setShadow(1);
		excelFileUploader.setAcceptedFiles(".xlsx");

		excelFileUploader.addSuccessHandler(new SuccessHandler<UploadFile>() {

			@Override
			public void onSuccess(SuccessEvent<UploadFile> event) {
				stepper.setSuccess("");
				stepTwo.setEnabled(true);
				stepper.nextStep();
				stepOne.setEnabled(false);
			}
		});

		excelFileUploader.addErrorHandler(new ErrorHandler<UploadFile>() {

			@Override
			public void onError(ErrorEvent<UploadFile> event) {
				MaterialToast.fireToast("Error uploading Excel file: " + event.getResponse().getCode() + ": " + event.getResponse().getMessage(),
						"rounded");
			}
		});

		MaterialUploadLabel excelLabel = new MaterialUploadLabel("Excel File Uploader", "Selcect or drag and drop files to upload.");
		excelFileUploader.add(excelLabel);

		stepOnePanel.add(excelFileUploader);
		stepOne.add(stepOnePanel);

		// Step 2 PDF
		stepTwo.setStep(2);
		stepTwo.setTitle("PDF File Upload");
		stepTwo.setDescription("Please select the PDF File you want to upload.");

		MaterialPanel stepTwoPanel = new MaterialPanel();
		stepTwoPanel.setWidth("100%");

		MaterialFileUploader pdfFileUploader = new MaterialFileUploader();
		pdfFileUploader.setUrl(GWT.getModuleBaseURL() + "uploader?type=upkeep&extension=pdf");
		pdfFileUploader.setMaxFileSize(10);
		pdfFileUploader.setShadow(1);
		pdfFileUploader.setAcceptedFiles(".pdf");

		pdfFileUploader.addSuccessHandler(new SuccessHandler<UploadFile>() {

			@Override
			public void onSuccess(SuccessEvent<UploadFile> event) {
				stepper.setSuccess("");
				stepThree.setEnabled(true);
				stepper.nextStep();
				stepTwo.setEnabled(false);
			}
		});

		pdfFileUploader.addErrorHandler(new ErrorHandler<UploadFile>() {

			@Override
			public void onError(ErrorEvent<UploadFile> event) {
				MaterialToast.fireToast("Error uploading PDF file: " + event.getResponse().getCode() + ": " + event.getResponse().getMessage(),
						"rounded");
			}
		});

		MaterialUploadLabel pdfLabel = new MaterialUploadLabel("PDF File Uploader", "Selcect or drag and drop files to upload.");
		pdfFileUploader.add(pdfLabel);

		stepTwoPanel.add(pdfFileUploader);
		stepTwo.add(stepTwoPanel);

		// Step 2 ZIP
		stepThree.setStep(3);
		stepThree.setTitle("ZIP Upload");
		stepThree.setDescription("Please select the ZIP Archive you want to upload.");

		MaterialPanel stepThreePanel = new MaterialPanel();
		stepThreePanel.setWidth("100%");

		MaterialFileUploader zipFileUploader = new MaterialFileUploader();
		zipFileUploader.setUrl(GWT.getModuleBaseURL() + "uploader?type=upkeep&extension=zip");
		zipFileUploader.setMaxFileSize(10);
		zipFileUploader.setShadow(1);
		zipFileUploader.setAcceptedFiles(".zip");

		zipFileUploader.addSuccessHandler(new SuccessHandler<UploadFile>() {

			@Override
			public void onSuccess(SuccessEvent<UploadFile> event) {
				stepper.setSuccess("");
				stepThree.setEnabled(false);
			}
		});

		zipFileUploader.addErrorHandler(new ErrorHandler<UploadFile>() {

			@Override
			public void onError(ErrorEvent<UploadFile> event) {
				MaterialToast.fireToast("Error uploading ZIP archive: " + event.getResponse().getCode() + ": " + event.getResponse().getMessage(),
						"rounded");
			}
		});

		MaterialUploadLabel zipLabel = new MaterialUploadLabel("ZIP Archive Uploader", "Selcect or drag and drop files to upload.");
		zipFileUploader.add(zipLabel);

		stepThreePanel.add(zipFileUploader);
		stepThree.add(stepThreePanel);

		stepper.add(stepOne);
		stepper.add(stepTwo);
		stepper.add(stepThree);

		panel.add(stepper);

		return panel;
	}

}
