package com.exe.server.core;
/**
 * 目标：处理 404 505 和首页
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private ServerSocket serverSocket;
	private boolean isRunning;
	public static void main(String[] main) {
		Server server = new Server();
		server.start();
	}
	//启动服务
	public void start() {
		try {
			serverSocket = new ServerSocket(8888);
			isRunning = true;
			receive();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("服务器启动失败...");
		}
		
	}
	//接受连接处理
	public void receive() {
		try {
			while(isRunning) {
				Socket client = serverSocket.accept();//阻塞式等待
				System.out.println("一个客户端建立了连接...");
				//多线程处理
				new Thread(new Dispatcher(client)).start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("客户端错误");
			stop();
		}
	}
	//停止服务
	public void stop() {
		isRunning = false;
		try {
			this.serverSocket.close();
			System.out.println("服务器已停止");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
