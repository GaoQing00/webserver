package com.exe.server.core;
/**
 * <servlet>
   <servlet-name>login</servlet-name>
   <servlet-class>com.exe.server.basic.servlet</servlet-class>
   </servlet>
 * @author Administrator
 *
 */
public class Entity {
	private String name;
	private String clz;
	public Entity(){
		
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClz() {
		return clz;
	}
	public void setClz(String clz) {
		this.clz = clz;
	}

}
