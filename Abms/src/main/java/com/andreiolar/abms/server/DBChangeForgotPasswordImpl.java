package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.andreiolar.abms.client.rpc.DBChangeForgotPassword;
import com.andreiolar.abms.security.BCrypt;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBChangeForgotPasswordImpl extends RemoteServiceServlet implements DBChangeForgotPassword {

	private static final long serialVersionUID = 9208827813638944891L;

	@Override
	public Boolean resetPassword(String token, String password) throws Exception {
		Boolean result = new Boolean(false);
		int execute = 0;
		String email = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "select email from password_recovery where token=?";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, token);

				rs = stmt.executeQuery();

				if (rs.next()) {
					email = rs.getString("email");

					String q2 = "update users set password=? where username=(select username from user_info where email=?)";
					stmt = conn.prepareStatement(q2);
					String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

					stmt.setString(1, hashedPassword);
					stmt.setString(2, email);

					execute = stmt.executeUpdate();

					if (execute > 0) {
						String q3 = "delete from password_recovery where email=?";
						stmt = conn.prepareStatement(q3);
						stmt.setString(1, email);

						execute = stmt.executeUpdate();
					}
				} else {
					result = null;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			conn.close();
		}

		if (result == null) {
			throw new Exception("Error reseting password! Link is not valid anymore!");
		}

		if (execute > 0) {
			result = new Boolean(true);
		}

		if (result.booleanValue() == false) {
			throw new Exception("Error reseting your password!");
		}

		return result;
	}

}
