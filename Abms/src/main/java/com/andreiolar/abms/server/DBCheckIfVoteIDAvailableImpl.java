package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.andreiolar.abms.client.rpc.DBCheckIfVoteIDAvailable;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBCheckIfVoteIDAvailableImpl extends RemoteServiceServlet implements DBCheckIfVoteIDAvailable {

	private static final long serialVersionUID = 6526156012803449665L;

	@Override
	public Boolean checkVoteId(String voteId) throws Exception {
		Boolean result = new Boolean(false);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "select * from votes where vote_id=?";
				stmt = conn.prepareStatement(q);
				stmt.setInt(1, Integer.parseInt(voteId));

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
			throw new Exception("There already exists/existed a voting session with vote ID: " + voteId);
		}

		return result;
	}

}
