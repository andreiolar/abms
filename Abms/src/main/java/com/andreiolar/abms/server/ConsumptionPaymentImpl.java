package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.andreiolar.abms.client.rpc.ConsumptionPayment;
import com.andreiolar.abms.mail.MailSender;
import com.andreiolar.abms.properties.PropertiesReader;
import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.stripe.Stripe;
import com.stripe.model.Charge;

public class ConsumptionPaymentImpl extends RemoteServiceServlet implements ConsumptionPayment {

	private static final long serialVersionUID = 8486455797914030412L;

	@Override
	public void pay(String token, String amount, String description, String month, UserDetails userDetails) throws Exception {
		PropertiesReader reader = new PropertiesReader();

		Stripe.apiKey = reader.readProperty("stripe.properties", "api_secret");

		Map<String, Object> chargeParams = new HashMap<String, Object>();

		Double d = Double.valueOf(amount) * 100;
		String billed = String.valueOf(d.intValue());

		chargeParams.put("amount", billed);
		chargeParams.put("currency", "ron");
		chargeParams.put("description", description);
		chargeParams.put("source", token);

		Charge created = Charge.create(chargeParams);

		if (created.getId() != null) {
			// Update DB
			Connection conn = null;
			PreparedStatement stmt = null;
			int executed = 0;

			try {
				conn = MyConnection.getConnection();

				try {
					String q = "update reading_costs set status=true where apt_number=? and month=?";
					stmt = conn.prepareStatement(q);
					stmt.setInt(1, Integer.parseInt(userDetails.getApartmentNumber()));
					stmt.setString(2, month);

					executed = stmt.executeUpdate();
				} catch (Exception e) {
					throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
				} finally {
					stmt.close();
				}
			} catch (Exception e) {
				throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
			} finally {
				conn.close();
			}

			if (executed > 0) {
				String subject = "Consumption costs for " + month + " successfully paid";
				String to = userDetails.getEmail();
				String message = "<p>" + "Hello " + userDetails.getFirstName() + " " + userDetails.getLastName() + "," + "<br><br>"
						+ "You have successfully paid the consumption costs for " + month + ".<br><br>" + "<b>Amount Paid: </b>" + amount
						+ " RON<br><br>" + "<u><b>Issued For:</b></u><br>" + "<b>Username: </b>" + userDetails.getUsername() + "<br>"
						+ "<b>Email: </b> " + userDetails.getEmail() + "<br>" + "<b>First Name: </b>" + userDetails.getFirstName() + "<br>"
						+ "<b>Last Name: </b>" + userDetails.getLastName() + "<br>" + "<b>Country: </b>" + userDetails.getCountry() + "<br><br>"
						+ "If you need to contact support regarding this transaction, please have the following information ready: <br>"
						+ "<b>Date: </b>" + new Date(created.getCreated() * 1000) + "<br>" + "<b>Transaction ID: </b>" + created.getId() + "<br><br>"
						+ "Thank You," + "<br><br>" + "Administration";

				MailSender.sendMail(subject, to, message, null);
			}

		} else {
			System.out.println("Error");
		}
	}
}
