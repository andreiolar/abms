package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.andreiolar.abms.client.rpc.DBSelfReading;
import com.andreiolar.abms.mail.MailSender;
import com.andreiolar.abms.shared.ConsumptionCost;
import com.andreiolar.abms.shared.Cost;
import com.andreiolar.abms.shared.SelfReading;
import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBSelfReadingImpl extends RemoteServiceServlet implements DBSelfReading {

	private static final long serialVersionUID = -8184360144223527661L;

	@Override
	public boolean insertReading(UserDetails userDetails, SelfReading reading) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		int executed = 0;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "insert into self_readings(apartment_number, cold_water, hot_water, electricity, gaz, month) values(?,?,?,?,?,?)";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, userDetails.getApartmentNumber());
				stmt.setString(2, reading.getColdWater());
				stmt.setString(3, reading.getHotWater());
				stmt.setString(4, reading.getElectricity());
				stmt.setString(5, reading.getGaz());
				stmt.setString(6, reading.getDate());

				executed = stmt.executeUpdate();
			} catch (Exception ex) {
				throw new RuntimeException("Something went wrong: " + ex.getMessage(), ex);
			} finally {
				stmt.close();
			}

			try {
				Cost payment = new ConsumptionCost(reading);
				String cost = String.valueOf(payment.getTotalCost());

				String q = "insert into reading_costs(apt_number, cost, month) values(?,?,?)";
				stmt = conn.prepareStatement(q);
				stmt.setInt(1, Integer.parseInt(userDetails.getApartmentNumber()));
				stmt.setString(2, cost);
				stmt.setString(3, reading.getDate());

				executed = stmt.executeUpdate();

				String subject = "Submitted consumption report for " + reading.getDate();
				String to = userDetails.getEmail();
				String message = "<p>" + "Hello " + userDetails.getFirstName() + " " + userDetails.getLastName() + "," + "<br><br>"
						+ "You have successfully submitted your consumption report for " + reading.getDate() + "." + "<br><br>" + "Details:" + "<br>"
						+ "Cold Water: <b>" + reading.getColdWater() + "</b> mc<br>" + "Hot Water: <b>" + reading.getHotWater() + "</b> mc<br>"
						+ "Electricity: <b>" + reading.getElectricity() + "</b> kW<br>" + "Gas: <b>" + reading.getGaz() + "</b> mc<br>" + "<br>"
						+ "Best regards," + "<br>" + "Administration" + "</p>";

				MailSender.sendMail(subject, to, message, null);
			} catch (Exception ex) {
				throw new RuntimeException("Something went wrong: " + ex.getMessage(), ex);
			} finally {
				stmt.close();
			}

		} catch (Exception ex) {
			throw new RuntimeException("Something went wrong: " + ex.getMessage(), ex);
		} finally {
			conn.close();
		}

		if (executed == 0) {
			throw new Exception("Something went wrong: Unable to submit consumption report. Please try again.");
		}

		return true;
	}
}
