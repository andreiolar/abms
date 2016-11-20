package com.andreiolar.abms.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class MyConnection {

	private static String URL = new String("jdbc:mysql://localhost:3306");
	private static String user = "root";
	private static String pass = "andrei";
	private static String schema = "administrare_bloc";

	public static Connection getConnection() throws Exception {
		Properties props = new Properties();
		props.setProperty("user", user);
		props.setProperty("password", pass);
		props.setProperty("zeroDateTimeBehavior", "convertToNull");
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection conn = DriverManager.getConnection(URL + "/" + schema, props);

		return conn;
	}

}
