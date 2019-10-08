package com.exe.server.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebContext {
	List<Entity> entitys = null;
	List<Mapping> mappings = null;
	
	//key-->servlet-name value-->servlet-class
	private Map<String,String> entityMap = new HashMap<String,String>();
	//key-->url-pattern value-->servlet-name
	private Map<String,String> mappingMap = new HashMap<String,String>();
	
	public WebContext(List<Entity> entitys, List<Mapping> mappings) {
		this.entitys = entitys;
		this.mappings = mappings;
		
		//将entity的List转成对应的map
		for(Entity entity:entitys) {
			entityMap.put(entity.getName(), entity.getClz());
		}
		//将map的List转成对应的map
		for(Mapping mapping:mappings) {
			for(String pattern:mapping.getPatterns()) {
				mappingMap.put(pattern, mapping.getName());//url-pattern为键值，是唯一的
			}
		}
	}
	
	/**
	 * 通过url的路径找到对应的class
	 */
	public String getClz(String pattern){
		String name = mappingMap.get(pattern);//key(pattern)-->value(name)
		return entityMap.get(name);//key(name)-->value(class)
	}
}
