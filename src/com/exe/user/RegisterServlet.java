package com.exe.user;

import com.exe.server.core.Request;
import com.exe.server.core.Response;
import com.exe.server.core.Servlet;

public class RegisterServlet implements Servlet {

	@Override
	public void service(Request request,Response response) {
		//��עҵ���߼�
		String uname = request.getParameter("uname");
		String[] favs = request.getParameterValue("fav");
		/**
		 * дHTML�ļ��Ƚ��鷳
		 * JSP����дҳ�棬�ܷ��㣬�ײ��ת��servlet
		 */
		response.print("<html>");
		response.print("<head>");
		//response.print("<meta http-equiv=\"content-type\"content=\"text/html;charset=UTF-8\">");
		response.print("<title>");
		response.print("ע��ɹ�");
		response.print("</title>");
		response.print("</head>");
		response.print("<body>");
		response.println("��ע�����ϢΪ��"+uname);
		response.println("\n");
		response.println("��ϲ�������ս�ĿΪ��");
		for(String fav:favs) {
			//if(fav=="0") {
			if(fav.equals("0")) {
				response.print("������ս");
			}else if(fav.equals("1")) {
				response.print("���ܰ�");
			}else if(fav.equals("2")) {
				response.print("���ƶ�����");
			}else if(fav.equals("3")) {
				response.print("����������");
			}
		}
		response.print("</body>");
		response.print("</html>");
	}

}
