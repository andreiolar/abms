package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.andreiolar.abms.client.rpc.DBSendMessage;
import com.andreiolar.abms.shared.Conversation;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBSendMessageImpl extends RemoteServiceServlet implements DBSendMessage {

	private static final long serialVersionUID = -1768687964161046618L;

	@Override
	public Boolean sendMessage(Conversation conversation) throws Exception {
		Boolean returnValue = false;
		int executed = 0;
		int lastInserted = 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		String destination = null;

		try {
			conn = MyConnection.getConnection();

			String ip = getThreadLocalRequest().getRemoteAddr();

			DateFormat dateFormat = new SimpleDateFormat("E, dd/MM/yyyy HH:mm:ss");
			Date date = new Date();
			String formattedDate = dateFormat.format(date);

			try {
				String q = "select username from user_info where apartment_number='" + conversation.getDestination() + "'";
				stmt = conn.prepareStatement(q);
				rs = stmt.executeQuery();

				if (rs.next()) {
					destination = rs.getString(1);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				rs.close();
				stmt.close();
			}

			if (destination == null) {
				throw new Exception("Could not determine recipient!");
			}

			try {
				String q = "insert into conversation(user_one, user_two, subject, ip, date) values(?,?,?,?,?)";
				stmt = conn.prepareStatement(q, Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, conversation.getSource());
				stmt.setString(2, destination);
				stmt.setString(3, conversation.getSubject());
				stmt.setString(4, ip);
				stmt.setString(5, formattedDate);;

				executed = stmt.executeUpdate();

				ResultSet generatedKeys = stmt.getGeneratedKeys();
				generatedKeys.next();
				lastInserted = generatedKeys.getInt(1);
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				stmt.close();
			}

			try {
				String q = "insert into conversation_reply(reply, username, ip, date, conv_id_fk) values(?,?,?,?,?)";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, conversation.getMessage());
				stmt.setString(2, conversation.getSource());
				stmt.setString(3, ip);
				stmt.setString(4, formattedDate);
				stmt.setInt(5, lastInserted);

				executed = stmt.executeUpdate();
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				stmt.close();
			}

			try {
				String[] users = new String[2];
				users[0] = conversation.getSource();
				users[1] = destination;

				for (int i = 0; i < 2; i++) {
					String q = "insert into conversation_filter(id, username) values(?,?)";
					stmt = conn.prepareStatement(q);
					stmt.setInt(1, lastInserted);
					stmt.setString(2, users[i]);
					executed = stmt.executeUpdate();
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				stmt.close();
			}

			try {
				String[] users = new String[2];
				users[0] = conversation.getSource();
				users[1] = destination;

				for (int i = 0; i < 2; i++) {
					String q = "insert into deleted_conversations(id, username) values(?,?)";
					stmt = conn.prepareStatement(q);
					stmt.setInt(1, lastInserted);
					stmt.setString(2, users[i]);
					executed = stmt.executeUpdate();
				}

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

		if (executed > 0) {
			returnValue = new Boolean(true);
		}

		if (returnValue.booleanValue() == false) {
			throw new Exception("Unable to send Message!");
		}

		return returnValue;
	}

}
