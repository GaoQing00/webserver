package com.exe.user;

import com.exe.server.core.Request;
import com.exe.server.core.Response;
import com.exe.server.core.Servlet;

public class LoginServlet implements Servlet {

	@Override
	public void service(Request request,Response response) {
		response.print("<html>");
		response.print("<head>");
		//加上这一句反而是乱码？
		//response.print("<meta http-equiv=\"content-type\"content=\"text/html;charset=UTF-8\">");
		response.print("<title>");
		response.print("登录成功");
		response.print("</title>");
		response.print("</head>");
		response.print("<body>");
		response.print("welcome back-->"+request.getParameter("uname").toString());
		response.print("</body>");
		response.print("</html>");
	}

}
