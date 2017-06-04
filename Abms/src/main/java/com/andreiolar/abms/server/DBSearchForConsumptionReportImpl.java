package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.andreiolar.abms.client.exception.ConsumptionReportNotFoundException;
import com.andreiolar.abms.client.rpc.DBSearchForConsumptionReport;
import com.andreiolar.abms.shared.ConsumptionCostReport;
import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBSearchForConsumptionReportImpl extends RemoteServiceServlet implements DBSearchForConsumptionReport {

	private static final long serialVersionUID = 3882952232018953915L;

	@Override
	public ConsumptionCostReport searchForConsumptionReport(UserDetails userDetails, String date) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		ConsumptionCostReport reading = null;

		boolean submitted = false;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "select sr.electricity, sr.gaz, rc.cost, rc.status from self_readings sr, reading_costs rc where (sr.month=? and rc.month=?) and (sr.apartment_number=? and rc.apt_number=?);";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, date);
				stmt.setString(2, date);
				stmt.setInt(3, Integer.parseInt(userDetails.getApartmentNumber()));
				stmt.setInt(4, Integer.parseInt(userDetails.getApartmentNumber()));

				rs = stmt.executeQuery();

				if (rs.next()) {
					submitted = true;

					String electricity = rs.getString("electricity");
					String gas = rs.getString("gaz");
					String cost = rs.getString("cost");
					boolean status = rs.getBoolean("status");

					reading = new ConsumptionCostReport(electricity, gas, cost, status);
				}
			} catch (Exception ex) {
				throw new RuntimeException("Something went wrong: " + ex.getMessage(), ex);
			} finally {
				rs.close();
				stmt.close();
			}
		} catch (Exception ex) {
			throw new RuntimeException("Something went wrong: " + ex.getMessage(), ex);
		} finally {
			conn.close();
		}

		if (!submitted) {
			throw new ConsumptionReportNotFoundException();
		}

		return reading;
	}
}
