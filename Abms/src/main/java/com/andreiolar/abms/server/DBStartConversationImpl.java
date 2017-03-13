package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.andreiolar.abms.client.exception.UnableToSendMessageException;
import com.andreiolar.abms.client.rpc.DBStartConversation;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBStartConversationImpl extends RemoteServiceServlet implements DBStartConversation {

	private static final long serialVersionUID = -606745218478180868L;

	@Override
	public void startConversation(String source, String destination, String message) throws Exception {
		Boolean returnValue = false;
		int executed = 0;
		int lastInserted = 0;
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = MyConnection.getConnection();

			DateFormat dateFormat = new SimpleDateFormat("E, dd/MM/yyyy HH:mm:ss");
			Date date = new Date();
			String formattedDate = dateFormat.format(date);

			try {
				String q = "insert into conversation(user_one, user_two) values(?,?)";
				stmt = conn.prepareStatement(q, Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, source);
				stmt.setString(2, destination);

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
				String q = "insert into conversation_reply(reply, username, date, conv_id_fk) values(?,?,?,?)";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, message);
				stmt.setString(2, source);
				stmt.setString(3, formattedDate);
				stmt.setInt(4, lastInserted);

				executed = stmt.executeUpdate();
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

		if (executed > 0) {
			returnValue = new Boolean(true);
		}

		if (returnValue.booleanValue() == false) {
			throw new UnableToSendMessageException();
		}
	}

}
