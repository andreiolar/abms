package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.andreiolar.abms.client.rpc.DBGetComplaintInfo;
import com.andreiolar.abms.shared.ComplaintInfo;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBGetComplaintInfoImpl extends RemoteServiceServlet implements DBGetComplaintInfo {

	private static final long serialVersionUID = -7333623300241523431L;

	public DBGetComplaintInfoImpl() {
	}

	@Override
	public List<ComplaintInfo> getComplaintInfo() throws Exception {
		List<ComplaintInfo> complaints = new ArrayList<ComplaintInfo>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "select complaints.complaint_to, complaints.date, complaints.id, complaints.phone_number, concat(user_info.first_name, ' ', user_info.last_name) as name from complaints inner join user_info on complaints.username=user_info.username";
				stmt = conn.prepareStatement(q);
				rs = stmt.executeQuery();

				while (rs.next()) {
					int id = rs.getInt("id");
					String name = rs.getString("name");
					String phoneNumber = rs.getString("phone_number");
					Date date = rs.getDate("date");
					String complaintTo = rs.getString("complaint_to");

					ComplaintInfo complaintInfo = new ComplaintInfo(String.valueOf(id), name, phoneNumber, date.toString(), complaintTo);
					complaints.add(complaintInfo);
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

		if (complaints == null || complaints.isEmpty()) {
			throw new Exception("Error retrieving complaints!");
		}

		return complaints;
	}

}
