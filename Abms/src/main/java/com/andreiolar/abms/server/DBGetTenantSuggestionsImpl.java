package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.andreiolar.abms.client.rpc.DBGetTenantSuggestions;
import com.andreiolar.abms.shared.UserInfo;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBGetTenantSuggestionsImpl extends RemoteServiceServlet implements DBGetTenantSuggestions {

	private static final long serialVersionUID = 4754068415026080673L;

	@Override
	public List<String> getTenantSuggestions(UserInfo userInfo) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		List<String> tenantSuggestions = new ArrayList<String>();

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "select * from user_info where not(username=?)";
				stmt = conn.prepareStatement(q);
				stmt.setString(1, userInfo.getUsername());

				rs = stmt.executeQuery();

				while (rs.next()) {
					String apartmentNumber = rs.getString("apartment_number");
					String firstName = rs.getString("first_name");
					String lastName = rs.getString("last_name");

					StringBuilder sb = new StringBuilder();
					sb.append(firstName);
					sb.append(" ");
					sb.append(lastName);
					sb.append(", ");
					sb.append("Apt. Number: ");
					sb.append(apartmentNumber);

					tenantSuggestions.add(sb.toString());

				}
			} finally {
				rs.close();
				stmt.close();
			}
		} finally {
			conn.close();
		}

		if (tenantSuggestions.isEmpty()) {
			throw new Exception("No tenant suggestions have been found!");
		}

		return tenantSuggestions;
	}

}
