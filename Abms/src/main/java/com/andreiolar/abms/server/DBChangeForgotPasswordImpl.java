package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.andreiolar.abms.client.rpc.DBChangeForgotPassword;
import com.andreiolar.abms.security.BCrypt;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBChangeForgotPasswordImpl extends RemoteServiceServlet implements DBChangeForgotPassword {

	private static final long serialVersionUID = 9208827813638944891L;

	@Override
	public Boolean resetPassword(String email, String password) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;

		boolean isUserPasswordUpdated = false;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "UPDATE users SET password=? WHERE username=(SELECT username FROM user_info WHERE email=?)";
				stmt = conn.prepareStatement(q);
				String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

				stmt.setString(1, hashedPassword);
				stmt.setString(2, email);

				int executed = stmt.executeUpdate();
				if (executed > 0) {
					isUserPasswordUpdated = true;
				}

			} catch (Exception ex) {
				throw new RuntimeException("Something went wrong: " + ex.getMessage(), ex);
			} finally {
				stmt.close();
			}

			if (isUserPasswordUpdated) {
				try {
					String q = "DELETE FROM password_recovery WHERE email=?";
					stmt = conn.prepareStatement(q);
					stmt.setString(1, email);

					stmt.executeUpdate();
				} catch (Exception e) {
					throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
				} finally {
					stmt.close();
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException("Something went wrong: " + ex.getMessage(), ex);
		} finally {
			conn.close();
		}

		if (!isUserPasswordUpdated) {
			throw new Exception("Failed to reset password. Please try again.");
		}

		return true;
	}

}
