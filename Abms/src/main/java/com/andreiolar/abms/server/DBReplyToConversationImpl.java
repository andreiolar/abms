package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.andreiolar.abms.client.rpc.DBReplyToConversation;
import com.andreiolar.abms.shared.ConversationMessage;
import com.andreiolar.abms.shared.ReplyMessage;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBReplyToConversationImpl extends RemoteServiceServlet implements DBReplyToConversation {

	private static final long serialVersionUID = 7094452124543422116L;

	@Override
	public ConversationMessage replyToConversation(ReplyMessage message) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;

		DateFormat dateFormat = new SimpleDateFormat("E, dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		String formattedDate = dateFormat.format(date);

		ConversationMessage conversationMessage;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "insert into conversation_reply(reply, username, date, conv_id_fk) values(?,?,?,?)";
				stmt = conn.prepareStatement(q, Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, message.getText());
				stmt.setString(2, message.getUsername());
				stmt.setString(3, formattedDate);
				stmt.setInt(4, message.getConvId());

				stmt.executeUpdate();

				ResultSet generatedKeys = stmt.getGeneratedKeys();
				generatedKeys.next();
				int lastInserted = generatedKeys.getInt(1);

				conversationMessage = new ConversationMessage(String.valueOf(lastInserted), message.getUsername(), message.getText(), formattedDate);
			} catch (Exception ex) {
				throw new RuntimeException("Something went wrong: " + ex.getMessage(), ex);
			} finally {
				stmt.close();
			}

		} catch (Exception ex) {
			throw new RuntimeException("Something went wrong: " + ex.getMessage(), ex);
		} finally {
			conn.close();
		}

		return conversationMessage;
	}
}
