package com.andreiolar.abms.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

import com.andreiolar.abms.properties.PropertiesReader;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

public class FileUploader extends HttpServlet {

	private static final long serialVersionUID = -1012536984949263031L;

	private static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/files/general";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (!ServletFileUpload.isMultipartContent(req)) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Not a multipart request");
			return;
		}

		ServletFileUpload upload = new ServletFileUpload(); // from Commons

		String type = req.getParameter("type");
		String extension = req.getParameter("extension");

		String filename = null;
		if (type.equals("upkeep")) {
			YearMonth currentMonth = YearMonth.now();
			YearMonth previousMonth = currentMonth.minusMonths(1);

			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMMM_yyyy");
			filename = "Upkeep_" + previousMonth.format(dtf) + "." + extension;
		} else if (type.equals("picture")) {
			String username = req.getParameter("username");
			filename = username + ".png";
		}

		try {
			FileItemIterator iter = upload.getItemIterator(req);
			PropertiesReader reader = new PropertiesReader();

			if (iter.hasNext()) {
				FileItemStream fileItem = iter.next();

				InputStream in = fileItem.openStream();
				// The destination of your uploaded files.
				File targetFile = new File(UPLOAD_DIRECTORY + "/" + filename);

				FileUtils.copyInputStreamToFile(in, targetFile);

				Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap("cloud_name", reader.readProperty("cloudinary.properties", "cloud_name"),
						"api_key", reader.readProperty("cloudinary.properties", "api_key"), "api_secret",
						reader.readProperty("cloudinary.properties", "api_secret")));

				cloudinary.uploader().upload(targetFile, ObjectUtils.asMap("use_filename", true, "unique_filename", false, "resource_type", "auto"));

			}
		} catch (Exception caught) {
			throw new RuntimeException(caught);
		}
	}

}
