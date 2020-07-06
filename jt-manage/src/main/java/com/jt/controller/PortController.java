package com.jt.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class PortController {
	
	//动态获取端口号信息
	@Value("${server.port}")
	private Integer port;
	
	@RequestMapping("/getPort")
	public Integer getPort() {
		
		return port;
	}
}
