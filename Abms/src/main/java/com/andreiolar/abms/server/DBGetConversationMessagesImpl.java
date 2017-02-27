package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.andreiolar.abms.client.exception.MessagesNotFoundException;
import com.andreiolar.abms.client.rpc.DBGetConversationMessages;
import com.andreiolar.abms.shared.ConversationMessage;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBGetConversationMessagesImpl extends RemoteServiceServlet implements DBGetConversationMessages {

	private static final long serialVersionUID = -4252711941791442575L;

	@Override
	public List<ConversationMessage> getConversationMessages(String id) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<ConversationMessage> messages = new ArrayList<ConversationMessage>();

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "select * from conversation_reply where conv_id_fk=?";
				stmt = conn.prepareStatement(q);
				stmt.setInt(1, Integer.valueOf(id));
				rs = stmt.executeQuery();

				while (rs.next()) {
					int messageId = rs.getInt("id");
					String message = rs.getString("reply");
					String username = rs.getString("username");
					String date = rs.getString("date");

					String q2 = "select gender from user_info where username=?";
					PreparedStatement stmt2 = conn.prepareStatement(q2);
					stmt2.setString(1, username);
					ResultSet rs2 = stmt2.executeQuery();

					if (rs2.next()) {
						String gender = rs2.getString("gender");

						ConversationMessage conversationMessage = new ConversationMessage(String.valueOf(messageId), username, gender, message, date);
						messages.add(conversationMessage);
					}
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

		if (messages.isEmpty()) {
			throw new MessagesNotFoundException();
		}

		return messages;
	}
}
