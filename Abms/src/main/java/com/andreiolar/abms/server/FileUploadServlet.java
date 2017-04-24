package com.andreiolar.abms.server;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import com.andreiolar.abms.mail.MailSender;
import com.andreiolar.abms.utils.ExcelToPersonalView;

public class FileUploadServlet extends HttpServlet {

	private static final long serialVersionUID = -9065632897517874244L;

	private static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/files/general";

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Process only multipart requests
		if (ServletFileUpload.isMultipartContent(req)) {

			// Create a factory for disk-based file items
			FileItemFactory factory = new DiskFileItemFactory();

			// Create a new file uplolad handler
			ServletFileUpload upload = new ServletFileUpload(factory);

			// Parse the request
			try {
				List<FileItem> items = upload.parseRequest(req);

				for (FileItem item : items) {
					// process only file upload - discard other form item types
					if (item.isFormField()) {
						continue;
					}

					String fileName = item.getName();
					// get only the file name, not the whole path
					if (fileName != null) {
						fileName = FilenameUtils.getName(fileName);
					}

					File uploadedFile = new File(UPLOAD_DIRECTORY, fileName);

					if (!uploadedFile.exists()) {
						uploadedFile.getParentFile().mkdirs();
					}

					if (uploadedFile.createNewFile()) {
						item.write(uploadedFile);
						resp.setStatus(HttpServletResponse.SC_CREATED);
						resp.getWriter().print("The file was created successfully.");
						resp.flushBuffer();

						sendMail(uploadedFile);

					} else {
						uploadedFile.delete();
						item.write(uploadedFile);
						uploadedFile.createNewFile();
						resp.setStatus(HttpServletResponse.SC_CREATED);
						resp.getWriter().print("The file was replaced successfully.");
						resp.flushBuffer();

						sendMail(uploadedFile);
					}

					ExcelToPersonalView.processFile(uploadedFile);
				}
			} catch (Exception ex) {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occured while craeting the file: " + ex.getMessage());
			}
		} else {
			resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Request contents type is not supported by the servlet");
		}
	}

	private void sendMail(File uploadedFile) {
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
							+ "The upkeep report was just submitted and it's available to preview and download at Administration -&gt; Monthly Costs"
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
