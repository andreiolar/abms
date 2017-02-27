package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.andreiolar.abms.client.exception.UserDetailsNotFoundException;
import com.andreiolar.abms.client.rpc.DBGetUserDetails;
import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBGetUserDetailsImpl extends RemoteServiceServlet implements DBGetUserDetails {

	private static final long serialVersionUID = 4301036655548785807L;

	@Override
	public UserDetails geUserDetails(String username) throws Exception {
		UserDetails userDetails = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "select * from user_info where username=?";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, username);
				rs = stmt.executeQuery();

				while (rs.next()) {
					String firstName = rs.getString("first_name");
					String lastName = rs.getString("last_name");
					Date date = rs.getDate("date_of_birth");
					String email = rs.getString("email");
					String mobileNumber = rs.getString("mobile_number");
					String gender = rs.getString("gender");
					String address = rs.getString("address");
					String city = rs.getString("city");
					String country = rs.getString("country");
					String personalNumber = rs.getString("personal_number");
					String idSeries = rs.getString("id_series");
					String apartmentNumber = rs.getString("apartment_number");

					userDetails = new UserDetails(firstName, lastName, date, email, mobileNumber, gender, address, city, country, personalNumber,
							idSeries, username, null, apartmentNumber);
				}

			} catch (Exception e) {
				throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
			}
		} catch (SQLException e) {
			throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
		} finally {
			rs.close();
			stmt.close();
			conn.close();
		}

		if (userDetails == null) {
			throw new UserDetailsNotFoundException("Unable to get user information for username: " + username + ". Plese check if the user exists.");
		}

		return userDetails;
	}

}
