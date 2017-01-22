package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.andreiolar.abms.client.exception.InvalidCredentialsException;
import com.andreiolar.abms.client.rpc.DBConnection;
import com.andreiolar.abms.security.BCrypt;
import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBConnectionImpl extends RemoteServiceServlet implements DBConnection {

	private static final long serialVersionUID = 1L;

	public DBConnectionImpl() {
	}

	@Override
	public UserDetails authenticateUser(String username, String password) throws InvalidCredentialsException {
		UserDetails userDetails = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = MyConnection.getConnection();

			String q = "SELECT i.*, u.password, u.type FROM user_info i, users u WHERE i.username=? AND u.username=?";
			stmt = conn.prepareStatement(q);
			stmt.setString(1, username);
			stmt.setString(2, username);
			rs = stmt.executeQuery();

			while (rs.next()) {
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				Date dateOfBirth = rs.getDate("date_of_birth");
				String email = rs.getString("email");
				String mobileNumber = rs.getString("mobile_number");
				String gender = rs.getString("gender");
				String address = rs.getString("address");
				String city = rs.getString("city");
				String country = rs.getString("country");
				String personalNumber = rs.getString("personal_number");
				String idSeries = rs.getString("id_series");
				String apartmentNumber = rs.getString("apartment_number");
				String pass = rs.getString("password");
				String type = rs.getString("type");

				if (BCrypt.checkpw(password, pass)) {
					userDetails = new UserDetails(firstName, lastName, dateOfBirth, email, mobileNumber, gender, address, city, country,
							personalNumber, idSeries, username, password, apartmentNumber);
					userDetails.setType(type);

					// HttpServletRequest threadLocalRequest = this.getThreadLocalRequest();
					// HttpSession session = threadLocalRequest.getSession();
					// session.setAttribute("user", user);
				}
			}

		} catch (Exception ex) {
			throw new RuntimeException("Something went wrong: " + ex.getMessage(), ex);
		} finally {
			try {
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
			}
		}

		if (userDetails == null) {
			throw new InvalidCredentialsException("Login failed! Wrong username or password!");
		}

		return userDetails;
	}

}
