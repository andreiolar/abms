package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.andreiolar.abms.client.rpc.DBCheckIfActiveVotingSession;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBCheckIfActiveVotingSessionImpl extends RemoteServiceServlet implements DBCheckIfActiveVotingSession {

	private static final long serialVersionUID = 2439851313636727119L;

	@Override
	public Boolean ckeckForActiveVotingSession() throws Exception {
		Boolean result = new Boolean(false);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "select * from votes where active='true'";
				stmt = conn.prepareStatement(q);

				rs = stmt.executeQuery();

				if (!rs.next()) {
					result = new Boolean(true);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				rs.close();
				stmt.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			conn.close();
		}

		if (result == null || result.booleanValue() == false) {
			throw new Exception("There already is an active voting session available!<br>Please deactivate that before creating a new one.");
		}

		return result;
	}

}
