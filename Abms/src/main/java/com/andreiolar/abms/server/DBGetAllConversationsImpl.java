package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.andreiolar.abms.client.rpc.DBGetAllConversations;
import com.andreiolar.abms.shared.Conversation;
import com.andreiolar.abms.shared.UserInfo;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBGetAllConversationsImpl extends RemoteServiceServlet implements DBGetAllConversations {

	private static final long serialVersionUID = -1065966916275096473L;

	@Override
	public List<Conversation> getAllMessages(UserInfo userInfo, String conversationFilter) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		List<Conversation> conversations = new ArrayList<Conversation>();

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "select * from conversation where (user_one=? or user_two=?)"
						+ " and (select filter from conversation_filter where username=? and id=conversation.id)=?"
						+ " and (select deleted from deleted_conversations where username=? and id=conversation.id)=? order by id desc";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, userInfo.getUsername());
				stmt.setString(2, userInfo.getUsername());
				stmt.setString(3, userInfo.getUsername());
				stmt.setString(4, conversationFilter);
				stmt.setString(5, userInfo.getUsername());
				stmt.setBoolean(6, false);

				rs = stmt.executeQuery();

				while (rs.next()) {
					int id = rs.getInt("id");
					String source = rs.getString("user_one");
					String destination = rs.getString("user_two");
					String subject = rs.getString("subject");
					String date = rs.getString("date");

					Conversation conversation = new Conversation();
					conversation.setId(id);
					conversation.setSource(source);
					conversation.setDestination(destination);
					conversation.setDate(date);

					conversations.add(conversation);
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

		if (conversations.isEmpty()) {
			throw new Exception("Sorry! No conversations found in: " + conversationFilter);
		}

		return conversations;
	}

}
