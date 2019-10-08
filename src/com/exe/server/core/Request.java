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
 * 封装请求协议：封装请求参数为Map
 * @author Administrator
 *
 */
public class Request {
	//POST /index.html/ HTTP/1.1
	//GET /login.html?uname=qing&pwd=123 HTTP/1.1
	//协议信息
	private String requestInfo;
	//请求方式
	private String method;
	//请求url
	private String url;
	//请求参数
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
		//分解字符串
		parseRequestInfo();
	}
	//分解字符串
	private void parseRequestInfo() {
		System.out.println("---分解---");
		System.out.println("--- 1、获取请求方式：开头到第一个/ ---");
		this.method=this.requestInfo.substring(0,this.requestInfo.indexOf("/")).toLowerCase().trim();
		System.out.println(method);
		System.out.println("--- 2、获取请求url：第一个/到HTTP/ ---");
		System.out.println("--- 可能包含请求参数？前面的为url ---");
		//1)获取/的位置
		int startIdx = this.requestInfo.indexOf("/")+1; 
		//2)获取HTTP/的位置
		int endIdx = this.requestInfo.indexOf("HTTP/")-1;
		//3)分割字符串
		this.url=this.requestInfo.substring(startIdx,endIdx).trim();
		//4)获取?的位置
		int queryIdx=this.url.indexOf("?");
		if(queryIdx>=0) {//表示存在请求参数
			String[] urlArray = this.url.split("\\?");//必须转义
			this.url = urlArray[0];
			queryStr = urlArray[1];
		}
		System.out.println(this.url);

		System.out.println("--- 3、获取请求参数：如果是GET已经获取，如果是POST可能在请求体中---");
		if(method.equals("post")) {
			String qStr = this.requestInfo.substring(this.requestInfo.lastIndexOf(CRLF)).trim();
			if(null==queryStr) {
				queryStr = qStr;
			}else {
				queryStr += "&"+qStr;
			}
		}
		queryStr = null==queryStr?"": queryStr;
		System.out.println(method+"-->"+url+"-->"+queryStr);//queryStr中文会乱码
		//转成Map fav=1&fav=2&uname=xxx&pwd=xxx&other=  //一对多（List）
		convertMap();
	}
	//处理请求参数为Map
	public void convertMap() {
		//分割字符串 &
		String[] keyValues = this.queryStr.split("&");
		for(String queryStr:keyValues) {
			//再次分割字符串 =
			String[] kv = queryStr.split("=");
			kv = Arrays.copyOf(kv, 2);//避免出现只有键没有值的情况
			//获取key 和 value
			String key = kv[0];
			String value = kv[1]==null?null:decode(kv[1],"utf-8");
			//存储到map中
			if(!parameterMap.containsKey(key)) {//第一次
				parameterMap.put(key, new ArrayList<String>());
			}
			parameterMap.get(key).add(value);//通过key找到容器，往容器里面添值
		}
	}
	
	//处理中文
	public String decode(String value,String enc) {
		try {
			return java.net.URLDecoder.decode(value, enc);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 通过name获取对应的多个值
	 * @param key
	 * @return
	 */
	public String[] getParameterValue(String key) {
		List<String> values = this.parameterMap.get(key);
		if(null==values||values.size()<1) {
			return null;
		}
		//<T> T[] toArray(T[] a) 方法返回的数组类型一定是集合中所储存的类型
		return values.toArray(new String[0]);//返回数组方便后面学习
	}
	
	/**
	 * 通过name获取对应的一个值
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
