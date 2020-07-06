package com.jt.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.pojo.ItemDesc;

public class ObjectTOString {
	
	private static final ObjectMapper MAPPER = 
									new ObjectMapper();
	
	/**
	 * {key:value,key2:value.....} 
	 * @throws IOException 
	 */
	
	@Test
	public void testObj() throws IOException {
		ItemDesc desc = new ItemDesc();
		desc.setItemId(1000L)
			.setItemDesc("描述信息")
			.setCreated(new Date())
			.setUpdated(desc.getCreated());
		
		String json = MAPPER.writeValueAsString(desc);
		System.out.println(json);
		
		//2.将json串转化为对象
		ItemDesc desc2 = 
				MAPPER.readValue(json, ItemDesc.class);
		System.out.println(desc2.toString());
		
	}
	
	
	/**
	 * 将List集合转化为json串
	 * 结构: [{key:vale},{key:value}]
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked") //压制警告
	@Test
	public void testList() throws IOException {
		ItemDesc desc1 = new ItemDesc();
		desc1.setItemId(1000L).setItemDesc("描述信息").setCreated(new Date()).setUpdated(desc1.getCreated());
		ItemDesc desc2 = new ItemDesc();
		desc2.setItemId(1000L).setItemDesc("描述信息").setCreated(new Date()).setUpdated(desc2.getCreated());
		List<ItemDesc> list = new ArrayList<>();
		list.add(desc1);
		list.add(desc2);
		//对象转化为json
		String json = MAPPER.writeValueAsString(list);
		System.out.println(json);
		
		//2.将json转化为对象
		List<ItemDesc> list2 = 
				MAPPER.readValue(json, list.getClass());
		System.out.println(list2);
	}
	
}
