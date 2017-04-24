package com.andreiolar.abms.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

import com.andreiolar.abms.mail.MailSender;
import com.andreiolar.abms.properties.PropertiesReader;
import com.andreiolar.abms.utils.ExcelToPersonalView;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

public class ExcelUploader extends HttpServlet {

	private static final long serialVersionUID = -2891066645502531422L;

	private static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/files/general";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (!ServletFileUpload.isMultipartContent(req)) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Not a multipart request");
			return;
		}

		ServletFileUpload upload = new ServletFileUpload(); // from Commons

		try {
			FileItemIterator iter = upload.getItemIterator(req);
			PropertiesReader reader = new PropertiesReader();

			if (iter.hasNext()) {
				FileItemStream fileItem = iter.next();

				InputStream in = fileItem.openStream();
				// The destination of your uploaded files.
				File targetFile = new File(UPLOAD_DIRECTORY + "/" + fileItem.getName());

				FileUtils.copyInputStreamToFile(in, targetFile);

				// Process file
				ExcelToPersonalView.processFile(targetFile);

				Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap("cloud_name", reader.readProperty("cloudinary.properties", "cloud_name"),
						"api_key", reader.readProperty("cloudinary.properties", "api_key"), "api_secret",
						reader.readProperty("cloudinary.properties", "api_secret")));

				cloudinary.uploader().upload(targetFile, ObjectUtils.asMap("use_filename", true, "unique_filename", false, "resource_type", "auto"));

				sendMails();
			}
		} catch (Exception caught) {
			throw new RuntimeException(caught);
		}
	}

	private void sendMails() {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = MyConnection.getConnection();

			try {
				String q = "select first_name, last_name, email from user_info";
				stmt = conn.prepareStatement(q);

				rs = stmt.executeQuery();

				while (rs.next()) {
					String firstName = rs.getString("first_name");
					String lastName = rs.getString("last_name");
					String email = rs.getString("email");

					if (firstName.contains("Administrator") || lastName.contains("Administrator")) {
						continue;
					}

					String subject = "Upkeep report for previous month";
					String to = email;
					String message = "<p>" + "Hello " + firstName + " " + lastName + "," + "<br><br>"
							+ "The upkeep report was just submitted and it's available to preview and download at Administration -&gt; General Costs View"
							+ "<br><br>" + "Best regards," + "<br>" + "Administration" + "</p>";

					MailSender.sendMail(subject, to, message, null);
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
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
