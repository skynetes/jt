package com.jt.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperUtil {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	//1.将对象转化为json
	//如果程序有异常,转化为运行时异常
	public static String toJSON(Object obj) {

		String json = null;
		try {
			json = MAPPER.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return json;
	}
	
	
	/**
	 * 要求:用户传递什么类型,返回什么对象.
	 * 方案: 使用泛型对象实现!!!
	 * @param json
	 * @param targetClass
	 * @return
	 */
	public static <T> T toObject(String json,Class<T> targetClass) {

		T t = null;
		try {
			t = MAPPER.readValue(json, targetClass);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return t;
	}
}
