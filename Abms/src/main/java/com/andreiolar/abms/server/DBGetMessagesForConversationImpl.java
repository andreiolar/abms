package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.andreiolar.abms.client.rpc.DBGetMessagesForConversation;
import com.andreiolar.abms.shared.Message;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBGetMessagesForConversationImpl extends RemoteServiceServlet implements DBGetMessagesForConversation {

	private static final long serialVersionUID = -1846410693714734065L;

	@Override
	public List<Message> getAllMessagesForConversation(int conversationId) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		List<Message> messages = new ArrayList<Message>();

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "select * from conversation_reply where conv_id_fk=? order by id";
				stmt = conn.prepareStatement(q);
				stmt.setInt(1, conversationId);

				rs = stmt.executeQuery();

				int i = 1;
				while (rs.next()) {
					String reply = rs.getString("reply");
					String from = rs.getString("username");
					String date = rs.getString("date");

					Message message = new Message(i, reply, from, date);
					messages.add(message);
					i++;
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

		if (messages.isEmpty()) {
			throw new Exception("Sorry! We were unable to retrieve messages for this conversation. Please try again!");
		}

		return messages;
	}

}
