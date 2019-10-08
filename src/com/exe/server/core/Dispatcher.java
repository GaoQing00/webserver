package com.exe.server.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
/**
 * �ַ���������״̬ ���ݴ��� 404 505 ��ҳ
 * @author Administrator
 *
 */
public class Dispatcher implements Runnable{
	private Socket client;
	private Response reponse;
	private Request request;
	public Dispatcher(Socket client) {
		this.client = client;
		try {
			//��ȡ����Э��  
			request = new Request(client);
			//��ȡ��ӦЭ��
			reponse = new Response(client);
		} catch (IOException e) {
			e.printStackTrace();
			this.release();
		}
	}

	@Override
	public void run() {
		try {
			if(null==request.getUrl()||request.getUrl().equals("")) {
				InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("index.html");
				byte[] datas = new byte[1024*1024];
				int len = is.read(datas);
				reponse.print(new String(datas));
				reponse.pushToBrowser(200);
				is.close();
				return ;
			}
			
			//���ڳ���new�����
			Servlet servlet = WebAPP.getServletFromUrl(request.getUrl());
			if(null!=servlet) {
				servlet.service(request, reponse);
				//��ע��״̬��
				reponse.pushToBrowser(200);	
			}else {
				//����
				InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("error.html");
				byte[] datas = new byte[1024*1024];
				int len = is.read(datas);
				reponse.print(new String(datas));
				reponse.pushToBrowser(404);	
				is.close();
			}
		}catch(Exception e) {
			reponse.print("");
			reponse.pushToBrowser(505);
		}
		release();
	}
	
	private void release() {
		try {
			client.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
