package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.andreiolar.abms.client.rpc.DBGetUserInfo;
import com.andreiolar.abms.shared.UserInfo;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBGetUserInfoImpl extends RemoteServiceServlet implements DBGetUserInfo {

	private static final long serialVersionUID = 1L;

	public DBGetUserInfoImpl() {
	}

	@Override
	public UserInfo getUserInfo(String username) throws Exception {
		UserInfo userInfo = null;
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

					userInfo = new UserInfo(firstName, lastName, date, email, mobileNumber, gender, address, city, country, personalNumber, idSeries,
							username, null, apartmentNumber);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			rs.close();
			stmt.close();
			conn.close();
		}

		if (userInfo == null) {
			throw new Exception("Unable to get user information for username: " + username + ". Plese check if the user exists.");
		}

		return userInfo;
	}

}
