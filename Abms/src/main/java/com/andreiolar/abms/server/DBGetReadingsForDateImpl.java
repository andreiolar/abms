package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.andreiolar.abms.client.exception.NoReadingsFoundForDateException;
import com.andreiolar.abms.client.rpc.DBGetReadingsForDate;
import com.andreiolar.abms.shared.SelfReading;
import com.andreiolar.abms.shared.SelfReadingCostWrapper;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBGetReadingsForDateImpl extends RemoteServiceServlet implements DBGetReadingsForDate {

	private static final long serialVersionUID = 1587353385720888600L;

	@Override
	public List<SelfReadingCostWrapper> getReadingsForDate(String date) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<SelfReadingCostWrapper> readings = new ArrayList<>();

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "select sr.*, rc.cost, rc.status from self_readings sr, reading_costs rc where sr.month=? and rc.month=? and sr.apartment_number=rc.apt_number";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, date);
				stmt.setString(2, date);

				rs = stmt.executeQuery();
				while (rs.next()) {
					String aptNumber = rs.getString("apartment_number");
					String coldWater = rs.getString("cold_water");
					String hotWater = rs.getString("hot_water");
					String electricity = rs.getString("electricity");
					String gaz = rs.getString("gaz");
					String cost = rs.getString("cost");
					boolean status = rs.getBoolean("status");

					SelfReading reading = new SelfReading(aptNumber, coldWater, hotWater, electricity, gaz);
					SelfReadingCostWrapper selfReadingCostWrapper = new SelfReadingCostWrapper(reading, cost, status);
					readings.add(selfReadingCostWrapper);
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

		if (readings.isEmpty()) {
			throw new NoReadingsFoundForDateException();
		}

		return readings;
	}
}
