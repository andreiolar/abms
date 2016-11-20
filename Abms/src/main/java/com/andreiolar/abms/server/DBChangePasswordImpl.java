package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.andreiolar.abms.client.rpc.DBChangePassword;
import com.andreiolar.abms.security.BCrypt;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBChangePasswordImpl extends RemoteServiceServlet implements DBChangePassword {

	private static final long serialVersionUID = 7946945854805509474L;

	@Override
	public Boolean changePassword(String username, String password) throws Exception {

		Boolean result = new Boolean(false);
		int success = 0;
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "update users set password=? where username=?";
				stmt = conn.prepareStatement(q);

				// Hash the password
				String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

				stmt.setString(1, hashedPassword);
				stmt.setString(2, username);

				success = stmt.executeUpdate();
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				stmt.close();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			conn.close();
		}

		if (success > 0) {
			result = new Boolean(true);
		}

		if (result == null || result.booleanValue() == false) {
			throw new Exception("Failure during changing password!");
		}

		return result;
	}

}
