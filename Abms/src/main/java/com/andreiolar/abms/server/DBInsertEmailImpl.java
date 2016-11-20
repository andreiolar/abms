package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.andreiolar.abms.client.rpc.DBInsertEmail;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBInsertEmailImpl extends RemoteServiceServlet implements DBInsertEmail {

	private static final long serialVersionUID = -4365048722221945570L;

	@SuppressWarnings("resource")
	@Override
	public Boolean insertEmail(String email, String aptNumber) throws Exception {
		Boolean result = new Boolean(false);
		int execute = 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "select apartment_number from user_info where apartment_number=?";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, aptNumber);

				rs = stmt.executeQuery();

				if (!rs.next()) {
					String q2 = "select * from email_for_registration where apartment_number=?";
					stmt = conn.prepareStatement(q2);
					stmt.setString(1, aptNumber);

					rs = stmt.executeQuery();

					if (!rs.next()) {
						String q3 = "insert into email_for_registration(email, apartment_number) values(?,?)";
						stmt = conn.prepareStatement(q3);
						stmt.setString(1, email);
						stmt.setString(2, aptNumber);

						execute = stmt.executeUpdate();
					} else {
						result = null;
					}

				} else {
					result = null;
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

		if (result == null) {
			throw new Exception("Apartment Number: " + aptNumber + " is already occupied!");
		}

		if (execute > 0) {
			result = new Boolean(true);
		}

		if (result.booleanValue() == false) {
			throw new Exception("Error inserting E-Mail address and Apartment Number into DB!");
		}

		return result;
	}

}
