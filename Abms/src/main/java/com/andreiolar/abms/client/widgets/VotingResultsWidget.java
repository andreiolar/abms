package com.andreiolar.abms.client.widgets;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.andreiolar.abms.client.rpc.DBGetFinishedVoteIds;
import com.andreiolar.abms.client.rpc.DBGetFinishedVoteIdsAsync;
import com.andreiolar.abms.client.rpc.DBGetUserVote;
import com.andreiolar.abms.client.rpc.DBGetUserVoteAsync;
import com.andreiolar.abms.client.rpc.DBGetVotingResults;
import com.andreiolar.abms.client.rpc.DBGetVotingResultsAsync;
import com.andreiolar.abms.client.utils.ChartDrawer;
import com.andreiolar.abms.shared.UserInfo;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class VotingResultsWidget extends Composite implements CustomWidget {

	private UserInfo userInfo;

	private boolean all;

	public VotingResultsWidget(UserInfo userInfo, boolean all) {
		this.userInfo = userInfo;
		this.all = all;

		initWidget(initializeWidget());
	}

	@Override
	public Widget initializeWidget() {
		final VerticalPanel panel = new VerticalPanel();
		panel.setSpacing(10);

		DBGetFinishedVoteIdsAsync rpc = (DBGetFinishedVoteIdsAsync) GWT.create(DBGetFinishedVoteIds.class);
		ServiceDefTarget tar = (ServiceDefTarget) rpc;
		String moduleURL = GWT.getModuleBaseURL() + "DBGetFinishedVoteIdsImpl";
		tar.setServiceEntryPoint(moduleURL);

		rpc.getFinishedVoteIds(all, new AsyncCallback<List<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				panel.add(new HTML("<p style=\"font-size:20px\"><b>" + caught.getMessage() + "</b></p>"));

			}

			@Override
			public void onSuccess(List<String> result) {
				HTML text = new HTML();
				text.setHTML(
						"<p>Please select vote ID in order to display results. <br>Obs: In order to display results voting session can't be in progress.</p>");

				panel.add(text);

				final ListBox dropBox = new ListBox();
				dropBox.setMultipleSelect(false);
				dropBox.setSize("75px", "25px");

				for (String voteId : result) {
					dropBox.addItem(voteId);
				}

				panel.add(dropBox);

				final SimplePanel sPanel = new SimplePanel();
				final SimplePanel iPanel = new SimplePanel();

				Button showResultsButton = new Button();
				showResultsButton.setText("Show Results");
				showResultsButton.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {

						DBGetVotingResultsAsync rpc = (DBGetVotingResultsAsync) GWT.create(DBGetVotingResults.class);
						ServiceDefTarget tar = (ServiceDefTarget) rpc;
						String moduleURL = GWT.getModuleBaseURL() + "DBGetVotingResultsImpl";
						tar.setServiceEntryPoint(moduleURL);

						final String voteId = dropBox.getSelectedItemText();

						rpc.getVotingResults(voteId, all, new AsyncCallback<Map<String, Number>>() {

							@Override
							public void onFailure(Throwable caught) {
								panel.add(new HTML("<p style=\"font-size:20px\"><b>" + caught.getMessage() + "</b></p>"));
							}

							@Override
							public void onSuccess(Map<String, Number> result) {

								String[] options = new String[result.size()];
								Number[] results = new Number[result.size()];

								int i = 0;
								Iterator<Entry<String, Number>> it = result.entrySet().iterator();
								while (it.hasNext()) {
									Entry<String, Number> entry = it.next();
									String key = entry.getKey();
									Number value = entry.getValue();

									options[i] = key;
									results[i] = value;

									i++;
								}

								sPanel.clear();

								Widget chart = ChartDrawer.createColumnChart(options, results);
								sPanel.add(chart);

								DBGetUserVoteAsync rpc = (DBGetUserVoteAsync) GWT.create(DBGetUserVote.class);
								ServiceDefTarget tar = (ServiceDefTarget) rpc;
								String moduleURL = GWT.getModuleBaseURL() + "DBGetUserVoteImpl";
								tar.setServiceEntryPoint(moduleURL);

								rpc.getUserVote(userInfo.getUsername(), voteId, new AsyncCallback<String>() {

									@Override
									public void onFailure(Throwable caught) {
										iPanel.clear();
										iPanel.add(new HTML("<p style=\"font-size:20px\"><b>" + caught.getMessage() + "</b></p>"));
									}

									@Override
									public void onSuccess(String result) {
										iPanel.clear();
										iPanel.add(new HTML("<p style=\"font-size:20px\"><b>" + "Your voting option: " + result + "</b></p>"));
									}
								});
							}
						});

					}
				});

				panel.add(showResultsButton);
				panel.add(sPanel);
				panel.setCellHorizontalAlignment(sPanel, HasHorizontalAlignment.ALIGN_CENTER);
				panel.add(iPanel);

			}
		});

		return panel;

	}

}
