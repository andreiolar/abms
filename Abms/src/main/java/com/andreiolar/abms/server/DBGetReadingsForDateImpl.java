package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.andreiolar.abms.client.exception.NoReadingsFoundForDateException;
import com.andreiolar.abms.client.rpc.DBGetReadingsForDate;
import com.andreiolar.abms.shared.SelfReading;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBGetReadingsForDateImpl extends RemoteServiceServlet implements DBGetReadingsForDate {

	private static final long serialVersionUID = 1587353385720888600L;

	@Override
	public List<SelfReading> getReadingsForDate(String date) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<SelfReading> readings = new ArrayList<>();

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "select * from self_readings where month=?";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, date);

				rs = stmt.executeQuery();
				while (rs.next()) {
					String aptNumber = rs.getString("apartment_number");
					String coldWater = rs.getString("cold_water");
					String hotWater = rs.getString("hot_water");
					String electricity = rs.getString("electricity");
					String gaz = rs.getString("gaz");

					SelfReading reading = new SelfReading(aptNumber, coldWater, hotWater, electricity, gaz);
					readings.add(reading);
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
