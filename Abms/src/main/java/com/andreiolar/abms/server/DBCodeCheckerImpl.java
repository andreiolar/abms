package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.andreiolar.abms.client.exception.InvalidCodeException;
import com.andreiolar.abms.client.rpc.DBCodeChecker;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBCodeCheckerImpl extends RemoteServiceServlet implements DBCodeChecker {

	private static final long serialVersionUID = -3120042466726321534L;

	@Override
	public void checkCode(String code, String email) throws InvalidCodeException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isValidCode = false;

		try {
			conn = MyConnection.getConnection();

			String q = "SELECT token FROM password_recovery WHERE email=?";
			stmt = conn.prepareStatement(q);
			stmt.setString(1, email);
			rs = stmt.executeQuery();

			if (rs.next()) {
				String token = rs.getString("token");
				if (token.equals(code)) {
					isValidCode = true;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
		} finally {
			try {
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
			}

		}

		if (!isValidCode) {
			throw new InvalidCodeException("Wrong/No code found for specified E-Mail Address.");
		}
	}

}
