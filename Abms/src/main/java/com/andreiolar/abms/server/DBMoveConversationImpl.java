package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.andreiolar.abms.client.rpc.DBMoveConversation;
import com.andreiolar.abms.shared.UserInfo;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBMoveConversationImpl extends RemoteServiceServlet implements DBMoveConversation {

	private static final long serialVersionUID = -6482691430048768043L;

	@Override
	public void moveConversation(int convId, String filter, UserInfo userInfo) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;

		int executed = 0;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "update conversation_filter set filter=? where id=? and username=?";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, filter);
				stmt.setInt(2, convId);
				stmt.setString(3, userInfo.getUsername());

				executed = stmt.executeUpdate();

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

		if (executed == 0) {
			throw new Exception("Unable to move conversation with ID: " + convId + " to " + filter);
		}
	}

}
