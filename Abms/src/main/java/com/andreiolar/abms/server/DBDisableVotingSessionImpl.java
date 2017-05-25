package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.andreiolar.abms.client.rpc.DBDisableVotingSession;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBDisableVotingSessionImpl extends RemoteServiceServlet implements DBDisableVotingSession {

	private static final long serialVersionUID = 6933483485739053277L;

	@Override
	public Void disableActiveVotingSession() throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		int executed = 0;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "update vote_descriptions set active=false where active=true";
				stmt = conn.prepareStatement(q);

				executed = stmt.executeUpdate();

			} catch (Exception e) {
				throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
			} finally {
				stmt.close();
			}
		} catch (Exception e) {
			throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
		} finally {
			conn.close();
		}

		if (executed == 0) {
			throw new RuntimeException();
		}

		return null;
	}

}
