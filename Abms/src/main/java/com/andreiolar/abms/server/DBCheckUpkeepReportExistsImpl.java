package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.andreiolar.abms.client.exception.UpkeepReportMissingException;
import com.andreiolar.abms.client.rpc.DBCheckUpkeepReportExists;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBCheckUpkeepReportExistsImpl extends RemoteServiceServlet implements DBCheckUpkeepReportExists {

	private static final long serialVersionUID = -3448705195615462890L;

	@Override
	public void checkUpkeepReport(String previousMonthAndYear) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int total = 0;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "select count(*) as total from personal_upkeep_information where luna=?";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, previousMonthAndYear);

				rs = stmt.executeQuery();

				if (rs.next()) {
					total = rs.getInt("total");
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

		if (total == 0) {
			throw new UpkeepReportMissingException();
		}
	}
}
