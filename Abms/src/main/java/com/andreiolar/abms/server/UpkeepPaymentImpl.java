package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.andreiolar.abms.client.exception.ClientCardException;
import com.andreiolar.abms.client.rpc.UpkeepPayment;
import com.andreiolar.abms.mail.MailSender;
import com.andreiolar.abms.properties.PropertiesReader;
import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.stripe.Stripe;
import com.stripe.exception.CardException;
import com.stripe.model.Charge;

public class UpkeepPaymentImpl extends RemoteServiceServlet implements UpkeepPayment {

	private static final long serialVersionUID = -7771546574706468738L;

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

		Charge created = null;

		try {
			created = Charge.create(chargeParams);
		} catch (CardException ce) {
			throw new ClientCardException(ce.getCode());
		}

		if (created.getId() != null) {
			// Update DB
			Connection conn = null;
			PreparedStatement stmt = null;
			int executed = 0;

			Date creationDate = new Date(created.getCreated() * 1000);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

			try {
				conn = MyConnection.getConnection();

				try {
					String q = "update upkeep_costs set status=true where apt_number=? and month=?";
					stmt = conn.prepareStatement(q);
					stmt.setInt(1, Integer.parseInt(userDetails.getApartmentNumber()));
					stmt.setString(2, month);

					executed = stmt.executeUpdate();
				} catch (Exception e) {
					throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
				} finally {
					stmt.close();
				}

				try {
					String q = "insert into user_transactions(transaction_id, apt_number, description, cost, date) values(?,?,?,?,?)";
					stmt = conn.prepareStatement(q);
					stmt.setString(1, created.getId());
					stmt.setInt(2, Integer.parseInt(userDetails.getApartmentNumber()));
					stmt.setString(3, "Upkeep Payment for " + month);
					stmt.setString(4, amount);
					stmt.setString(5, sdf.format(creationDate));

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
				String subject = "Upkeep costs for " + month + " successfully paid";
				String to = userDetails.getEmail();
				String message = "<p>" + "Hello " + userDetails.getFirstName() + " " + userDetails.getLastName() + "," + "<br><br>"
						+ "You have successfully paid the upkeep costs for " + month + ".<br><br>" + "<b>Amount Paid: </b>" + amount + " RON<br><br>"
						+ "<u><b>Issued For:</b></u><br>" + "<b>Username: </b>" + userDetails.getUsername() + "<br>" + "<b>Email: </b> "
						+ userDetails.getEmail() + "<br>" + "<b>First Name: </b>" + userDetails.getFirstName() + "<br>" + "<b>Last Name: </b>"
						+ userDetails.getLastName() + "<br>" + "<b>Country: </b>" + userDetails.getCountry() + "<br><br>"
						+ "If you need to contact support regarding this transaction, please have the following information ready: <br>"
						+ "<b>Date: </b>" + creationDate + "<br>" + "<b>Transaction ID: </b>" + created.getId() + "<br><br>" + "Thank You,"
						+ "<br><br>" + "Administration";

				MailSender.sendMail(subject, to, message, null);
			}

		} else {
			System.out.println("Error");
		}
	}

}
