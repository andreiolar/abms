package com.andreiolar.abms.client.utils;

import org.moxieapps.gwt.highcharts.client.Chart;
import org.moxieapps.gwt.highcharts.client.Legend;
import org.moxieapps.gwt.highcharts.client.Series;
import org.moxieapps.gwt.highcharts.client.Style;
import org.moxieapps.gwt.highcharts.client.ToolTip;
import org.moxieapps.gwt.highcharts.client.ToolTipData;
import org.moxieapps.gwt.highcharts.client.ToolTipFormatter;
import org.moxieapps.gwt.highcharts.client.labels.DataLabels;
import org.moxieapps.gwt.highcharts.client.labels.DataLabelsData;
import org.moxieapps.gwt.highcharts.client.labels.DataLabelsFormatter;
import org.moxieapps.gwt.highcharts.client.labels.Labels;
import org.moxieapps.gwt.highcharts.client.labels.XAxisLabels;
import org.moxieapps.gwt.highcharts.client.plotOptions.ColumnPlotOptions;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Widget;

public final class ChartDrawer {

	public static Widget createColumnChart(String categories[], Number results[]) {

		final Chart chart = new Chart();

		chart.setType(Series.Type.COLUMN);
		chart.setMargin(50, 50, 100, 80);
		chart.setChartTitleText("Voting session results");
		chart.setLegend(new Legend().setEnabled(false));
		chart.setSize(1500, 550, true);
		chart.setToolTip(new ToolTip().setFormatter(new ToolTipFormatter() {

			@Override
			public String format(ToolTipData toolTipData) {
				return "<b>" + toolTipData.getXAsString() + "</b><br/>" + "Number of votes: "
						+ NumberFormat.getFormat("0").format(toolTipData.getYAsDouble()) + " votes";
			}
		}));

		chart.getXAxis().setCategories(categories).setLabels(
				new XAxisLabels().setRotation(-45).setAlign(Labels.Align.RIGHT).setStyle(new Style().setFont("normal 13px Verdana, sans-serif")));

		chart.getYAxis().setAxisTitleText("Votes").setMin(0);
		chart.addSeries(chart.createSeries().setName("Votes").setPoints(results)
				.setPlotOptions(new ColumnPlotOptions().setDataLabels(new DataLabels().setEnabled(true).setRotation(-90).setColor("#FFFFFF")
						.setAlign(Labels.Align.RIGHT).setX(-3).setY(10).setFormatter(new DataLabelsFormatter() {

							@Override
							public String format(DataLabelsData dataLabelsData) {
								return NumberFormat.getFormat("0").format(dataLabelsData.getYAsDouble());
							}
						}).setStyle(new Style().setFont("normal 13px Verdana, sans-serif")))));

		return chart;
	}
}
