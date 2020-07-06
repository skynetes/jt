package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

//该类为系统级返回值VO对象,判断后台操作是否正确
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class SysResult {
	private Integer status; //200表示成功 201表示失败
	private String msg;		//响应提示信息
	private Object data;	//服务器响应数据

	/**
	 * 为了以后调用方便,封装工具API static
	 */
	public static SysResult success() {

		return new SysResult(200,"服务器处理成功!!", null);
	}

	public static SysResult success(Object data) {

		return new SysResult(200,"服务器处理成功!!", data);
	}

	public static SysResult success(String msg,Object data) {

		return new SysResult(200,msg, data);
	}

	public static SysResult fail() {

		return new SysResult(201,"处理失败", null);
	}

}
