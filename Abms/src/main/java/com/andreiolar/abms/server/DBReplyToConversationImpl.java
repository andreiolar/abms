package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.andreiolar.abms.client.rpc.DBReplyToConversation;
import com.andreiolar.abms.shared.ReplyMessage;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBReplyToConversationImpl extends RemoteServiceServlet implements DBReplyToConversation {

	private static final long serialVersionUID = 7094452124543422116L;

	@Override
	public void replyToConversation(ReplyMessage message) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;

		DateFormat dateFormat = new SimpleDateFormat("E, dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		String formattedDate = dateFormat.format(date);

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "insert into conversation_reply(reply, username, date, conv_id_fk) values(?,?,?,?)";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, message.getText());
				stmt.setString(2, message.getUsername());
				stmt.setString(3, formattedDate);
				stmt.setInt(4, message.getConvId());

				stmt.executeUpdate();
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
	}
}
