package com.andreiolar.abms.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {

	public String readProperty(String propertiesFile, String property) {
		Properties prop = new Properties();
		InputStream is = null;

		try {
			String filename = "/custom/" + propertiesFile;
			String filePath = System.getProperty("user.dir") + filename;

			is = new FileInputStream(new File(filePath));
			prop.load(is);

			String value = prop.getProperty(property);

			if (value == null) {
				throw new IOException("Could not read property: " + property);
			}

			return value;

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}

		return null;
	}

}
