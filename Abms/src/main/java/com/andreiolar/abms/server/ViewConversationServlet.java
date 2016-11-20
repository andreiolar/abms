package com.andreiolar.abms.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

public class ViewConversationServlet extends HttpServlet {

	private static final long serialVersionUID = -6333288871497868174L;

	@Override
	public void service(ServletRequest req, ServletResponse resp) throws ServletException, IOException {

		String id = req.getParameter("id");
		System.out.print("");

	}

}
