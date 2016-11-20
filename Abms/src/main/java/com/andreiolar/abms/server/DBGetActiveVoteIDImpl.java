package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.andreiolar.abms.client.rpc.DBGetActiveVoteID;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBGetActiveVoteIDImpl extends RemoteServiceServlet implements DBGetActiveVoteID {

	private static final long serialVersionUID = 4160130365854522086L;

	@Override
	public String getActiveVoteID() throws Exception {
		String result = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "select vote_id from votes where active='true' group by vote_id";
				stmt = conn.prepareStatement(q);

				rs = stmt.executeQuery();

				while (rs.next()) {
					result = String.valueOf(rs.getInt("vote_id"));
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

		return result;
	}

}
