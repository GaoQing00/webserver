package com.exe.server.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
/**
 * 分发器：加入状态 内容处理 404 505 首页
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
			//获取请求协议  
			request = new Request(client);
			//获取响应协议
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
			
			//不在出现new的情况
			Servlet servlet = WebAPP.getServletFromUrl(request.getUrl());
			if(null!=servlet) {
				servlet.service(request, reponse);
				//关注了状态码
				reponse.pushToBrowser(200);	
			}else {
				//错误
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
