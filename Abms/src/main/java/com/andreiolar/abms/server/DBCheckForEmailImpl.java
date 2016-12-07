package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.andreiolar.abms.client.exception.EmailNotFoundException;
import com.andreiolar.abms.client.rpc.DBCheckForEmail;
import com.andreiolar.abms.shared.Email;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBCheckForEmailImpl extends RemoteServiceServlet implements DBCheckForEmail {

	private static final long serialVersionUID = 1L;

	public DBCheckForEmailImpl() {
	}

	@Override
	public Email checkForEmail(String emailAddress) throws EmailNotFoundException {

		Email emailToReturn = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "select * from email_for_registration where email=?";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, emailAddress);
				rs = stmt.executeQuery();

				while (rs.next()) {
					String email = rs.getString("email");
					String aptNumber = rs.getString("apartment_number");
					emailToReturn = new Email(email, aptNumber);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
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

		if (emailToReturn == null) {
			throw new EmailNotFoundException("E-Mail Address Not Found!");
		}

		return emailToReturn;
	}

}
