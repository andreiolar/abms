package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.andreiolar.abms.client.rpc.DBCheckForEmail;
import com.andreiolar.abms.shared.Email;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBCheckForEmailImpl extends RemoteServiceServlet implements DBCheckForEmail {

	private static final long serialVersionUID = 1L;

	private String URL = new String("jdbc:mysql://localhost:3306");
	private String user = "root";
	private String pass = "andrei";
	private String schema = "administrare_bloc";

	public DBCheckForEmailImpl() {
	}

	private Connection getConnection() throws Exception {
		Properties props = new Properties();
		props.setProperty("user", user);
		props.setProperty("password", pass);
		props.setProperty("zeroDateTimeBehavior", "convertToNull");
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection conn = DriverManager.getConnection(URL + "/" + schema, props);

		return conn;
	}

	@Override
	public Email checkForEmail(String emailAddress) throws Exception {

		Email emailToReturn = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = getConnection();

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
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			rs.close();
			stmt.close();
			conn.close();
		}

		if (emailToReturn == null) {
			throw new Exception("E-Mail Address Not Found!");
		}

		return emailToReturn;
	}

}
