package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.andreiolar.abms.client.exception.NoConversationsFoundException;
import com.andreiolar.abms.client.rpc.DBConversationDetails;
import com.andreiolar.abms.shared.ConversationDetails;
import com.andreiolar.abms.shared.UserDetails;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBConversationDetailsImpl extends RemoteServiceServlet implements DBConversationDetails {

	private static final long serialVersionUID = 6906200656694971670L;

	@Override
	public List<ConversationDetails> getConversationDetails(UserDetails userDetails) throws Exception {
		List<ConversationDetails> conversationDetails = new ArrayList<>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "select * from conversation where user_one=? or user_two=?";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, userDetails.getUsername());
				stmt.setString(2, userDetails.getUsername());

				rs = stmt.executeQuery();

				while (rs.next()) {
					int id = rs.getInt("id");
					String userOne = rs.getString("user_one");
					String userTwo = rs.getString("user_two");

					String conversationWith = userDetails.getUsername().equals(userOne) ? userTwo : userOne;

					String q2 = "select reply, date from conversation_reply where conv_id_fk=? order by id desc limit 1";
					PreparedStatement stmt2 = conn.prepareStatement(q2);
					stmt2.setInt(1, id);

					ResultSet rs2 = stmt2.executeQuery();
					if (rs2.next()) {
						String reply = rs2.getString("reply");
						String date = rs2.getString("date");

						String q3 = "select first_name, last_name, gender from user_info where username=?";
						PreparedStatement stmt3 = conn.prepareStatement(q3);
						stmt3.setString(1, conversationWith);

						ResultSet rs3 = stmt3.executeQuery();
						if (rs3.next()) {
							String firstName = rs3.getString("first_name");
							String lastName = rs3.getString("last_name");
							String gender = rs3.getString("gender");

							ConversationDetails conversationDetail = new ConversationDetails(conversationWith, firstName, lastName, reply, date,
									gender);
							conversationDetail.setId(id);
							conversationDetails.add(conversationDetail);
						}

						rs3.close();
						stmt3.close();

					}

					rs2.close();
					stmt2.close();
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

		if (conversationDetails.isEmpty()) {
			throw new NoConversationsFoundException("No conversations found.");
		}

		return conversationDetails;
	}

}
