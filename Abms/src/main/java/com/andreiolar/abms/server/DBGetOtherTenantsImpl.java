package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.andreiolar.abms.client.exception.OtherTenantsNotFoundException;
import com.andreiolar.abms.client.rpc.DBGetOtherTenants;
import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBGetOtherTenantsImpl extends RemoteServiceServlet implements DBGetOtherTenants {

	private static final long serialVersionUID = -53305662480747473L;

	@Override
	public List<UserDetails> getOtherTenants(String username) throws Exception {
		List<UserDetails> otherTenants = new ArrayList<>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "select * from user_info where username != ?";
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

					UserDetails userDetails = new UserDetails(firstName, lastName, date, email, mobileNumber, gender, address, city, country,
							personalNumber, idSeries, username, null, apartmentNumber);
					otherTenants.add(userDetails);
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

		if (otherTenants.isEmpty()) {
			throw new OtherTenantsNotFoundException();
		}

		return otherTenants;
	}

}
