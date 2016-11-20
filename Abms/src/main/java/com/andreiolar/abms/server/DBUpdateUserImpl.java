package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.andreiolar.abms.client.rpc.DBUpdateUser;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBUpdateUserImpl extends RemoteServiceServlet implements DBUpdateUser {

	private static final long serialVersionUID = 6703785033635623808L;

	@Override
	public Boolean updateUser(String firstName, String lastName, String email, String mobileNumber, String address, String city, String country,
			String idSeries, String personalNumber) throws Exception {

		Boolean result = new Boolean(false);
		int success = 0;
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "update user_info set first_name=?, last_name=?, email=?, mobile_number=?, address=?, city=?, country=?, id_series=? where personal_number=?";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, firstName);
				stmt.setString(2, lastName);
				stmt.setString(3, email);
				stmt.setString(4, mobileNumber);
				stmt.setString(5, address);
				stmt.setString(6, city);
				stmt.setString(7, country);
				stmt.setString(8, idSeries);
				stmt.setString(9, personalNumber);

				success = stmt.executeUpdate();
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				stmt.close();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			conn.close();
		}

		if (success > 0) {
			result = new Boolean(true);
		}

		if (result == null || result.booleanValue() == false) {
			throw new Exception("Error updating user information!");
		}

		return result;
	}

}
