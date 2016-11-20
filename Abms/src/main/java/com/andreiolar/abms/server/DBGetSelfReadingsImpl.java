package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.andreiolar.abms.client.rpc.DBGetSelfReadings;
import com.andreiolar.abms.shared.SelfReading;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBGetSelfReadingsImpl extends RemoteServiceServlet implements DBGetSelfReadings {

	private static final long serialVersionUID = -7399607655056279755L;

	@Override
	public List<SelfReading> getSelfReadings(String date) throws Exception {

		List<SelfReading> selfReadings = new ArrayList<SelfReading>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

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

					SelfReading selfReading = new SelfReading(aptNumber, coldWater, hotWater, electricity, gaz, null);
					selfReadings.add(selfReading);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				rs.close();
				stmt.close();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			conn.close();
		}

		if (selfReadings.isEmpty()) {
			throw new Exception("No entries found for month: " + date);
		}

		return selfReadings;
	}

}
