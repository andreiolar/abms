package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.andreiolar.abms.client.rpc.DBChangeUsername;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBChangeUsernameImpl extends RemoteServiceServlet implements DBChangeUsername {

	private static final long serialVersionUID = -4059776992321790422L;

	@Override
	public Boolean changeUsername(String originalUsername, String newUsername) throws Exception {
		Boolean result = new Boolean(true);
		Connection conn = null;
		PreparedStatement stmt = null;

		int executeUser = 0;
		int executeUserInfo = 0;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "update users set username=? where username=?";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, newUsername);
				stmt.setString(2, originalUsername);

				executeUser = stmt.executeUpdate();
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				stmt.close();
			}

			try {
				String q = "update user_info set username=? where username=?";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, newUsername);
				stmt.setString(2, originalUsername);

				executeUserInfo = stmt.executeUpdate();
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

		if (executeUser < 1 || executeUserInfo < 1) {
			throw new Exception("Error updateing username!");
		}

		return result;
	}

}
