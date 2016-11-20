package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.andreiolar.abms.client.rpc.DBGetAllSuggestions;
import com.andreiolar.abms.shared.UserInfo;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBGetAllSuggestionsImpl extends RemoteServiceServlet implements DBGetAllSuggestions {

	private static final long serialVersionUID = 1568963589542032937L;

	@Override
	public List<String> getAllSuggestions(UserInfo userInfo) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		List<String> suggestions = new ArrayList<String>();

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "select * from conversation where (user_one=? or user_two=?)"
						+ " and (select deleted from deleted_conversations where username=? and id=conversation.id)=? order by id desc";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, userInfo.getUsername());
				stmt.setString(2, userInfo.getUsername());
				stmt.setString(3, userInfo.getUsername());
				stmt.setBoolean(4, false);

				rs = stmt.executeQuery();

				while (rs.next()) {
					int id = rs.getInt("id");
					String source = rs.getString("user_one");
					String destination = rs.getString("user_two");
					String subject = rs.getString("subject");
					String date = rs.getString("date");

					StringBuilder sb = new StringBuilder();

					sb.append(id);
					sb.append(" | ");

					String convWith = source.equals(userInfo.getUsername()) ? destination : source;

					sb.append(convWith);
					sb.append(" | ");

					sb.append(subject);
					sb.append(" | ");

					sb.append(date);

					suggestions.add(sb.toString());
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

		if (suggestions.isEmpty()) {
			throw new Exception("Sorry! No conversations found");
		}

		return suggestions;
	}

}
