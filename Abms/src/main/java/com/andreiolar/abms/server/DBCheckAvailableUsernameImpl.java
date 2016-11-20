package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.andreiolar.abms.client.rpc.DBCheckAvailableUsername;
import com.andreiolar.abms.shared.User;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBCheckAvailableUsernameImpl extends RemoteServiceServlet implements DBCheckAvailableUsername {

	private static final long serialVersionUID = 1L;

	private String URL = new String("jdbc:mysql://localhost:3306");
	private String user = "root";
	private String pass = "andrei";
	private String schema = "administrare_bloc";

	public DBCheckAvailableUsernameImpl() {
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
	public User checkForUsername(String username) throws Exception {

		User userToReturn = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = getConnection();

			try {
				String q = "select * from users where username=?";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, username);
				rs = stmt.executeQuery();

				while (rs.next()) {
					int id = rs.getInt("id");
					String user = rs.getString("username");

					userToReturn = new User(id, user, null, null);
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

		if (userToReturn == null) {
			throw new Exception("Username not found!");
		}

		return userToReturn;
	}

}
