package com.exe.server.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ��װ����Э�飺��װ�������ΪMap
 * @author Administrator
 *
 */
public class Request {
	//POST /index.html/ HTTP/1.1
	//GET /login.html?uname=qing&pwd=123 HTTP/1.1
	//Э����Ϣ
	private String requestInfo;
	//����ʽ
	private String method;
	//����url
	private String url;
	//�������
	private String queryStr;
	private Map<String,List<String>> parameterMap;
	private final String CRLF="\r\n";
	public Request(Socket client) throws IOException {
		this(client.getInputStream());	
	}
	public Request(InputStream is) {
		parameterMap = new HashMap<String,List<String>>();
		byte[] datas = new byte[1024*1024];
		int len;
		try {
			len = is.read(datas);
			this.requestInfo = new String(datas,0,len);
		} catch (IOException e) {
			e.printStackTrace();
			return ;
		}
		//�ֽ��ַ���
		parseRequestInfo();
	}
	//�ֽ��ַ���
	private void parseRequestInfo() {
		System.out.println("---�ֽ�---");
		System.out.println("--- 1����ȡ����ʽ����ͷ����һ��/ ---");
		this.method=this.requestInfo.substring(0,this.requestInfo.indexOf("/")).toLowerCase().trim();
		System.out.println(method);
		System.out.println("--- 2����ȡ����url����һ��/��HTTP/ ---");
		System.out.println("--- ���ܰ������������ǰ���Ϊurl ---");
		//1)��ȡ/��λ��
		int startIdx = this.requestInfo.indexOf("/")+1; 
		//2)��ȡHTTP/��λ��
		int endIdx = this.requestInfo.indexOf("HTTP/")-1;
		//3)�ָ��ַ���
		this.url=this.requestInfo.substring(startIdx,endIdx).trim();
		//4)��ȡ?��λ��
		int queryIdx=this.url.indexOf("?");
		if(queryIdx>=0) {//��ʾ�����������
			String[] urlArray = this.url.split("\\?");//����ת��
			this.url = urlArray[0];
			queryStr = urlArray[1];
		}
		System.out.println(this.url);

		System.out.println("--- 3����ȡ��������������GET�Ѿ���ȡ�������POST��������������---");
		if(method.equals("post")) {
			String qStr = this.requestInfo.substring(this.requestInfo.lastIndexOf(CRLF)).trim();
			if(null==queryStr) {
				queryStr = qStr;
			}else {
				queryStr += "&"+qStr;
			}
		}
		queryStr = null==queryStr?"": queryStr;
		System.out.println(method+"-->"+url+"-->"+queryStr);//queryStr���Ļ�����
		//ת��Map fav=1&fav=2&uname=xxx&pwd=xxx&other=  //һ�ԶࣨList��
		convertMap();
	}
	//�����������ΪMap
	public void convertMap() {
		//�ָ��ַ��� &
		String[] keyValues = this.queryStr.split("&");
		for(String queryStr:keyValues) {
			//�ٴηָ��ַ��� =
			String[] kv = queryStr.split("=");
			kv = Arrays.copyOf(kv, 2);//�������ֻ�м�û��ֵ�����
			//��ȡkey �� value
			String key = kv[0];
			String value = kv[1]==null?null:decode(kv[1],"utf-8");
			//�洢��map��
			if(!parameterMap.containsKey(key)) {//��һ��
				parameterMap.put(key, new ArrayList<String>());
			}
			parameterMap.get(key).add(value);//ͨ��key�ҵ�������������������ֵ
		}
	}
	
	//��������
	public String decode(String value,String enc) {
		try {
			return java.net.URLDecoder.decode(value, enc);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * ͨ��name��ȡ��Ӧ�Ķ��ֵ
	 * @param key
	 * @return
	 */
	public String[] getParameterValue(String key) {
		List<String> values = this.parameterMap.get(key);
		if(null==values||values.size()<1) {
			return null;
		}
		//<T> T[] toArray(T[] a) �������ص���������һ���Ǽ����������������
		return values.toArray(new String[0]);//�������鷽�����ѧϰ
	}
	
	/**
	 * ͨ��name��ȡ��Ӧ��һ��ֵ
	 * @param key
	 * @return
	 */
	public String getParameter(String key) {
		String[] values = getParameterValue(key);
		return values==null?null:values[0];
	}
	
	public String getMethod() {
		return method;
	}

	public String getUrl() {
		return url;
	}

	public String getQueryStr() {
		return queryStr;
	}

}
