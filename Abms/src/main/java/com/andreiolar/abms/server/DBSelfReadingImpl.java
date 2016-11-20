package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.andreiolar.abms.client.rpc.DBSelfReading;
import com.andreiolar.abms.mail.MailSender;
import com.andreiolar.abms.shared.SelfReading;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBSelfReadingImpl extends RemoteServiceServlet implements DBSelfReading {

	private static final long serialVersionUID = -8184360144223527661L;

	@Override
	public Boolean insertReading(String usernmae, SelfReading reading) throws Exception {

		int result = 0;
		Boolean bool = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		boolean alreadyInserted = false;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "select * from self_readings where apartment_number=(select apartment_number from user_info where username=?) and month=?";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, usernmae);
				stmt.setString(2, reading.getMonth());

				rs = stmt.executeQuery();

				if (rs.next()) {
					alreadyInserted = true;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				rs.close();
				stmt.close();
			}

			if (!alreadyInserted) {
				try {
					String q = "insert into self_readings(apartment_number, cold_water, hot_water, electricity, gaz, month) values((select apartment_number from user_info where username='"
							+ usernmae + "'),?,?,?,?,?)";
					stmt = conn.prepareStatement(q);
					stmt.setString(1, reading.getColdWater());
					stmt.setString(2, reading.getHotWater());
					stmt.setString(3, reading.getElectricity());
					stmt.setString(4, reading.getGaz());
					stmt.setString(5, reading.getMonth());

					result = stmt.executeUpdate();

					String q2 = "select email, first_name, last_name from user_info where username=?";
					stmt = conn.prepareStatement(q2);
					stmt.setString(1, usernmae);

					rs = stmt.executeQuery();

					if (rs.next()) {
						String email = rs.getString("email");
						String firstName = rs.getString("first_name");
						String lastName = rs.getString("last_name");

						String subject = "Submitted reading report for month " + reading.getMonth();
						String to = email;
						String message = "<p>" + "Hello " + firstName + " " + lastName + "," + "<br><br>"
								+ "You have successfully submitted yor reading report for month " + reading.getMonth() + "." + "<br><br>" + "Details:"
								+ "<br>" + "Cold Water: <b>" + reading.getColdWater() + "</b> mc<br>" + "Hot Water: <b>" + reading.getHotWater()
								+ "</b> mc<br>" + "Electricity: <b>" + reading.getElectricity() + "</b> kW<br>" + "Gaz: <b>" + reading.getGaz()
								+ "</b> mc<br>" + "<br>" + "Best regards," + "<br>" + "Administration" + "</p>";

						MailSender.sendMail(subject, to, message, null);
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					stmt.close();
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			conn.close();
		}

		if (result > 0) {
			bool = new Boolean(true);
		}

		if (alreadyInserted == true) {
			throw new Exception("Reading already submitted for month: " + reading.getMonth().split("-")[0]);
		}

		if (bool == null) {
			throw new Exception("Error inserting reading into DB!");
		}

		return bool;
	}

}
