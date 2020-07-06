package com.jt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {
	
	/**
	 * http://localhost:8092/saveUser/{id}/{name}/{age}
	 * 目的:实现通用页面跳转
	 * url地址:
	 * 	/page/item-add
	 *  /page/item-list
	 *  /page/item-param-list
	 * 
	 * 特点:
	 * 	1.前缀相同
	 *  2.请求页面的url地址与页面名称一致的
	 *  
	 * 实现思路:
	 * 	1.需要动态获取页面url的数据信息
	 * 
	 * 实现步骤:  
	 * 	 利用restFul风格(一)
	 * 		1.参数与参数之间.需要使用/分割
	 * 		2.使用{}标识参数,并且自定义参数名称
	 * 		3.方法中定义参数,并且利用注解转化
	 * 	
	 *  利用restFul风格实现通用url.完成数据库CRUD
	 * 	案例:
	 * 		/user/saveUser   新增      post
	 * 		/user/updateUser 修改	put
	 * 		/user/deleteUser 删除	delete
	 * 		/user/findUser   查询       get
	 * 		/user	type="XXXX"		自动实现CRUD
	 */
	
	@RequestMapping("/page/{moduleName}")
	public String module(@PathVariable String moduleName) {
		
		return moduleName;
	}
	
}
