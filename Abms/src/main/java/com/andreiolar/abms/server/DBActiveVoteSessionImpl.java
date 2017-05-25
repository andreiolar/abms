package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.andreiolar.abms.client.exception.NoActiveVoteException;
import com.andreiolar.abms.client.rpc.DBActiveVoteSession;
import com.andreiolar.abms.shared.VoteSession;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBActiveVoteSessionImpl extends RemoteServiceServlet implements DBActiveVoteSession {

	private static final long serialVersionUID = -2964959230065267612L;

	@Override
	public VoteSession getActiveVoteSession() throws Exception {
		VoteSession voteSession = new VoteSession();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "select * from vote_descriptions where active=true";
				stmt = conn.prepareStatement(q);

				rs = stmt.executeQuery();

				if (rs.next()) {
					int id = rs.getInt("vote_id");
					String title = rs.getString("title");
					String description = rs.getString("description");

					voteSession.setVoteId(id);
					voteSession.setTitle(title);
					voteSession.setDescription(description);
					voteSession.setActive(true);
				}
			} catch (Exception e) {
				throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
			} finally {
				rs.close();
				stmt.close();
			}
		} catch (Exception e) {
			throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
		} finally {
			conn.close();
		}

		if (voteSession.getVoteId() == 0) {
			throw new NoActiveVoteException();
		}

		return voteSession;
	}

}
