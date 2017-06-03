package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.andreiolar.abms.client.exception.NoUpkeepReportsFoundForDateException;
import com.andreiolar.abms.client.rpc.RetreiveUpkeepCostReports;
import com.andreiolar.abms.shared.UpkeepCostReport;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class RetreiveUpkeepCostReportsImpl extends RemoteServiceServlet implements RetreiveUpkeepCostReports {

	private static final long serialVersionUID = -8282859036131415587L;

	@Override
	public List<UpkeepCostReport> retreiveUpkeepCostReports(String date) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<UpkeepCostReport> reports = new ArrayList<UpkeepCostReport>();

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "select pui.aptNumber, pui.costTotal, uc.status from personal_upkeep_information pui, upkeep_costs uc where pui.luna=? and uc.month=? and pui.aptNumber=uc.apt_number";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, date);
				stmt.setString(2, date);

				rs = stmt.executeQuery();

				while (rs.next()) {
					String aptNumber = rs.getString("aptNumber");
					String totalCost = rs.getString("costTotal");
					boolean status = rs.getBoolean("status");

					UpkeepCostReport upkeepCostReport = new UpkeepCostReport(Integer.parseInt(aptNumber), totalCost, status);
					reports.add(upkeepCostReport);
				}

			} catch (Exception e) {
				throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
			} finally {
				rs.close();
				stmt.close();
			}
		} catch (Exception e) {
			throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
		} finally {
			conn.close();
		}

		if (reports.isEmpty()) {
			throw new NoUpkeepReportsFoundForDateException();
		}

		return reports;
	}

}
