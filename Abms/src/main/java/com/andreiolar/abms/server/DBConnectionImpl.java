package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.andreiolar.abms.client.exception.InvalidCredentialsException;
import com.andreiolar.abms.client.rpc.DBConnection;
import com.andreiolar.abms.security.BCrypt;
import com.andreiolar.abms.shared.User;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBConnectionImpl extends RemoteServiceServlet implements DBConnection {

	private static final long serialVersionUID = 1L;

	public DBConnectionImpl() {
	}

	@Override
	public User authenticateUser(String username, String password) throws InvalidCredentialsException {
		User returnUser = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = MyConnection.getConnection();

			String q = "select * from users where BINARY username=?";
			stmt = conn.prepareStatement(q);
			stmt.setString(1, username);
			rs = stmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("id");
				String user = rs.getString("username");
				String pass = rs.getString("password");
				String type = rs.getString("type");

				if (BCrypt.checkpw(password, pass)) {
					returnUser = new User(id, user, pass, type);

					HttpServletRequest threadLocalRequest = this.getThreadLocalRequest();
					HttpSession session = threadLocalRequest.getSession();
					session.setAttribute("user", user);
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

		if (returnUser == null) {
			throw new InvalidCredentialsException("Login failed! Wrong username or password!");
		}

		return returnUser;
	}

}
