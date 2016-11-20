package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.andreiolar.abms.client.rpc.DBDeleteConversation;
import com.andreiolar.abms.shared.UserInfo;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBDeleteConversationImpl extends RemoteServiceServlet implements DBDeleteConversation {

	private static final long serialVersionUID = -1228367999936288388L;

	@Override
	public void deleteConversation(int convId, UserInfo userInfo) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;

		int executed = 0;

		try {
			conn = MyConnection.getConnection();

			String ip = getThreadLocalRequest().getRemoteAddr();

			DateFormat dateFormat = new SimpleDateFormat("E, dd/MM/yyyy HH:mm:ss");
			Date date = new Date();
			String formattedDate = dateFormat.format(date);

			try {
				String q = "update deleted_conversations set deleted=? where id=? and username=?";
				stmt = conn.prepareStatement(q);
				stmt.setBoolean(1, true);
				stmt.setInt(2, convId);
				stmt.setString(3, userInfo.getUsername());

				executed = stmt.executeUpdate();

			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				stmt.close();
			}

			try {
				String q = "insert into conversation_reply(reply, username, ip, date, conv_id_fk) values(?,?,?,?,?)";
				stmt = conn.prepareStatement(q);
				stmt.setString(1,
						"This conversation was deleted by user: " + userInfo.getUsername()
								+ ". Replying to this conversation will not produce any effect, since " + userInfo.getUsername()
								+ " will not recieve your messages. If you want to send a message to " + userInfo.getUsername()
								+ " please start a new conversation. We recommend you to delete this conversation as soon as possible.");
				stmt.setString(2, userInfo.getUsername());
				stmt.setString(3, ip);
				stmt.setString(4, formattedDate);
				stmt.setInt(5, convId);

				executed = stmt.executeUpdate();
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				stmt.close();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			conn.close();
		}

		if (executed == 0) {
			throw new Exception("Unable to delete conversation with ID: " + convId);
		}
	}

}
