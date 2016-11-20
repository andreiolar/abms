package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.andreiolar.abms.client.rpc.DBGetFinishedVoteIds;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBGetFinishedVoteIdsImpl extends RemoteServiceServlet implements DBGetFinishedVoteIds {

	private static final long serialVersionUID = -4995794912138693937L;

	@Override
	public List<String> getFinishedVoteIds(boolean all) throws Exception {

		List<String> finishedVoteIds = new ArrayList<String>();

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "select vote_id from votes where active='false' group by vote_id";

				if (all) {
					q = "select vote_id from votes group by vote_id";
				}

				stmt = conn.prepareStatement(q);
				rs = stmt.executeQuery();

				while (rs.next()) {
					int voteId = rs.getInt("vote_id");
					finishedVoteIds.add(String.valueOf(voteId));
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

		if (finishedVoteIds == null || finishedVoteIds.isEmpty()) {
			throw new Exception("There are currently no finished votes in order for you to view results for.");
		}

		return finishedVoteIds;
	}

}
