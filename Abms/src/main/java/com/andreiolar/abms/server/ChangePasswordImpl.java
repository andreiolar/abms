package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.andreiolar.abms.client.exception.OldPasswordNotCorrectException;
import com.andreiolar.abms.client.rpc.ChangePassword;
import com.andreiolar.abms.security.BCrypt;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ChangePasswordImpl extends RemoteServiceServlet implements ChangePassword {

	private static final long serialVersionUID = 4682474835758074576L;

	@Override
	public void changePassword(String username, String oldPassword, String newPassword) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		String originalPassword = null;

		boolean passwordsMatch = false;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "select password from users where username = ?";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, username);

				rs = stmt.executeQuery();

				if (rs.next()) {
					originalPassword = rs.getString("password");
				}
			} catch (Exception e) {
				throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
			} finally {
				rs.close();
				stmt.close();
			}

			if (originalPassword != null) {
				if (BCrypt.checkpw(oldPassword, originalPassword)) {
					passwordsMatch = true;
				}

				if (passwordsMatch) {
					try {
						String q = "update users set password=? where username=?";
						stmt = conn.prepareStatement(q);

						// Hash the password
						String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

						stmt.setString(1, hashedPassword);
						stmt.setString(2, username);

						stmt.executeUpdate();
					} catch (Exception e) {
						throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
					} finally {
						stmt.close();
					}
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
		} finally {
			conn.close();
		}

		if (!passwordsMatch) {
			throw new OldPasswordNotCorrectException();
		}
	}
}
