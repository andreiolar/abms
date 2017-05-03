package com.andreiolar.abms.client.widgets;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.andreiolar.abms.client.exception.NoFinishedVotingSessionFound;
import com.andreiolar.abms.client.rpc.DBGetVoteSessions;
import com.andreiolar.abms.client.rpc.DBGetVoteSessionsAsync;
import com.andreiolar.abms.client.utils.ChartDrawer;
import com.andreiolar.abms.shared.FinishedVoteSession;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLoader;
import gwt.material.design.client.ui.MaterialModal;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Hr;
import gwt.material.design.client.ui.html.Option;

public class VoteResultsWidget extends Composite implements CustomWidget {

	private boolean showActiveVotes;

	public VoteResultsWidget(boolean showActiveVotes) {
		this.showActiveVotes = showActiveVotes;

		initWidget(initializeWidget());
	}

	@Override
	public Widget initializeWidget() {
		MaterialPanel panel = new MaterialPanel();

		MaterialLabel title = new MaterialLabel("View Vote Results");
		title.setTextColor(Color.BLUE);
		title.setTextAlign(TextAlign.CENTER);
		title.setFontSize("36px");
		title.setFontWeight(FontWeight.BOLD);
		panel.add(title);

		panel.add(new Hr());

		MaterialLabel label = new MaterialLabel();
		label.setText("Vote results for finished voting sessions can be seen here by selecting the desired voting session from the drop-down below.");
		label.setMarginTop(25.0);
		label.setMarginLeft(25.0);
		label.setFontSize("18px");
		panel.add(label);

		DBGetVoteSessionsAsync rpc = (DBGetVoteSessionsAsync) GWT.create(DBGetVoteSessions.class);
		ServiceDefTarget tar = (ServiceDefTarget) rpc;
		String moduleURL = GWT.getModuleBaseURL() + "DBGetVoteSessionsImpl";
		tar.setServiceEntryPoint(moduleURL);

		MaterialLoader.showLoading(true);

		rpc.getVoteSessions(showActiveVotes, new AsyncCallback<Map<String, FinishedVoteSession>>() {

			@Override
			public void onSuccess(Map<String, FinishedVoteSession> result) {
				MaterialLoader.showLoading(false);

				MaterialComboBox<String> finishedVotesBox = new MaterialComboBox<String>();
				finishedVotesBox.setPlaceholder("Finished Vote Session");
				finishedVotesBox.add(new Option("Select a Vote Session"));

				Iterator<Entry<String, FinishedVoteSession>> iterator = result.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<String, FinishedVoteSession> next = iterator.next();
					String voteId = next.getKey();
					FinishedVoteSession voteSession = next.getValue();
					Option option = new Option(voteId + ". " + voteSession.getTitle());
					finishedVotesBox.add(option);
				}

				finishedVotesBox.setSelectedIndex(0);
				finishedVotesBox.setStyleName("comboboxInst");
				finishedVotesBox.setTextAlign(TextAlign.CENTER);

				MaterialPanel descriptionPanel = new MaterialPanel();
				MaterialPanel chartPanel = new MaterialPanel();

				finishedVotesBox.addSelectionHandler(new SelectionHandler<String>() {

					@Override
					public void onSelection(SelectionEvent<String> event) {
						String selectedValue = finishedVotesBox.getSelectedValue();

						if (selectedValue.equals("Select a Vote Session")) {
							// Do nothing
						} else {
							String key = selectedValue.substring(0, selectedValue.indexOf("."));
							FinishedVoteSession finishedVoteSession = result.get(key);

							if (finishedVoteSession != null) {
								descriptionPanel.clear();
								chartPanel.clear();

								descriptionPanel.setShadow(2);
								descriptionPanel.setStyleName("vote-description-panel");

								MaterialLabel title = new MaterialLabel();
								title.setText(finishedVoteSession.getTitle());
								title.setMarginTop(25.0);
								title.setMarginLeft(25.0);
								title.setTextColor(Color.BLUE);
								title.setFontSize("18px");
								descriptionPanel.add(title);

								MaterialLabel description = new MaterialLabel();
								description.setText(finishedVoteSession.getDescription());
								description.setMarginTop(50.0);
								description.setMarginLeft(25.0);
								description.setPaddingBottom(25.0);
								descriptionPanel.add(description);

								panel.add(descriptionPanel);

								chartPanel.setStyleName("chart-panel");
								chartPanel.setMarginTop(50.0);
								chartPanel.setMarginBottom(100.0);

								Map<String, Number> voteResults = finishedVoteSession.getResults();
								String[] options = new String[result.size()];
								Number[] numbers = new Number[result.size()];

								int i = 0;
								Iterator<Entry<String, Number>> it = voteResults.entrySet().iterator();
								while (it.hasNext()) {
									Entry<String, Number> entry = it.next();
									String vote_option = entry.getKey();
									Number value = entry.getValue();

									options[i] = vote_option;
									numbers[i] = value;

									i++;
								}

								Widget columnChart = ChartDrawer.createColumnChart(options, numbers);
								chartPanel.add(columnChart);

								panel.add(chartPanel);
							}
						}
					}
				});

				panel.add(finishedVotesBox);

			}

			@Override
			public void onFailure(Throwable caught) {
				MaterialLoader.showLoading(false);
				if (caught instanceof NoFinishedVotingSessionFound) {
					MaterialPanel descriptionPanel = new MaterialPanel();
					descriptionPanel.setShadow(2);
					descriptionPanel.setStyleName("vote-description-panel");

					MaterialLabel description = new MaterialLabel();
					description.setText("Sorry, no finished voting sessions until this point. Please try again later.");
					description.setFontSize("18px");
					description.setMarginTop(25.0);
					description.setMarginLeft(25.0);
					description.setPaddingBottom(25.0);
					descriptionPanel.add(description);

					panel.add(descriptionPanel);
				} else {
					MaterialModal errorModal = ModalCreator.createModal(caught);
					RootPanel.get().add(errorModal);
					errorModal.open();
				}
			}
		});

		return panel;
	}

}
