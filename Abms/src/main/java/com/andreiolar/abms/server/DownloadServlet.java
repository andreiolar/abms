package com.andreiolar.abms.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadServlet extends HttpServlet {

	private static final long serialVersionUID = -3484626486124019731L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String fileName = req.getParameter("fileInfo");

		resp.setContentType("application/octet-stream");
		resp.setHeader("Content-Disposition:", "attachment;filename=" + "\"" + fileName + "\"");

		File myFile = new File(System.getProperty("user.dir") + "/files/general", fileName);

		OutputStream out = resp.getOutputStream();
		FileInputStream in = new FileInputStream(myFile);
		byte[] buffer = new byte[4096];
		int length;

		while ((length = in.read(buffer)) > 0) {
			out.write(buffer, 0, length);
		}

		in.close();
		out.flush();

	}

}
