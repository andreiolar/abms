package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.andreiolar.abms.client.rpc.DBRetreiveSubmittedComplaints;
import com.andreiolar.abms.shared.SubmittedComplaint;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBRetreiveSubmittedComplaintsImpl extends RemoteServiceServlet implements DBRetreiveSubmittedComplaints {

	private static final long serialVersionUID = -7151137099738579417L;

	@Override
	public List<SubmittedComplaint> retreiveSubmittedComplaints() throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<SubmittedComplaint> submittedComplaints = new ArrayList<SubmittedComplaint>();

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "select c.complaint_to, c.date, c.phone_number, u.apartment_number from complaints c, user_info u where u.username = c.username";
				stmt = conn.prepareStatement(q);

				rs = stmt.executeQuery();
				while (rs.next()) {
					int aptNumber = Integer.parseInt(rs.getString("apartment_number"));
					java.sql.Date date = rs.getDate("date");
					String complaintTo = rs.getString("complaint_to");
					String phoneNumber = rs.getString("phone_number");

					SubmittedComplaint submittedComplaint = new SubmittedComplaint(aptNumber, new Date(date.getTime()), complaintTo, phoneNumber);
					submittedComplaints.add(submittedComplaint);
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

		return submittedComplaints;
	}
}
