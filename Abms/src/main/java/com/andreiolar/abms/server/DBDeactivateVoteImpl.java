package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.andreiolar.abms.client.rpc.DBDeactivateVote;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBDeactivateVoteImpl extends RemoteServiceServlet implements DBDeactivateVote {

	private static final long serialVersionUID = 1333220538206548489L;

	@Override
	public Boolean deactivateVote(String voteId) throws Exception {
		Boolean result = new Boolean(false);
		int execute = 0;
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "update votes set active='false' where vote_id=?";
				stmt = conn.prepareStatement(q);
				stmt.setInt(1, Integer.parseInt(voteId));

				execute = stmt.executeUpdate();
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

		if (execute > 0) {
			result = new Boolean(true);
		}

		if (result == null || result.booleanValue() == false) {
			throw new Exception("Error while deactivating voting session!");
		}

		return result;
	}

}
