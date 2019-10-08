package com.exe.user;

import com.exe.server.core.Request;
import com.exe.server.core.Response;
import com.exe.server.core.Servlet;

public class OthersServlet implements Servlet {

	@Override
	public void service(Request request,Response reponse) {
		reponse.print("other test page");

	}

}
