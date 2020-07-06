package com.jt.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.ItemDesc;
import com.jt.util.ObjectMapperUtil;

@RestController
public class JSONPController {
	
	/**
	 * 接收跨域请求
	 * 1.url地址 http://manage.jt.com/web/testJSONP?callback=jQuery1111037519146002454984_1576562827901&_=1576562827902
	 * 2.获取参数  callback
	 * 3.返回值类型 callback(JSONdata)
	 */
	
	//@RequestMapping("/web/testJSONP")
	public String jsonpOld(String callback) {
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(1001L)
				.setItemDesc("商品详情信息!!!");
		String json = ObjectMapperUtil.toJSON(itemDesc);
		
		return callback +"("+json+")";
	}
	
	@RequestMapping("/web/testJSONP")
	public JSONPObject jsonp(String callback) {
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(1001L)
				.setItemDesc("商品详情信息!!!");
		
		return new JSONPObject(callback, itemDesc);
	}
}	
