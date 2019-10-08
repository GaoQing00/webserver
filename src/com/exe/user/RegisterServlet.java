package com.exe.user;

import com.exe.server.core.Request;
import com.exe.server.core.Response;
import com.exe.server.core.Servlet;

public class RegisterServlet implements Servlet {

	@Override
	public void service(Request request,Response response) {
		//关注业务逻辑
		String uname = request.getParameter("uname");
		String[] favs = request.getParameterValue("fav");
		/**
		 * 写HTML文件比较麻烦
		 * JSP用于写页面，很方便，底层会转成servlet
		 */
		response.print("<html>");
		response.print("<head>");
		//response.print("<meta http-equiv=\"content-type\"content=\"text/html;charset=UTF-8\">");
		response.print("<title>");
		response.print("注册成功");
		response.print("</title>");
		response.print("</head>");
		response.print("<body>");
		response.println("你注册的信息为："+uname);
		response.println("\n");
		response.println("你喜欢的综艺节目为：");
		for(String fav:favs) {
			//if(fav=="0") {
			if(fav.equals("0")) {
				response.print("极限挑战");
			}else if(fav.equals("1")) {
				response.print("奔跑吧");
			}else if(fav.equals("2")) {
				response.print("王牌对王牌");
			}else if(fav.equals("3")) {
				response.print("向往的生活");
			}
		}
		response.print("</body>");
		response.print("</html>");
	}

}
