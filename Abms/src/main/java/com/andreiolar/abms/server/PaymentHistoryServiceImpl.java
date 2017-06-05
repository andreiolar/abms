package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.andreiolar.abms.client.rpc.PaymentHistoryService;
import com.andreiolar.abms.shared.PaymentEntry;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class PaymentHistoryServiceImpl extends RemoteServiceServlet implements PaymentHistoryService {

	private static final long serialVersionUID = 8427131241492800764L;

	@Override
	public List<PaymentEntry> listPaymentEntries(String aptNumber) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<PaymentEntry> paymentEntries = new ArrayList<PaymentEntry>();

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "select * from user_transactions where apt_number=?";
				stmt = conn.prepareStatement(q);
				stmt.setInt(1, Integer.parseInt(aptNumber));

				rs = stmt.executeQuery();

				while (rs.next()) {
					String transactionId = rs.getString("transaction_id");
					String description = rs.getString("description");
					String cost = rs.getString("cost");
					String date = rs.getString("date");

					PaymentEntry paymentEntry = new PaymentEntry(transactionId, description, cost, date);
					paymentEntries.add(paymentEntry);
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

		return paymentEntries;
	}
}
