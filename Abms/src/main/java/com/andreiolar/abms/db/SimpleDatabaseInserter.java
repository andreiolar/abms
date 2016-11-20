package com.andreiolar.abms.db;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class SimpleDatabaseInserter {

	private static String URL = new String("jdbc:mysql://localhost:3306");
	private static String user = "root";
	private static String pass = "andrei";
	private static String schema = "administrare_bloc";

	private static Connection getConnection() throws Exception {
		Properties props = new Properties();
		props.setProperty("user", user);
		props.setProperty("password", pass);
		props.setProperty("zeroDateTimeBehavior", "convertToNull");
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection conn = DriverManager.getConnection(URL + "/" + schema, props);

		return conn;
	}

	/**
	 * The insert is NOT supposed to insert duplicates. In this case, we separate them by month and by apartment number. If there is no entry for the
	 * current month and for the specified apartment number, then we don't insert it.
	 * 
	 * @throws SQLException
	 **/
	@SuppressWarnings("resource")
	public static void insert(Object object, String databaseTable) throws Exception {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		Field[] declaredFields = object.getClass().getDeclaredFields();

		String parameters = "";
		String fields = "";

		for (int i = 0; i < declaredFields.length; i++) {
			if (i == declaredFields.length - 1) {
				parameters += "?";
				fields += declaredFields[i].getName();
			} else {
				parameters += "?, ";
				fields += declaredFields[i].getName() + ", ";
			}
		}

		try {
			conn = getConnection();

			try {
				String checkQuery = "select * from " + databaseTable + " where luna=? and aptNumber=?";
				stmt = conn.prepareStatement(checkQuery);
				declaredFields[14].setAccessible(true);
				stmt.setString(1, (String) declaredFields[14].get(object));
				declaredFields[0].setAccessible(true);
				stmt.setString(2, (String) declaredFields[0].get(object));

				rs = stmt.executeQuery();

				if (!rs.next()) {
					String q = "insert into " + databaseTable + "(" + fields + ") values(" + parameters + ")";
					stmt = conn.prepareStatement(q);

					for (int i = 0; i < declaredFields.length; i++) {
						declaredFields[i].setAccessible(true);
						stmt.setString(i + 1, (String) declaredFields[i].get(object));
					}

					int executeUpdate = stmt.executeUpdate();

					if (executeUpdate != 1) {
						throw new Exception("Error inserting personal User costs into DB!");
					}
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			rs.close();
			stmt.close();
			conn.close();

		}

	}

}
