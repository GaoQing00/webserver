package com.exe.server.core;
/**
 * Ŀ�꣺���� 404 505 ����ҳ
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
	//��������
	public void start() {
		try {
			serverSocket = new ServerSocket(8888);
			isRunning = true;
			receive();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("����������ʧ��...");
		}
		
	}
	//�������Ӵ���
	public void receive() {
		try {
			while(isRunning) {
				Socket client = serverSocket.accept();//����ʽ�ȴ�
				System.out.println("һ���ͻ��˽���������...");
				//���̴߳���
				new Thread(new Dispatcher(client)).start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("�ͻ��˴���");
			stop();
		}
	}
	//ֹͣ����
	public void stop() {
		isRunning = false;
		try {
			this.serverSocket.close();
			System.out.println("��������ֹͣ");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
